@file:Suppress("unused", "NOTHING_TO_INLINE")

package com.avstaim.darkside.cookies.ui

import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.core.view.doOnLayout
import com.avstaim.darkside.cookies.Size
import kotlinx.coroutines.Job

var View.visible: Boolean
    get() = visibility == View.VISIBLE
    set(value) {
        visibility = if (value) View.VISIBLE else View.GONE
    }

suspend fun View.waitForLayout() {
    val job = Job()
    doOnLayout {
        job.complete()
    }
    job.join()
}

fun View.dispatchVisibility(threshold: Size) {
    invalidateShouldShow(threshold)
    setOnApplyWindowInsetsListener { _, insets ->
        invalidateShouldShow(threshold)
        insets
    }
}

private fun View.invalidateShouldShow(threshold: Size) {
    visible = windowHeight.px >= threshold.px
}

/**
 * @return true when a receiver [View] is currently actually visible inside it's ancestor [ViewGroup]
 * (it can not obligatory be direct parent). [View] is considered visible even if only a small part of it is visible.
 */
fun View.isVisibleIn(ancestor: ViewGroup): Boolean {
    if (!isShown) return false
    val actualPosition = this.positionRect
    val ancestorPosition = ancestor.positionRect

    return actualPosition.intersect(ancestorPosition)
}

/**
 * Get actual position rect (left, top, right, bottom) in screen-bases coordinates.
 */
val View.positionRect: Rect
    get() = IntArray(2).also { getLocationOnScreen(it) }.let { Rect(it[0], it[1], it[0] + width, it[1] + height) }

/**
 * Sets [ViewTreeObserver.OnPreDrawListener] to given view
 */
fun View.onPreDraw(action: () -> Boolean) {
    viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
        override fun onPreDraw(): Boolean {
            viewTreeObserver.removeOnPreDrawListener(this)
            return action.invoke()
        }
    })
}
