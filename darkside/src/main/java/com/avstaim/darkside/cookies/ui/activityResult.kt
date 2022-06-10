@file:Suppress("unused")

package com.avstaim.darkside.cookies.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContract
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.reflect.KClass

suspend inline fun <reified A : Activity, P, reified R> ComponentActivity.requestActivityForResult(
    key: String = randomKey(),
    param: P,
    noinline serializer: (P) -> Bundle,
    noinline parser: (Bundle) -> R,
): R? {
    return suspendCancellableCoroutine { continuation ->
        val launcher = activityResultRegistry.register(
            key,
            HelperContract<P, R>(A::class, serializer, parser),
        ) { result ->
            if (continuation.isActive) {
                continuation.resume(result)
            }
        }
        launcher.launch(param)

        continuation.invokeOnCancellation {
            launcher.unregister()
        }
    }
}

suspend inline fun <reified R> ComponentActivity.requestActivityForResult(
    key: String = randomKey(),
    intent: Intent,
    noinline parser: (Intent) -> R,
): R? {
    return suspendCancellableCoroutine { continuation ->
        val launcher = activityResultRegistry.register(
            key,
            HelperContractSimple(intent, parser),
        ) { result ->
            if (continuation.isActive) {
                continuation.resume(result)
            }
        }
        launcher.launch(Unit)

        continuation.invokeOnCancellation {
            launcher.unregister()
        }
    }
}

suspend inline fun ComponentActivity.requestActivityForResult(
    key: String = randomKey(),
    intent: Intent,
): ActivityResult {
    return suspendCancellableCoroutine { continuation ->
        val launcher = activityResultRegistry.register(
            key,
            HelperContractNoParse(intent),
        ) { result ->
            if (continuation.isActive) {
                continuation.resume(result)
            }
        }
        launcher.launch(Unit)

        continuation.invokeOnCancellation {
            launcher.unregister()
        }
    }
}

class HelperContract<P, R>(
    private val activityClass: KClass<out Activity>,
    private val serializer: (P) -> Bundle,
    private val parser: (Bundle) -> R,
) : ActivityResultContract<P, R?>() {

    override fun createIntent(context: Context, input: P): Intent =
        Intent(context, activityClass.java).apply {
            replaceExtras(serializer(input))
        }

    override fun parseResult(resultCode: Int, intent: Intent?): R? =
        intent?.extras?.let(parser)
}

class HelperContractSimple<R>(
    private val intent: Intent,
    private val parser: (Intent) -> R,
) : ActivityResultContract<Unit, R?>() {

    override fun createIntent(context: Context, input: Unit) = intent
    override fun parseResult(resultCode: Int, intent: Intent?): R? = intent?.let(parser)
}

class HelperContractNoParse(
    private val intent: Intent,
) : ActivityResultContract<Unit, ActivityResult>() {

    override fun createIntent(context: Context, input: Unit) = intent
    override fun parseResult(resultCode: Int, intent: Intent?): ActivityResult =
        ActivityResult(
            code = when (resultCode) {
                Activity.RESULT_OK -> ResultCode.Ok
                Activity.RESULT_CANCELED -> ResultCode.Cancelled
                else -> ResultCode.Other(resultCode)
            },
            intent = intent,
        )
}

fun randomKey(): String =
    (0..(4..32).random()).map { ('a'..'z').random() }.joinToString()


sealed interface ResultCode {
    object Ok : ResultCode
    object Cancelled : ResultCode
    class Other(val code: Int) : ResultCode
}

data class ActivityResult(
    val code: ResultCode,
    val intent: Intent?,
)
