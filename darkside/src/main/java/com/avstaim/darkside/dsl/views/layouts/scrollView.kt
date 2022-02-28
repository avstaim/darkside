@file:Suppress("unused")

package com.avstaim.darkside.dsl.views.layouts

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.FrameLayout
import android.widget.ScrollView
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
import com.avstaim.darkside.dsl.views.wrapCtxIfNeeded

class ScrollViewBuilder(context: Context, @AttrRes defStyleAttr: Int, @StyleRes defStyleRes: Int) :
    ScrollView(context, null, defStyleAttr), //FIXME: defStyleRes (fuck api16)
    LayoutBuilder<FrameLayout.LayoutParams> by context.layoutBuilder(FrameLayout::LayoutParams) {

    constructor(context: Context) : this(context, 0, 0)

    init {
        attachTo(this)
    }

    override val ctx: Context
        get() = context
}

inline fun ViewBuilder.scrollView(
    @IdRes id: Int = View.NO_ID,
    @StyleRes themeRes: Int = NO_THEME,
    @AttrRes styleAttr: Int = NO_STYLE_ATTR,
    @StyleRes styleRes: Int = NO_STYLE_RES,
    init: ScrollViewBuilder.() -> Unit
): ScrollView = view(::ScrollViewBuilder, id, themeRes, styleAttr, styleRes, init)

inline fun Activity.scrollView(
    @IdRes id: Int = View.NO_ID,
    @StyleRes themeRes: Int = NO_THEME,
    @AttrRes styleAttr: Int = NO_STYLE_ATTR,
    @StyleRes styleRes: Int = NO_STYLE_RES,
    init: ScrollViewBuilder.() -> Unit
): ScrollView =
    ScrollViewBuilder(
        this.wrapCtxIfNeeded(themeRes),
        styleAttr,
        styleRes,
    ).apply {
        this.id = id
        init()
    }
