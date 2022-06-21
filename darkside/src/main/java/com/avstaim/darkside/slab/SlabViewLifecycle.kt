@file:Suppress("unused")

package com.avstaim.darkside.slab

import android.app.Activity
import android.content.res.Configuration
import android.view.View
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment

/**
 * Utility helper to get all the the events from [SlabLifecycle] in the single [View].
 */
internal open class SlabViewLifecycle : SlabLifecycle {

    private var viewHelper: ViewHelper? = null

    /**
     * Attaches to the given view. If View is currently visible all corresponding events will be dispatched immediately.
     *
     * If current listener is already attached to the view, it will be detached with all event notified.
     */
    fun attachTo(view: View) {
        detach()
        viewHelper = ViewHelper(view).also { it.attachToView() }
    }

    /**
     * Called when the view is attached to the window. No activity of this component must start before this event.
     */
    @CallSuper
    override fun onAttach() = Unit

    /**
     * Same as [Activity.onStart] or [Fragment.onStart].
     */
    @CallSuper
    override fun onStart() = Unit

    /**
     * Same as [Activity.onResume] or [Fragment.onResume].
     */
    @CallSuper
    override fun onResume() = Unit

    /**
     * Same as [Activity.onPause] or [Fragment.onPause].
     */
    @CallSuper
    override fun onPause() = Unit
    /**
     * Same as [Activity.onStop] or [Fragment.onStop].
     */
    @CallSuper
    override fun onStop() = Unit

    /**
     * Called when the view is detached from the window or activity is destroyed.
     * [SlabViewLifecycle] is must be able to be freed by GC after that.
     */
    @CallSuper
    override fun onDetach() = Unit

    /**
     * @return currently attach view or throw if not attached.
     */
    fun requireAttachedView(): View =
        viewHelper?.view ?: error("No view in view helper")

    /**
     * Detached from the currently attached view with all required events.
     * This method is idempotent.
     */
    fun detach() {
        viewHelper?.detachFromView()
        viewHelper = null
    }

    override fun onCreate() = Unit
    override fun onDestroy() = Unit
    override fun onConfigurationChanged(newConfig: Configuration?) = Unit


    private inner class ViewHelper(val view: View) : SlabController(
        targetLifecycle = this@SlabViewLifecycle,
        delayAttachToLayout = false,
    ) {

        fun attachToView() {
            view.addOnAttachStateChangeListener(this)
            if (isAttachedToWindow(view)) {
                onViewAttachedToWindow(view)
            }
        }

        fun detachFromView() {
            view.removeOnAttachStateChangeListener(this)
            if (isAttachedToWindow(view)) {
                onViewDetachedFromWindow(view)
            }
        }
    }

    private fun isAttachedToWindow(view: View): Boolean = view.isAttachedToWindow
}
