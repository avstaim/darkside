package com.avstaim.darkside.slab

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.FragmentActivity
import com.avstaim.darkside.R

internal object SlabHooks {

    fun getWindowEvents(someView: View): WindowEventsHookView {
        val tag = someView.getTag(R.id.slab_window_events_hook_view)
        return if (tag is WindowEventsHookView) {
            tag
        } else {
            getWindowEvents(findActivity(someView.context)).also { hookView ->
                someView.setTag(R.id.slab_window_events_hook_view, hookView)
            }
        }
    }

    private fun getWindowEvents(activity: Activity): WindowEventsHookView {
        activity.findViewById<WindowEventsHookView>(R.id.slab_window_events_hook_view)?.let { return it }

        return WindowEventsHookView(activity).apply {
            id = R.id.slab_window_events_hook_view
            activity.addContentView(this, FrameLayout.LayoutParams(0, 0))
        }
    }

    fun findActivity(context: Context?): Activity =
        when(context) {
            is Activity -> context
            is ContextWrapper -> findActivity(context.baseContext)
            else -> throw IllegalArgumentException("Unsupported context $context")
        }


    operator fun get(context: Context): SlabHookResultFragment {
        val activity: Activity = findActivity(context) as? FragmentActivity ?: error("not a fragment activity")
        val fm = (activity as FragmentActivity).supportFragmentManager
        val fragment = fm.findFragmentByTag(SlabHookResultFragment.FRAGMENT_TAG)
        if (fragment is SlabHookResultFragment) {
            return fragment
        }
        val hookResultFragment = SlabHookResultFragment()
        fm.beginTransaction().add(hookResultFragment, SlabHookResultFragment.FRAGMENT_TAG).commitNowAllowingStateLoss()
        return hookResultFragment
    }
}
