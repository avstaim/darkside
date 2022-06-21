@file:Suppress("unused")

package com.avstaim.darkside.animations

import android.animation.Animator
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

fun CoroutineScope.animateAsync(init: DslAnimatorBuilder.() -> Unit): Deferred<Unit> =
    async {
        animator(init).startAndSuspend()
    }

suspend fun Animator.startAndSuspend() {
    suspendCancellableCoroutine<Unit> { continuation ->
        val animator = this
        animator.addListener(object : Animator.AnimatorListener {

            override fun onAnimationEnd(animation: Animator?) {
                if (continuation.isActive) {
                    continuation.resume(Unit)
                }
            }

            override fun onAnimationCancel(animation: Animator?) {
                if (continuation.isActive) {
                    continuation.resumeWithException(CancellationException())
                }
            }

            override fun onAnimationRepeat(animation: Animator?) = Unit
            override fun onAnimationStart(animation: Animator?) = Unit
        })
        continuation.invokeOnCancellation {
            animator.cancel()
        }
        animator.start()
    }
}