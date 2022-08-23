@file:Suppress("unused")

package com.avstaim.darkside.cookies.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.contract.ActivityResultContract
import com.avstaim.darkside.common.optin.DelicateApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.reflect.KClass

/**
 * Allows to start another activity in async way, suspending given coroutine until result is obtained.
 *
 * @param A activity to launch
 * @param P parameter type to pass to opening activity
 * @param R result type, waiting to get from activity launching
 *
 * @param param data to pass to opening activity
 * @param serializer used to serialize [param] to [Bundle]
 * @param parser used to deserialize result from [Bundle]
 *
 * @return result got from activity or `null` if there was no one
 *
 * WARNING! This api is delicate and should be used with a great caution.
 *
 * When you starting an activity for result and configuration is changed: most activities are re-created.
 * This means the coroutine will be cancelled and result will never be got.
 *
 * You can use this api only when you can be sure that activity is never re-created
 * or you can safely loose a request in such case.
 */
@DelicateApi
suspend inline fun <reified A : Activity, P, reified R> ActivityResultCaller.requestActivityForResult(
    param: P,
    noinline serializer: (P) -> Bundle,
    noinline parser: (Bundle) -> R,
): R? {
    return suspendCancellableCoroutine { continuation ->
        val launcher = registerForActivityResult(
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

/**
 * Allows to start another activity in async way, suspending given coroutine until result is obtained.
 *

 * @param R result type, waiting to get from activity launching
 *
 * @param intent to open an activity
 * @param parser used to deserialize result from [Bundle]
 *
 * @return result got from activity or `null` if there was no one
 *
 * WARNING! This api is delicate and should be used with a great caution.
 *
 * When you starting an activity for result and configuration is changed: most activities are re-created.
 * This means the coroutine will be cancelled and result will never be got.
 *
 * You can use this api only when you can be sure that activity is never re-created
 * or you can safely loose a request in such case.
 */
@DelicateApi
suspend inline fun <reified R> ActivityResultCaller.requestActivityForResult(
    intent: Intent,
    noinline parser: (Intent) -> R,
): R? {
    return suspendCancellableCoroutine { continuation ->
        val launcher = registerForActivityResult(
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

/**
 * Allows to start another activity in async way, suspending given coroutine until result is obtained.
 *
 * @param intent to open an activity
 *
 * @return an [ActivityResult] with raw data got from activity or `null` if there was no one
 *
 * WARNING! This api is delicate and should be used with a great caution.
 *
 * When you starting an activity for result and configuration is changed: most activities are re-created.
 * This means the coroutine will be cancelled and result will never be got.
 *
 * You can use this api only when you can be sure that activity is never re-created
 * or you can safely loose a request in such case.
 */
@DelicateApi
suspend inline fun ActivityResultCaller.requestActivityForResult(
    intent: Intent,
): ActivityResult {
    return suspendCancellableCoroutine { continuation ->
        val launcher = registerForActivityResult(
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

sealed class ResultCode(val code: Int) {
    object Ok : ResultCode(Activity.RESULT_OK)
    object Cancelled : ResultCode(Activity.RESULT_CANCELED)
    class Other(code: Int) : ResultCode(code)
}

data class ActivityResult(
    val code: ResultCode,
    val intent: Intent?,
)
