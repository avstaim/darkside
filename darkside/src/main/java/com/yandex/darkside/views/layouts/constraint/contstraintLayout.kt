// Copyright (c) 2020 Yandex LLC. All rights reserved.
// Author: Alex Sher <avstaim@yandex-team.ru>

@file:Suppress("unused")

package com.yandex.darkside.views.layouts.constraint

import android.content.Context
import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.StyleRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.yandex.darkside.views.NO_THEME
import com.yandex.darkside.views.ViewBuilder
import com.yandex.darkside.views.ViewBuilderImpl
import com.yandex.darkside.views.view

class ConstraintLayoutBuilder(context: Context) : ConstraintLayout(context), ViewBuilder by ViewBuilderImpl(context) {

    init {
        attachTo(this)
    }

    private val constraintSetBuilder = ConstraintSetBuilder().also {
        it.clone(this)
        setConstraintSet(it)
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

    fun <T : View> T.constraints(init: AttachedConstraintBuilder.() -> Unit) =
        AttachedConstraintBuilder(this.id, constraintSetBuilder).apply(init)
}

inline fun ViewBuilder.constraintLayout(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    init: ConstraintLayoutBuilder.() -> Unit
): ConstraintLayout =
    view(
        factory = ::ConstraintLayoutBuilder,
        id = id,
        styleRes = theme,
        initView = init
    )
