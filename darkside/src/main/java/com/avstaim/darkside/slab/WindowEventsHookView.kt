package com.avstaim.darkside.slab

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.res.Configuration
import android.graphics.Canvas
import android.os.Bundle
import android.view.View
import com.avstaim.darkside.slab.SlabHooks.findActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner

/**
 * Invisible view inserted into activity layout to receive window visibility and focus changes.
 */
@SuppressLint("ViewConstructor")
internal class WindowEventsHookView(
    private val activity: Activity,
) : View(activity), Application.ActivityLifecycleCallbacks, DefaultLifecycleObserver {

    private val observerList = ObserverList<Listener>()
    private val observerListIterator = observerList.reverseIterator()
    private var lifecycle: Lifecycle? = null

    /**
     * @return true if container activity is currently visible.
     */
    var isActivityStarted = false
        private set

    /**
     * @return true if container activity is currently resumed.
     */
    var isActivityResumed = false
        private set


    init {
        setWillNotDraw(true)
    }

    fun addListener(listener: Listener) {
        observerList.addObserver(listener)
    }

    fun removeListener(listener: Listener) {
        observerList.removeObserver(listener)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        val activity = findActivity(context)

        // Lifecycle can tell the actual state without guess.
        if (activity is FragmentActivity) {
            lifecycle = activity.lifecycle
            val state = lifecycle!!.currentState
            isActivityStarted = state.isAtLeast(Lifecycle.State.STARTED)
            isActivityResumed = state.isAtLeast(Lifecycle.State.RESUMED)
            lifecycle!!.addObserver(this)
        } else {
            // Try to guess activity status based on the window state.
            isActivityStarted = windowVisibility == VISIBLE
            isActivityResumed = isActivityStarted && this.activity.window.isActive
            activity.application.registerActivityLifecycleCallbacks(this)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(0, 0)
    }

    override fun layout(l: Int, t: Int, r: Int, b: Int) {
        super.layout(l, t, r, b)
        observerListIterator.rewind()
        while (observerListIterator.hasNext()) {
            observerListIterator.next().onLayout()
        }
    }

    @SuppressLint("MissingSuperCall") // ViewStub do the same
    override fun draw(canvas: Canvas) {
    }

    override fun dispatchDraw(canvas: Canvas) {}
    override fun onDetachedFromWindow() {
        activity.application.unregisterActivityLifecycleCallbacks(this)
        isActivityStarted = false
        isActivityResumed = false
        if (lifecycle != null) {
            lifecycle!!.removeObserver(this)
            lifecycle = null
        }
        super.onDetachedFromWindow()
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
    override fun onStart(owner: LifecycleOwner) {
        if (isActivityStarted) return
        isActivityStarted = true
        dispatchStartChanged()
    }

    override fun onResume(owner: LifecycleOwner) {
        if (isActivityResumed) return
        isActivityResumed = true
        dispatchResumeChanged()
    }

    override fun onPause(owner: LifecycleOwner) {
        if (!isActivityResumed) return
        isActivityResumed = false
        dispatchResumeChanged()
    }

    override fun onStop(owner: LifecycleOwner) {
        if (!isActivityStarted) return
        isActivityStarted = false
        dispatchStartChanged()
    }

    override fun onActivityStarted(activity: Activity) {
        if (this.activity !== activity) {
            return
        }
        isActivityStarted = true
        dispatchStartChanged()
    }

    override fun onActivityResumed(activity: Activity) {
        if (this.activity !== activity) {
            return
        }
        isActivityResumed = true
        dispatchResumeChanged()
    }

    override fun onActivityPaused(activity: Activity) {
        if (this.activity !== activity) {
            return
        }
        isActivityResumed = false
        dispatchResumeChanged()
    }

    override fun onActivityStopped(activity: Activity) {
        if (this.activity !== activity) {
            return
        }
        isActivityStarted = false
        dispatchStartChanged()
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = Unit
    override fun onActivityDestroyed(activity: Activity) = Unit

    override fun onConfigurationChanged(newConfig: Configuration) {
        observerListIterator.rewind()
        while (observerListIterator.hasNext()) {
            observerListIterator.next().onConfigurationChanged(newConfig)
        }
    }

    private fun dispatchResumeChanged() {
        observerListIterator.rewind()
        while (observerListIterator.hasNext()) {
            observerListIterator.next().onActivityResumeChanged(isActivityResumed)
        }
    }

    private fun dispatchStartChanged() {
        observerListIterator.rewind()
        while (observerListIterator.hasNext()) {
            observerListIterator.next().onActivityStartChanged(isActivityStarted)
        }
    }

    internal interface Listener {
        fun onLayout()
        fun onActivityStartChanged(started: Boolean)
        fun onActivityResumeChanged(resumed: Boolean)
        fun onConfigurationChanged(newConfig: Configuration)
    }
}