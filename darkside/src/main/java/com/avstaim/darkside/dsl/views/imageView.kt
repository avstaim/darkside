@file:Suppress("unused")

package com.avstaim.darkside.dsl.views

import android.view.View
import android.widget.ImageView
import androidx.annotation.AttrRes
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.StyleRes
import com.avstaim.darkside.cookies.noGetter

inline fun ViewBuilder.imageView(
    @IdRes id: Int = View.NO_ID,
    @StyleRes themeRes: Int = NO_THEME,
    @AttrRes styleAttr: Int = NO_STYLE_ATTR,
    @StyleRes styleRes: Int = NO_STYLE_RES,
    init: ImageView.() -> Unit = {}
): ImageView = view(id, themeRes, styleAttr, styleRes, init)

var ImageView.drawableResource: Int
    get() = noGetter()
    set(@DrawableRes value) = setImageResource(value)
