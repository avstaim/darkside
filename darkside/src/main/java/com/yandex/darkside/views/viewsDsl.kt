// Copyright (c) 2020 Yandex LLC. All rights reserved.
// Author: Alex Sher <avstaim@yandex-team.ru>

@file:Suppress("unused")

package com.yandex.darkside.views

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.StyleRes
import androidx.appcompat.view.ContextThemeWrapper
import androidx.constraintlayout.widget.ConstraintLayout

typealias ViewFactory<V> = (Context) -> V

const val NO_THEME = 0

const val matchParent: Int = ViewGroup.LayoutParams.MATCH_PARENT
const val wrapContent: Int = ViewGroup.LayoutParams.WRAP_CONTENT
const val matchConstraint: Int = ConstraintLayout.LayoutParams.MATCH_CONSTRAINT

@Suppress("NOTHING_TO_INLINE")
inline fun Context.withTheme(theme: Int) = ContextThemeWrapper(this, theme)

fun Context.wrapCtxIfNeeded(theme: Int): Context {
    return if (theme == NO_THEME) this else withTheme(theme)
}

inline fun <T : View> ViewBuilder.view(
    factory: ViewFactory<T>,
    @IdRes id: Int = View.NO_ID,
    @StyleRes styleRes: Int = NO_THEME,
    initView: T.() -> Unit = {}
): T {
    return factory(ctx.wrapCtxIfNeeded(styleRes)).apply {
        this.id = id
        initView()
        addToParent()
    }
}
