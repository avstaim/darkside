package com.avstaim.darkside.slab

import android.content.res.Configuration

/**
 * Lifecycle definition for lifecycle.
 */
internal interface SlabLifecycle {

    /**
     * Called when the [Slab] is created.
     */
    fun onCreate()

    /**
     * Called when underlying activity is destroyed.
     */
    fun onDestroy()

    /**
     * Called when the view is attached to the window. No activity of this component must start before this event.
     */
    fun onAttach()

    /**
     * Same as [Activity.onStart] or [Fragment.onStart].
     */
    fun onStart()

    /**
     * Same as [Activity.onResume] or [Fragment.onResume].
     */
    fun onResume()

    /**
     * Same as [Activity.onPause] or [Fragment.onPause].
     */
    fun onPause()

    /**
     * Same as [Activity.onStop] or [Fragment.onStop].
     */
    fun onStop()

    /**
     * Called when the view is detached from the window or activity is destroyed.
     * [Slab] is must be able to be freed by GC after that.
     */
    fun onDetach()

    /**
     * Same as [Activity.onConfigurationChanged] or
     * [Fragment.onConfigurationChanged].
     */
    fun onConfigurationChanged(newConfig: Configuration?)
}
