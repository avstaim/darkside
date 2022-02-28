@file:Suppress("unused")

package com.avstaim.darkside.dsl.views.layouts.constraint

import android.content.Context
import androidx.constraintlayout.widget.ConstraintLayout
import com.avstaim.darkside.dsl.views.LayoutBuilder
import com.avstaim.darkside.dsl.views.Ui

abstract class ConstraintLayoutUi private constructor(
    final override val ctx: Context,
    private val layoutBuilder: ConstraintLayoutBuilder,
) : Ui<ConstraintLayout>, LayoutBuilder<ConstraintLayout.LayoutParams> by layoutBuilder {

    constructor(ctx: Context) : this(ctx, ConstraintLayoutBuilder(ctx))

    override val root: ConstraintLayout by lazy {
        layoutBuilder.also {
            it.applyConstraints {
                constraints()
            }
        }
    }

    protected fun generateId() = generateViewIdCompat()

    abstract fun ConstraintSetBuilder.constraints()
}
