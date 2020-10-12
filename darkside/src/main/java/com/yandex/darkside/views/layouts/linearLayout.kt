// Copyright (c) 2020 Yandex LLC. All rights reserved.
// Author: Alex Sher <avstaim@yandex-team.ru>

@file:Suppress("unused")

package com.yandex.darkside.views.layouts

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.IdRes
import androidx.annotation.StyleRes
import com.yandex.darkside.views.NO_THEME
import com.yandex.darkside.views.ViewBuilder
import com.yandex.darkside.views.ViewBuilderImpl
import com.yandex.darkside.views.view

open class LinearLayoutBuilder(context: Context) : LinearLayout(context), ViewBuilder by ViewBuilderImpl(context) {

    init {
        @Suppress("LeakingThis")
        attachTo(this)
    }

    override val ctx: Context
        get() = context

    override fun <T : View> T.lparams(width: Int, height: Int): T {
        layoutParams = LayoutParams(width, height)
        return this
    }

    fun <T : View> T.lparams(width: Int, height: Int, init: LayoutParams.() -> Unit): T {
        layoutParams = LayoutParams(width, height).also(init)
        return this
    }
}

inline fun ViewBuilder.linearLayout(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    init: LinearLayoutBuilder.() -> Unit ={}
): LinearLayout =
    view(
        factory = ::LinearLayoutBuilder,
        id = id,
        styleRes = theme,
        initView = init
    )


inline fun ViewBuilder.verticalLayout(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    init: LinearLayoutBuilder.() -> Unit = {}
): LinearLayout =
    view(::LinearLayoutBuilder, id, theme) {
        orientation = LinearLayout.VERTICAL
        init()
    }

inline fun ViewBuilder.horizontalLayout(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    init: LinearLayoutBuilder.() -> Unit = {}
): LinearLayout =
    view(::LinearLayoutBuilder, id, theme) {
        orientation = LinearLayout.HORIZONTAL
        init()
    }
