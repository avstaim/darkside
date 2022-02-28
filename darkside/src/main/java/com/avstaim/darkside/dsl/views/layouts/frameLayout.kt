@file:Suppress("unused")

package com.avstaim.darkside.dsl.views.layouts

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.AttrRes
import androidx.annotation.IdRes
import androidx.annotation.StyleRes
import com.avstaim.darkside.dsl.views.LayoutBuilder
import com.avstaim.darkside.dsl.views.NO_STYLE_ATTR
import com.avstaim.darkside.dsl.views.NO_STYLE_RES
import com.avstaim.darkside.dsl.views.NO_THEME
import com.avstaim.darkside.dsl.views.ViewBuilder
import com.avstaim.darkside.dsl.views.layoutBuilder
import com.avstaim.darkside.dsl.views.view
import com.avstaim.darkside.dsl.views.wrapContent
import com.avstaim.darkside.dsl.views.wrapCtxIfNeeded

class FrameLayoutBuilder(context: Context, @AttrRes defStyleAttr: Int, @StyleRes defStyleRes: Int) :
    FrameLayout(context, null, defStyleAttr), //FIXME: defStyleRes (fuck api16)
    LayoutBuilder<FrameLayout.LayoutParams> by context.layoutBuilder(FrameLayout::LayoutParams) {

    constructor(context: Context) : this(context, 0, 0)

    init {
        attachTo(this)
    }

    override val ctx: Context
        get() = context
}

class FrameLayoutBuilder16(context: Context, @AttrRes defStyleAttr: Int) :
    FrameLayout(context, null, defStyleAttr),
    LayoutBuilder<FrameLayout.LayoutParams> by context.layoutBuilder(FrameLayout::LayoutParams) {

    constructor(context: Context) : this(context, 0)

    init {
        attachTo(this)
    }

    override val ctx: Context
        get() = context
}


inline fun ViewBuilder.frameLayout(
    @IdRes id: Int = View.NO_ID,
    @StyleRes themeRes: Int = NO_THEME,
    @AttrRes styleAttr: Int = NO_STYLE_ATTR,
    @StyleRes styleRes: Int = NO_STYLE_RES,
    init: FrameLayoutBuilder.() -> Unit
): FrameLayout = view(::FrameLayoutBuilder, id, themeRes, styleAttr, styleRes, init)

inline fun Activity.frameLayout(
    @IdRes id: Int = View.NO_ID,
    @StyleRes themeRes: Int = NO_THEME,
    @AttrRes styleAttr: Int = NO_STYLE_ATTR,
    @StyleRes styleRes: Int = NO_STYLE_RES,
    init: FrameLayoutBuilder.() -> Unit
): FrameLayout =
    FrameLayoutBuilder(
        this.wrapCtxIfNeeded(themeRes),
        styleAttr,
        styleRes,
    ).apply {
        this.id = id
        init()
    }

inline fun frameLayoutParams(
    width: Int = wrapContent,
    height: Int = wrapContent,
    @SuppressLint("InlinedApi")
    gravity: Int = FrameLayout.LayoutParams.UNSPECIFIED_GRAVITY,
    init: FrameLayout.LayoutParams.() -> Unit = {}
): FrameLayout.LayoutParams {
    return FrameLayout.LayoutParams(width, height).also {
        it.gravity = gravity
    }.apply(init)
}

inline fun FrameLayout.children(init: LayoutBuilder<FrameLayout.LayoutParams>.() -> Unit) {
    context.layoutBuilder(FrameLayout::LayoutParams).also {
        it.attachTo(viewManager = this)
        it.init()
    }
}