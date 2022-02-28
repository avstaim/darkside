@file:Suppress("unused")

package com.avstaim.darkside.dsl.views.layouts

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.AttrRes
import androidx.annotation.IdRes
import androidx.annotation.StyleRes
import com.avstaim.darkside.cookies.noGetter
import com.avstaim.darkside.dsl.views.LayoutBuilder
import com.avstaim.darkside.dsl.views.NO_STYLE_ATTR
import com.avstaim.darkside.dsl.views.NO_STYLE_RES
import com.avstaim.darkside.dsl.views.NO_THEME
import com.avstaim.darkside.dsl.views.ViewBuilder
import com.avstaim.darkside.dsl.views.layoutBuilder
import com.avstaim.darkside.dsl.views.view
import com.avstaim.darkside.dsl.views.wrapContent
import com.avstaim.darkside.dsl.views.wrapCtxIfNeeded

class LinearLayoutBuilder(context: Context, @AttrRes defStyleAttr: Int, @StyleRes defStyleRes: Int) :
    LinearLayout(context, null, defStyleAttr), //FIXME: defStyleRes (fuck api16)
    LayoutBuilder<LinearLayout.LayoutParams> by context.layoutBuilder(LinearLayout::LayoutParams) {

    constructor(context: Context) : this(context, 0, 0)

    init {
        @Suppress("LeakingThis")
        attachTo(this)
    }

    override val ctx: Context
        get() = context
}

inline fun ViewBuilder.linearLayout(
    @IdRes id: Int = View.NO_ID,
    @StyleRes themeRes: Int = NO_THEME,
    @AttrRes styleAttr: Int = NO_STYLE_ATTR,
    @StyleRes styleRes: Int = NO_STYLE_RES,
    init: LinearLayoutBuilder.() -> Unit ={}
): LinearLayout = view(::LinearLayoutBuilder, id, themeRes, styleAttr, styleRes, init)

inline fun Activity.linearLayout(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    init: LinearLayoutBuilder.() -> Unit
): LinearLayout =
    LinearLayoutBuilder(this.wrapCtxIfNeeded(theme)).apply {
        this.id = id
        init()
    }

inline fun ViewBuilder.verticalLayout(
    @IdRes id: Int = View.NO_ID,
    @StyleRes themeRes: Int = NO_THEME,
    @AttrRes styleAttr: Int = NO_STYLE_ATTR,
    @StyleRes styleRes: Int = NO_STYLE_RES,
    init: LinearLayoutBuilder.() -> Unit = {}
): LinearLayout =
    view(::LinearLayoutBuilder, id, themeRes, styleAttr, styleRes) {
        orientation = LinearLayout.VERTICAL
        init()
    }

inline fun ViewBuilder.horizontalLayout(
    @IdRes id: Int = View.NO_ID,
    @StyleRes themeRes: Int = NO_THEME,
    @AttrRes styleAttr: Int = NO_STYLE_ATTR,
    @StyleRes styleRes: Int = NO_STYLE_RES,
    init: LinearLayoutBuilder.() -> Unit = {}
): LinearLayout =
    view(::LinearLayoutBuilder, id, themeRes, styleAttr, styleRes) {
        orientation = LinearLayout.HORIZONTAL
        init()
    }

var LinearLayout.horizontalGravity: Int
    get() = noGetter()
    set(v) = setHorizontalGravity(v)

var LinearLayout.verticalGravity: Int
    get() = noGetter()
    set(v) = setVerticalGravity(v)

inline fun linearLayoutParams(
    width: Int = wrapContent,
    height: Int = wrapContent,
    weight: Float = 0f,
    gravity: Int = -1,
    init: LinearLayout.LayoutParams.() -> Unit = {},
): LinearLayout.LayoutParams = LinearLayout.LayoutParams(width, height, weight).also {
    it.gravity = gravity
    it.init()
}


inline fun LinearLayout.children(init: LayoutBuilder<LinearLayout.LayoutParams>.() -> Unit) {
    context.layoutBuilder(LinearLayout::LayoutParams).also {
        it.attachTo(viewManager = this)
        it.init()
    }
}