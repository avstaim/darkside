package com.avstaim.darkside.cookies

import android.app.Activity
import android.content.ContextWrapper
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope

val Activity.lifecycleOwner: LifecycleOwner?
    get() = this as? LifecycleOwner

val Activity.coroutineScope: CoroutineScope
    get() = lifecycleOwner?.lifecycleScope ?: GlobalScope

val View.activity: Activity
    get() {
        when (val context = context) {
            is Activity -> return context
            is ContextWrapper -> {
                var contextWrapper = context
                while (contextWrapper is ContextWrapper) {
                    contextWrapper.ifIs<Activity> { return it }
                    contextWrapper = contextWrapper.baseContext
                }
                error("Unknown view context $contextWrapper")
            }
            else -> error("Unknown view context $context")
        }
    }
