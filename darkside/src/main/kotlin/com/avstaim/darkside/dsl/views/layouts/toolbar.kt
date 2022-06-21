@file:Suppress("NOTHING_TO_INLINE", "unused")

package com.avstaim.darkside.dsl.views.layouts

import android.app.Activity
import android.content.Context
import android.view.Gravity
import android.view.Menu
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.Toolbar
import com.avstaim.darkside.cookies.noGetter
import com.avstaim.darkside.dsl.views.LayoutBuilder
import com.avstaim.darkside.dsl.views.NO_STYLE_ATTR
import com.avstaim.darkside.dsl.views.NO_STYLE_RES
import com.avstaim.darkside.dsl.views.NO_THEME
import com.avstaim.darkside.dsl.views.ViewBuilder
import com.avstaim.darkside.dsl.views.layoutBuilder
import com.avstaim.darkside.dsl.views.view
import com.avstaim.darkside.dsl.views.wrapContent

class ToolbarBuilder(context: Context, @AttrRes defStyleAttr: Int, @StyleRes defStyleRes: Int) :
    Toolbar(context, null, defStyleAttr),
    LayoutBuilder<Toolbar.LayoutParams> by context.layoutBuilder(Toolbar::LayoutParams) {

    constructor(context: Context) : this(context, 0, 0)

    init {
        attachTo(this)
    }

    override val ctx: Context
        get() = context
}

inline fun ViewBuilder.toolbar(
    @IdRes id: Int = View.NO_ID,
    @StyleRes themeRes: Int = NO_THEME,
    @AttrRes styleAttr: Int = NO_STYLE_ATTR,
    @StyleRes styleRes: Int = NO_STYLE_RES,
    init: ToolbarBuilder.() -> Unit = {},
): Toolbar = view(::ToolbarBuilder, id, themeRes, styleAttr, styleRes, init)

inline fun Toolbar.menu(init: Menu.() -> Unit) = menu.apply(init)

inline var Toolbar.contentInset: Int
    get() = noGetter()
    set(value) = setContentInsetsAbsolute(value, value)

inline var Toolbar.startContentInset: Int
    get() = contentInsetStart
    set(value) = setContentInsetsRelative(value, contentInsetEnd)

inline var Toolbar.endContentInset: Int
    get() = contentInsetEnd
    set(value) = setContentInsetsRelative(contentInsetStart, value)

inline var Toolbar.leftContentInset: Int
    get() = contentInsetLeft
    set(value) = setContentInsetsAbsolute(value, contentInsetRight)

inline var Toolbar.rightContentInset: Int
    get() = contentInsetRight
    set(value) = setContentInsetsAbsolute(contentInsetLeft, value)

@Suppress("NOTHING_TO_INLINE")
inline fun Toolbar.attachTo(activity: Activity) {
    val appCompatActivity = activity as? AppCompatActivity
    appCompatActivity?.setSupportActionBar(this)
}

inline fun toolbarLayoutParams(
    width: Int = wrapContent,
    height: Int = wrapContent,
    gravity: Int = Gravity.NO_GRAVITY,
    init: Toolbar.LayoutParams.() -> Unit = {},
) = Toolbar.LayoutParams(width, height, gravity).also(init)

inline var Toolbar.logoResource: Int
    get() = noGetter()
    set(@DrawableRes value) {
        logo = AppCompatResources.getDrawable(context, value)
    }

inline fun Toolbar.children(init: LayoutBuilder<Toolbar.LayoutParams>.() -> Unit) {
    context.layoutBuilder(Toolbar::LayoutParams).also {
        it.attachTo(viewManager = this)
        it.init()
    }
}
