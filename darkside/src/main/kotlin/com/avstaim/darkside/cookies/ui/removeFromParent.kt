@file:Suppress("unused")

package com.avstaim.darkside.cookies.ui

import android.view.View
import android.view.ViewGroup
import com.avstaim.darkside.dsl.views.Ui

fun View.removeFromParent() {
    val parent = this.parent as? ViewGroup
    parent?.removeView(this)
}

fun Ui<out View>.removeFromParent() = root.removeFromParent()
