package com.avstaim.darkside.slab

import android.content.res.Configuration
import android.os.Handler
import android.os.Looper
import android.view.View
import com.avstaim.darkside.slab.SlabHooks.getWindowEvents

/**
 * Base logic to get [SlabLifecycle] events.
 */
internal open class SlabController(
    private val targetLifecycle: SlabLifecycle,
    private val delayAttachToLayout: Boolean,
) : View.OnAttachStateChangeListener, WindowEventsHookView.Listener {

    private val mainHandler = Handler(Looper.getMainLooper())
    private var windowEventsHookView: WindowEventsHookView? = null
    var isAttached = false
        private set
    private var isActivityStarted = false
    private var isActivityResumed = false
    private var isContainerVisible = false

    override fun onViewAttachedToWindow(v: View) {
        if (windowEventsHookView != null) return
        windowEventsHookView = getWindowEvents(v).also { hookView ->
            hookView.addListener(this)
            isActivityStarted = hookView.isActivityStarted
            isActivityResumed = hookView.isActivityResumed
            isContainerVisible = true
        }

        if (!delayAttachToLayout) {
            dispatchAttached()
        } else {
            // Layout will not happen until activity start but it seems that onAttach is expected, so
            // this posted task will race against the layout traversal.
            mainHandler.post { dispatchAttached() }
        }
    }

    private fun dispatchAttached() {
        mainHandler.removeCallbacksAndMessages(null)
        if (isAttached) return
        isAttached = true
        targetLifecycle.onAttach()
        if (isContainerVisible) {
            if (isActivityStarted) {
                targetLifecycle.onStart()
            }
            if (isActivityResumed) {
                targetLifecycle.onResume()
            }
        }
    }

    override fun onViewDetachedFromWindow(v: View) {
        mainHandler.removeCallbacksAndMessages(null)
        if (windowEventsHookView == null) return
        if (isAttached) {
            if (isContainerVisible) {
                if (isActivityResumed) {
                    targetLifecycle.onPause()
                }
                if (isActivityStarted) {
                    targetLifecycle.onStop()
                }
            }
            isActivityResumed = false
            isActivityStarted = false
        }
        if (isAttached) {
            targetLifecycle.onDetach()
            isAttached = false
        }
        windowEventsHookView?.removeListener(this)
        windowEventsHookView = null
    }

    override fun onLayout() = dispatchAttached()

    override fun onActivityResumeChanged(resumed: Boolean) {
        if (isActivityResumed == resumed) return
        isActivityResumed = resumed
        if (!isAttached) return
        if (isContainerVisible) {
            if (isActivityResumed) {
                targetLifecycle.onResume()
            } else {
                targetLifecycle.onPause()
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        if (isContainerVisible && isActivityResumed) {
            targetLifecycle.onConfigurationChanged(newConfig)
        }
    }

    override fun onActivityStartChanged(started: Boolean) {
        if (isActivityStarted == started) return
        isActivityStarted = started
        if (!isAttached) return
        if (isContainerVisible) {
            if (isActivityStarted) {
                targetLifecycle.onStart()
            } else {
                targetLifecycle.onStop()
            }
        }
        isActivityStarted = started
    }
}
