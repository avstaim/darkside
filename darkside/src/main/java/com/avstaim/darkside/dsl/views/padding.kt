@file:Suppress("unused")

package com.avstaim.darkside.dsl.views

import android.view.View
import androidx.annotation.Px
import com.avstaim.darkside.cookies.NO_GETTER
import com.avstaim.darkside.cookies.noGetter

inline var View.padding: Int
    @Deprecated(NO_GETTER, level = DeprecationLevel.HIDDEN) get() = noGetter()
    set(@Px value) = setPadding(value, value, value, value)

inline var View.horizontalPadding: Int
    @Deprecated(NO_GETTER, level = DeprecationLevel.HIDDEN) get() = noGetter()
    set(@Px value) = setPadding(value, paddingTop, value, paddingBottom)

inline var View.verticalPadding: Int
    @Deprecated(NO_GETTER, level = DeprecationLevel.HIDDEN) get() = noGetter()
    set(@Px value) = setPadding(paddingLeft, value, paddingRight, value)

inline var View.topPadding: Int
    get() = paddingTop
    set(@Px value) = setPadding(paddingLeft, value, paddingRight, paddingBottom)

inline var View.bottomPadding: Int
    get() = paddingBottom
    set(@Px value) = setPadding(paddingLeft, paddingTop, paddingRight, value)

inline var View.startPadding: Int
    get() = paddingStart
    set(@Px value) = setPaddingRelative(value, paddingTop, paddingEnd, paddingBottom)

inline var View.endPadding: Int
    get() = paddingEnd
    set(@Px value) = setPaddingRelative(paddingStart, paddingTop, value, paddingBottom)

inline var View.leftPadding: Int
    get() = paddingLeft
    set(@Px value) = setPadding(value, paddingTop, paddingRight, paddingBottom)

inline var View.rightPadding: Int
    get() = paddingRight
    set(@Px value) = setPadding(paddingLeft, paddingTop, value, paddingBottom)
