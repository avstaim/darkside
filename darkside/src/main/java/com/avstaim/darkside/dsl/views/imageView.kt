@file:Suppress("unused")

package com.avstaim.darkside.dsl.views

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.annotation.AttrRes
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.StyleRes
import androidx.core.widget.ImageViewCompat
import com.avstaim.darkside.cookies.noGetter

inline fun ViewBuilder.imageView(
    @IdRes id: Int = View.NO_ID,
    @StyleRes themeRes: Int = NO_THEME,
    @AttrRes styleAttr: Int = NO_STYLE_ATTR,
    @StyleRes styleRes: Int = NO_STYLE_RES,
    init: ImageView.() -> Unit = {}
): ImageView = view(id, themeRes, styleAttr, styleRes, init)

inline var ImageView.drawableResource: Int
    get() = noGetter()
    set(@DrawableRes value) = setImageResource(value)


inline var ImageView.imageDrawable: Drawable?
    get() = drawable
    set(value) = setImageDrawable(value)

inline var ImageView.imageResource: Int
    get() = noGetter()
    set(@DrawableRes value) = setImageResource(value)

inline var ImageView.imageTintListCompat: ColorStateList
    get() = noGetter()
    set(value) = ImageViewCompat.setImageTintList(this, value)