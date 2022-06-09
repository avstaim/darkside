package com.avstaim.darkside.slab

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.View
import android.widget.FrameLayout
import com.avstaim.darkside.R
import java.lang.IllegalArgumentException

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
}
