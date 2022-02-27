
@file:Suppress("NOTHING_TO_INLINE", "unused")

package com.avstaim.darkside.dsl.resource

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources

inline fun Context.drawableForResource(@DrawableRes drawableRes: Int): Drawable? =
    AppCompatResources.getDrawable(this, drawableRes)

inline fun View.drawableForResource(@DrawableRes drawableRes: Int): Drawable? =
    context.drawableForResource(drawableRes)

inline fun stateListDrawable(init: StateListDrawable.() -> Unit) = StateListDrawable().also(init)
inline fun StateListDrawable.state(@AttrRes stateRes: Int, drawable: Drawable?) =
    addState(intArrayOf(stateRes), drawable)
