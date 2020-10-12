@file:Suppress("unused")

package com.yandex.darkside.views.extensions

import android.os.Build
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.yandex.darkside.cookies.noGetter

var TextView.textColor: Int
    get() = textColors.defaultColor
    set(value) = setTextColor(value)

var TextView.textColorResource: Int
    get() = noGetter()
    set(colorId) = setTextColor(context.resources.run {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) getColor(colorId, null) else getColor(colorId)
    })

var TextView.fontResource: Int
    get() = noGetter()
    set(fontId) {
        typeface = ResourcesCompat.getFont(context, fontId)
    }

var TextView.lineSpacingAdd: Float
    get() = lineSpacingExtra
    set(value) = setLineSpacing(value, lineSpacingMultiplier)

var TextView.lineSpacingMult: Float
    get() = lineSpacingMultiplier
    set(value) = setLineSpacing(lineSpacingExtra, value)

var TextView.textResource: Int
    get() = noGetter()
    set(v) = setText(v)
