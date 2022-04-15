/*
 * Copyright 2019-2021 Louis Cognault Ayeva Derman. Use of this source code is governed by the Apache 2.0 license.
 */

@file:Suppress("NOTHING_TO_INLINE", "unused")

package com.avstaim.darkside.dsl.views

import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.util.TypedValue
import android.view.View
import androidx.annotation.ArrayRes
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.avstaim.darkside.cookies.illegalArg
import com.avstaim.darkside.cookies.isMainThread

inline fun Context.str(@StringRes stringResId: Int): String = resources.getString(stringResId)
inline fun Fragment.str(@StringRes stringResId: Int) = context!!.str(stringResId)
inline fun View.str(@StringRes stringResId: Int) = context.str(stringResId)

inline fun Context.str(
    @StringRes stringResId: Int,
    vararg formatArgs: Any?
): String = resources.getString(stringResId, *formatArgs)

inline fun Fragment.str(
    @StringRes stringResId: Int,
    vararg formatArgs: Any?
) = context!!.str(stringResId, *formatArgs)

inline fun View.str(
    @StringRes stringResId: Int,
    vararg formatArgs: Any?
) = context.str(stringResId, *formatArgs)


inline fun Context.strArray(
    @ArrayRes stringResId: Int
): Array<String> = resources.getStringArray(stringResId)

inline fun Fragment.strArray(@ArrayRes stringResId: Int) = context!!.strArray(stringResId)
inline fun View.strArray(@ArrayRes stringResId: Int) = context.strArray(stringResId)

/**
 * @see [androidx.core.content.ContextCompat.getColor]
 */
@ColorInt
fun Context.color(@ColorRes colorRes: Int): Int = if (Build.VERSION.SDK_INT >= 23) getColor(colorRes) else {
    @Suppress("DEPRECATION")
    resources.getColor(colorRes)
}

inline fun Fragment.color(@ColorRes colorRes: Int) = context!!.color(colorRes)
inline fun View.color(@ColorRes colorRes: Int) = context.color(colorRes)

@ColorInt
fun Context.styledColor(@AttrRes attr: Int): Int = withResolvedThemeAttribute(attr) {
    when  {
        type in TypedValue.TYPE_FIRST_COLOR_INT..TypedValue.TYPE_LAST_COLOR_INT -> data
        type == TypedValue.TYPE_STRING && string.startsWith("res/color/") -> color(resourceId)
        else -> illegalArg(unexpectedThemeAttributeTypeErrorMessage(expectedKind = "color"))
    }
}

inline fun Fragment.styledColor(@AttrRes attr: Int) = context!!.styledColor(attr)
inline fun View.styledColor(@AttrRes attr: Int) = context.styledColor(attr)

inline fun <R> Context.withResolvedThemeAttribute(
    @AttrRes attrRes: Int,
    resolveRefs: Boolean = true,
    crossinline block: TypedValue.() -> R
): R = if (isMainThread) {
    if (theme.resolveAttribute(attrRes, uiThreadConfinedCachedTypedValue, resolveRefs).not()) {
        throw Resources.NotFoundException(
            "Couldn't resolve attribute resource #0x" + Integer.toHexString(attrRes)
                    + " from the theme of this Context."
        )
    }
    block(uiThreadConfinedCachedTypedValue)
} else synchronized(cachedTypeValue) {
    if (theme.resolveAttribute(attrRes, cachedTypeValue, resolveRefs).not()) {
        throw Resources.NotFoundException(
            "Couldn't resolve attribute resource #0x" + Integer.toHexString(attrRes)
                    + " from the theme of this Context."
        )
    }
    block(cachedTypeValue)
}

@PublishedApi @JvmField internal val uiThreadConfinedCachedTypedValue = TypedValue()
@PublishedApi @JvmField internal val cachedTypeValue = TypedValue()

internal fun TypedValue.unexpectedThemeAttributeTypeErrorMessage(expectedKind: String): String {
    val article = when (expectedKind.firstOrNull() ?: ' ') {
        in "aeio" -> "an"
        else -> "a"
    }
    return "Expected $article $expectedKind theme attribute but got type 0x${type.toString(16)} " +
            "(see what it corresponds to in android.util.TypedValue constants)"
}