@file:Suppress("unused")

package com.yandex.darkside.views.extensions

import android.animation.AnimatorInflater
import android.app.Activity
import android.content.ContextWrapper
import android.os.Build
import android.view.View
import android.view.ViewGroup
import androidx.annotation.AnimatorRes
import androidx.annotation.DimenRes
import com.yandex.darkside.cookies.coroutineScope
import com.yandex.darkside.cookies.ifIs
import com.yandex.darkside.cookies.noGetter
import kotlinx.coroutines.launch

val View.activity: Activity
    get() {
        when (val context = context) {
            is Activity -> return context
            is ContextWrapper -> {
                var contextWrapper = context
                while (contextWrapper is ContextWrapper) {
                    contextWrapper.ifIs<Activity> { return it }
                    contextWrapper = contextWrapper.baseContext
                }
                error("Unknown view context $contextWrapper")
            }
            else -> error("Unknown view context $context")
        }
    }

var View.contentDescriptionResId: Int
    get() = noGetter()
    set(value) {
        contentDescription = resources.getString(value)
    }

fun View.onClick(value: suspend () -> Unit) {
    setOnClickListener {
        activity.coroutineScope.launch { value() }
    }
}

var View.leftPadding: Int
    get() = paddingLeft
    set(value) = setPadding(value, paddingTop, paddingRight, paddingBottom)

var View.rightPadding: Int
    get() = paddingRight
    set(value) = setPadding(paddingLeft, paddingTop, value, paddingBottom)

var View.padding: Int
    get() = noGetter()
    set(value) = setPadding(value, value, value, value)

var View.verticalPadding: Int
    get() = noGetter()
    set(value) = setPadding(paddingLeft, value, paddingRight, value)

var View.horizontalPadding: Int
    get() = noGetter()
    set(value) = setPadding(value, paddingTop, value, paddingBottom)

var View.stateListAnimatorResource: Int
    get() = noGetter()
    set(@AnimatorRes value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            stateListAnimator = AnimatorInflater.loadStateListAnimator(context, value)
        }
    }

var View.topMargin: Int
    get() = noGetter()
    set(value) {
        withMarginLayoutParams {
            topMargin = value
        }
    }

var View.leftMargin: Int
    get() = noGetter()
    set(value) {
        withMarginLayoutParams {
            leftMargin = value
        }
    }

var View.bottomMargin: Int
    get() = noGetter()
    set(value) {
        withMarginLayoutParams {
            bottomMargin = value
        }
    }

var View.rightMargin: Int
    get() = noGetter()
    set(value) {
        withMarginLayoutParams {
            rightMargin = value
        }
    }

var View.topMarginRes: Int
    get() = noGetter()
    set(@DimenRes value) {
        withMarginLayoutParams {
            topMargin = context.resources.getDimensionPixelSize(value)
        }
    }

var View.leftMarginRes: Int
    get() = noGetter()
    set(@DimenRes value) {
        withMarginLayoutParams {
            leftMargin = context.resources.getDimensionPixelSize(value)
        }
    }

var View.bottomMarginRes: Int
    get() = noGetter()
    set(@DimenRes value) {
        withMarginLayoutParams {
            bottomMargin = context.resources.getDimensionPixelSize(value)
        }
    }

var View.rightMarginRes: Int
    get() = noGetter()
    set(@DimenRes value) {
        withMarginLayoutParams {
            rightMargin = context.resources.getDimensionPixelSize(value)
        }
    }

inline fun View.withMarginLayoutParams(block: ViewGroup.MarginLayoutParams.() -> Unit) = withLayoutParams(block)

inline fun <reified T : ViewGroup.LayoutParams> View.withLayoutParams(block: T.() -> Unit) {
    (layoutParams as? T)?.let { lp ->
        lp.block()
        layoutParams = lp
    }
}
