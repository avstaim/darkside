@file:Suppress("unused", "UNUSED_PARAMETER")

package com.avstaim.darkside.dsl.views.layouts

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.LinearLayout
import android.widget.RadioGroup
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

class RadioGroupBuilder(context: Context, @AttrRes defStyleAttr: Int, @StyleRes defStyleRes: Int) :
    RadioGroup(context, null),
    LayoutBuilder<LinearLayout.LayoutParams> by context.layoutBuilder(LinearLayout::LayoutParams) {

    constructor(context: Context) : this(context, 0, 0)

    init {
        @Suppress("LeakingThis")
        attachTo(this)
    }

    override val ctx: Context
        get() = context
}

inline fun ViewBuilder.radioGroup(
    @IdRes id: Int = View.NO_ID,
    @StyleRes themeRes: Int = NO_THEME,
    @AttrRes styleAttr: Int = NO_STYLE_ATTR,
    @StyleRes styleRes: Int = NO_STYLE_RES,
    init: RadioGroupBuilder.() -> Unit ={}
): RadioGroup = view(::RadioGroupBuilder, id, themeRes, styleAttr, styleRes, init)

inline fun Activity.radioGroup(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    init: RadioGroupBuilder.() -> Unit
): LinearLayout =
    RadioGroupBuilder(this.wrapCtxIfNeeded(theme)).apply {
        this.id = id
        init()
    }

inline fun radioLayoutParams(
    width: Int = wrapContent,
    height: Int = wrapContent,
    weight: Float = 0f,
    gravity: Int = -1,
    init: RadioGroup.LayoutParams.() -> Unit = {},
): RadioGroup.LayoutParams = RadioGroup.LayoutParams(width, height, weight).also {
    it.gravity = gravity
    it.init()
}
