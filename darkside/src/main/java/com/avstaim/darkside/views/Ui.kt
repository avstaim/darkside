// Copyright (c) 2020 Yandex LLC. All rights reserved.
// Author: Alex Sher <avstaim@yandex-team.ru>

@file:Suppress("NOTHING_TO_INLINE", "unused")

package com.avstaim.darkside.views

import android.app.Activity
import android.content.Context
import android.view.View

interface Ui {
    val ctx: Context
    val root: View
}

@Suppress("LeakingThis", "REDUNDANT_MODIFIER")
open abstract class LayoutUi(
    override val ctx: Context
) : Ui {

    override lateinit var root: View

    init {
        UiBuilder().layout()
    }

    abstract fun ViewBuilder.layout()

    private inner class UiBuilder : SimplifiedViewBuilder {

        override val ctx: Context
            get() = this@LayoutUi.ctx

        override fun View.addToParent() {
            root = this
        }
    }
}

class DslUi(ctx: Context, private val init: ViewBuilder.() -> Unit) : LayoutUi(ctx) {
    override fun ViewBuilder.layout() = init()
}

inline fun ui(
    ctx: Context,
    noinline init: ViewBuilder.() -> Unit,
): Ui = DslUi(ctx, init)

inline fun Activity.setContentView(ui: Ui) = setContentView(ui.root)
