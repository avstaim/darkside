package com.avstaim.darkside.dsl.views

import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes

class ViewAccessor internal constructor(private val parentViewProvider: () -> View) {

    @Suppress("UNCHECKED_CAST")
    operator fun <T: View> get(@IdRes id: Int): T {
        return when (val parentView = parentViewProvider.invoke()) {
            is ViewGroup -> parentView.findViewById(id) ?: throw IllegalArgumentException("View is not found")
            else -> if (parentView.id == id) {
                parentView as T
            } else {
                throw IllegalArgumentException("Not a viewgroup")
            }
        }
    }
}
