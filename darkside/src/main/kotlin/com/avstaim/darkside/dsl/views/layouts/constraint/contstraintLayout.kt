@file:Suppress("unused")

package com.avstaim.darkside.dsl.views.layouts.constraint

import android.content.Context
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.IdRes
import androidx.annotation.StyleRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.forEach
import com.avstaim.darkside.dsl.views.LayoutBuilder
import com.avstaim.darkside.dsl.views.NO_STYLE_ATTR
import com.avstaim.darkside.dsl.views.NO_STYLE_RES
import com.avstaim.darkside.dsl.views.NO_THEME
import com.avstaim.darkside.dsl.views.ViewBuilder
import com.avstaim.darkside.dsl.views.layoutBuilder
import com.avstaim.darkside.dsl.views.view
import com.avstaim.darkside.dsl.views.wrapContent

class ConstraintLayoutBuilder(context: Context, @AttrRes defStyleAttr: Int, @StyleRes defStyleRes: Int) :
    ConstraintLayout(context, null, defStyleAttr, defStyleRes),
    LayoutBuilder<ConstraintLayout.LayoutParams> by context.layoutBuilder(ConstraintLayout::LayoutParams) {

    constructor(context: Context) : this(context, 0, 0)

    init {
        attachTo(this)
    }

    private val constraintSetBuilder = ConstraintSetBuilder().also {
        ensureChildrenHaveIds()
        it.clone(this)
        setConstraintSet(it)
    }

    override val ctx: Context
        get() = context

    fun <T : View> T.constraints(init: AttachedConstraintBuilder.() -> Unit) =
        AttachedConstraintBuilder(this.id, constraintSetBuilder).apply(init)
}

internal fun ConstraintLayout.ensureChildrenHaveIds() {
    forEach { child ->
        if (child.id == -1) {
            child.id = generateViewIdCompat()
        }
    }
}

inline fun ViewBuilder.constraintLayout(
    @IdRes id: Int = View.NO_ID,
    @StyleRes themeRes: Int = NO_THEME,
    @AttrRes styleAttr: Int = NO_STYLE_ATTR,
    @StyleRes styleRes: Int = NO_STYLE_RES,
    init: ConstraintLayoutBuilder.() -> Unit
): ConstraintLayout =
    view(::ConstraintLayoutBuilder, id, themeRes, styleAttr, styleRes, init)

inline fun ViewBuilder.constraintLayoutOf(
    @IdRes id: Int = View.NO_ID,
    @StyleRes themeRes: Int = NO_THEME,
    @AttrRes styleAttr: Int = NO_STYLE_ATTR,
    @StyleRes styleRes: Int = NO_STYLE_RES,
    vararg views: View,
    init: ConstraintLayoutBuilder.() -> Unit,
): ConstraintLayout =
    view(::ConstraintLayoutBuilder, id, themeRes, styleAttr, styleRes, init).also { layout ->
        views.forEach { child ->
            layout.addView(child)
        }
    }

inline fun constraintLayoutParams(
    width: Int = wrapContent,
    height: Int = wrapContent,
    init: ConstraintLayout.LayoutParams.() -> Unit = {},
) = ConstraintLayout.LayoutParams(width, height).also(init)


inline fun ConstraintLayout.children(init: LayoutBuilder<ConstraintLayout.LayoutParams>.() -> Unit) {
    context.layoutBuilder(ConstraintLayout::LayoutParams).also {
        it.attachTo(viewManager = this)
        it.init()
    }
}
