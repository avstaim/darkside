@file:Suppress("unused", "UNUSED_PARAMETER")

package com.avstaim.darkside.dsl.views.layouts

import android.app.Activity
import android.content.Context
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.IdRes
import androidx.annotation.StyleRes
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.avstaim.darkside.dsl.views.LayoutBuilder
import com.avstaim.darkside.dsl.views.NO_STYLE_ATTR
import com.avstaim.darkside.dsl.views.NO_STYLE_RES
import com.avstaim.darkside.dsl.views.NO_THEME
import com.avstaim.darkside.dsl.views.ViewBuilder
import com.avstaim.darkside.dsl.views.layoutBuilder
import com.avstaim.darkside.dsl.views.resolveDefaultStyleAttrForView
import com.avstaim.darkside.dsl.views.view
import com.avstaim.darkside.dsl.views.wrapContent
import com.avstaim.darkside.dsl.views.wrapCtxIfNeeded

class CoordinatorLayoutBuilder(context: Context, @AttrRes defStyleAttr: Int, @StyleRes defStyleRes: Int) :
    CoordinatorLayout(context, null, defStyleAttr),
    LayoutBuilder<CoordinatorLayout.LayoutParams> by context.layoutBuilder(CoordinatorLayout::LayoutParams) {

    constructor(context: Context) : this(context, resolveDefaultStyleAttrForView<CoordinatorLayout>(), 0)

    init {
        attachTo(this)
    }

    override val ctx: Context
        get() = context
}

// Styling [CoordinatorLayout] is not supported at the moment as it lacks (Context, Attrs, Int, Int) constructor.
inline fun ViewBuilder.coordinatorLayout(
    @IdRes id: Int = View.NO_ID,
    @StyleRes themeRes: Int = NO_THEME,
    @AttrRes styleAttr: Int = NO_STYLE_ATTR,
    init: CoordinatorLayoutBuilder.() -> Unit
): CoordinatorLayout = view(::CoordinatorLayoutBuilder, id, themeRes, styleAttr, NO_STYLE_RES, init)

inline fun Activity.coordinatorLayout(
    @IdRes id: Int = View.NO_ID,
    @StyleRes themeRes: Int = NO_THEME,
    @AttrRes styleAttr: Int = NO_STYLE_ATTR,
    init: CoordinatorLayoutBuilder.() -> Unit
): CoordinatorLayout =
    CoordinatorLayoutBuilder(this.wrapCtxIfNeeded(themeRes), styleAttr, NO_STYLE_RES).apply {
        this.id = id
        init()
    }

inline fun <V : View> coordinatorLayoutParams(
    width: Int = wrapContent,
    height: Int = wrapContent,
    behavior: CoordinatorLayout.Behavior<V>? = null,
    initParams: CoordinatorLayout.LayoutParams.() -> Unit = {}
): CoordinatorLayout.LayoutParams {
    return CoordinatorLayout.LayoutParams(width, height).also { layoutParams ->
        behavior?.let { layoutParams.behavior = it }
    }.apply(initParams)
}

inline fun CoordinatorLayout.children(init: LayoutBuilder<CoordinatorLayout.LayoutParams>.() -> Unit) {
    context.layoutBuilder(CoordinatorLayout::LayoutParams).also {
        it.attachTo(viewManager = this)
        it.init()
    }
}
