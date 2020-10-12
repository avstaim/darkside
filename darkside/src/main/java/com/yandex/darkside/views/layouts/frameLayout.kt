// Copyright (c) 2020 Yandex LLC. All rights reserved.
// Author: Alex Sher <avstaim@yandex-team.ru>

@file:Suppress("unused")

package com.yandex.darkside.views.layouts

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.IdRes
import androidx.annotation.StyleRes
import com.yandex.darkside.views.NO_THEME
import com.yandex.darkside.views.ViewBuilder
import com.yandex.darkside.views.ViewBuilderImpl
import com.yandex.darkside.views.view
import com.yandex.darkside.views.wrapContent
import com.yandex.darkside.views.wrapCtxIfNeeded

class FrameLayoutBuilder(context: Context) : FrameLayout(context), ViewBuilder by ViewBuilderImpl(context) {

    init {
        attachTo(this)
    }

    override val ctx: Context
        get() = context

    override fun <T : View> T.lparams(width: Int, height: Int): T {
        layoutParams = LayoutParams(width, height)
        return this
    }

    fun <T : View> T.lparams(width: Int = wrapContent, height: Int = wrapContent, gravity: Int): T {
        layoutParams = LayoutParams(width, height, gravity)
        return this
    }
}

inline fun ViewBuilder.frameLayout(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    init: FrameLayoutBuilder.() -> Unit
): FrameLayout = view(
    factory = ::FrameLayoutBuilder,
    id = id,
    styleRes = theme,
    initView = init
)

inline fun Activity.frameLayout(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    init: FrameLayoutBuilder.() -> Unit
): FrameLayout =
    FrameLayoutBuilder(this.wrapCtxIfNeeded(theme)).apply {
        this.id = id
        init()
    }

inline fun FrameLayout.lParams(
    width: Int = wrapContent,
    height: Int = wrapContent,
    @SuppressLint("InlinedApi")
    gravity: Int = FrameLayout.LayoutParams.UNSPECIFIED_GRAVITY,
    initParams: FrameLayout.LayoutParams.() -> Unit = {}
): FrameLayout.LayoutParams {
    return FrameLayout.LayoutParams(width, height).also {
        it.gravity = gravity
    }.apply(initParams)
}