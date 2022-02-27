@file:Suppress("unused")

package com.avstaim.darkside.animations

import android.animation.Animator
import android.animation.ValueAnimator

/**
 * Used to simplify animator endin' listening.
 */
class AnimatorEndingListener(private val block: () -> Unit) : Animator.AnimatorListener {

    override fun onAnimationEnd(animation: Animator?) = block()

    override fun onAnimationRepeat(animation: Animator?) = Unit
    override fun onAnimationCancel(animation: Animator?) = Unit
    override fun onAnimationStart(animation: Animator?) = Unit
}

fun Animator.endingListener(block: () -> Unit) = addListener(AnimatorEndingListener(block))

inline fun <reified T> ValueAnimator.updateListener(crossinline block: (T) -> Unit) {
    addUpdateListener {
        (it.animatedValue as? T)?.let { block(it) }
            ?: error("Animated value of wrong type ${it.animatedValue::class}, when ${T::class} is expected")
    }
}
