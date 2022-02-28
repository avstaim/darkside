@file:Suppress("NOTHING_TO_INLINE", "unused")

package com.avstaim.darkside.dsl.views.layouts

import android.os.Build
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Px
import com.avstaim.darkside.cookies.ifIs
import com.avstaim.darkside.cookies.noGetter
import com.avstaim.darkside.dsl.views.InternalApi
import com.avstaim.darkside.dsl.views.LayoutBuilder
import com.avstaim.darkside.dsl.views.wrapContent
import kotlin.reflect.KClass

inline fun View.simpleLayoutParams(
    @Px width: Int,
    @Px height: Int,
    init: ViewGroup.LayoutParams.() -> Unit = {},
): ViewGroup.LayoutParams {
    layoutParams?.let { lp ->
        lp.init()
        return lp
    }

    return ViewGroup.LayoutParams(width, height).also(init)
}

inline fun View.marginLayoutParams(
    @Px width: Int = wrapContent,
    @Px height: Int = wrapContent,
    init: ViewGroup.MarginLayoutParams.() -> Unit = {},
): ViewGroup.MarginLayoutParams {
    layoutParams.ifIs<ViewGroup.MarginLayoutParams> { lp ->
        lp.init()
        return lp
    }

    return ViewGroup.MarginLayoutParams(width, height).also(init)
}

inline fun <reified LP : ViewGroup.LayoutParams> LayoutBuilder<LP>.layoutParams(
    @Px width: Int = wrapContent,
    @Px height: Int = wrapContent,
    init: LP.() -> Unit = {},
): LP = generateLayoutParams(width, height).also(init)

inline fun <reified LP : ViewGroup.LayoutParams> LayoutBuilder<LP>.layout(init: LayoutParamsBuilder<LP>.() -> Unit) {
    LayoutParamsBuilderImpl(layoutBuilder = this, klass = LP::class).also(init)
}

var ViewGroup.MarginLayoutParams.margin: Int
    get() = noGetter()
    set(value) = setMargins(value, value, value, value)

var ViewGroup.MarginLayoutParams.horizontalMargin: Int
    get() = noGetter()
    set(value) {
        leftMargin = value
        rightMargin = value
    }

var ViewGroup.MarginLayoutParams.verticalMargin: Int
    get() = noGetter()
    set(value) {
        topMargin = value
        bottomMargin = value
    }

var ViewGroup.MarginLayoutParams.startMargin: Int
    get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
        marginStart
    } else {
        leftMargin
    }
    set(value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            marginStart = value
        } else {
            leftMargin = value
        }
    }

var ViewGroup.MarginLayoutParams.endMargin: Int
    get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
        marginEnd
    } else {
        rightMargin
    }
    set(value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            marginEnd = value
        } else {
            rightMargin = value
        }
    }

interface LayoutParamsBuilder<LP : ViewGroup.LayoutParams> {
    operator fun View.invoke(width: Int = wrapContent, height: Int = wrapContent, init: LP.() -> Unit)
}

@InternalApi
class LayoutParamsBuilderImpl<LP : ViewGroup.LayoutParams>(
    val layoutBuilder: LayoutBuilder<LP>,
    val klass: KClass<LP>,
): LayoutParamsBuilder<LP> {

    override fun View.invoke(width: Int, height: Int, init: LP.() -> Unit) {
        val lp = layoutParams
        layoutParams = if (lp != null && klass.isInstance(lp)) {
            @Suppress("UNCHECKED_CAST")
            (lp as LP).also {
                it.width = width
                it.height = height
                it.init()
            }
        } else {
            layoutBuilder.generateLayoutParams(width, height).also(init)
        }
    }
}
