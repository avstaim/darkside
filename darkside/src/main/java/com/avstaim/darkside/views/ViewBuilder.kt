// Copyright (c) 2020 Yandex LLC. All rights reserved.
// Author: Alex Sher <avstaim@yandex-team.ru>

package com.avstaim.darkside.views

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.ViewManager

interface ViewBuilder {

    val ctx: Context
    fun attachTo(viewManager: ViewManager)
    fun <T : View> T.lparams(width: Int = wrapContent, height: Int = wrapContent): T

    fun View.addToParent()
}

class ViewBuilderImpl(override val ctx: Context) : ViewBuilder {

    private var viewManager: ViewManager? = null

    override fun <T : View> T.lparams(width: Int, height: Int): T {
        layoutParams = ViewGroup.LayoutParams(width, height)
        return this
    }

    override fun attachTo(viewManager: ViewManager) {
        this.viewManager = viewManager
    }

    override fun View.addToParent() {
        when (val manager = viewManager) {
            is ViewGroup -> manager.addView(this)
            is Activity -> manager.addView(this, null)
            null -> throw IllegalStateException("viewManager is not attached")
            else -> throw IllegalStateException("$manager is the wrong parent")
        }
    }
}

internal interface SimplifiedViewBuilder : ViewBuilder {

    override fun attachTo(viewManager: ViewManager) = Unit

    override fun <T : View> T.lparams(width: Int, height: Int): T {
        layoutParams = ViewGroup.LayoutParams(width, height)
        return this
    }
}
