@file:Suppress("NOTHING_TO_INLINE", "unused")

package com.avstaim.darkside.dsl.views

import android.content.Context
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.annotation.StyleRes
import androidx.appcompat.view.ContextThemeWrapper
import splitties.views.inflate

const val NO_THEME = 0
const val NO_STYLE_RES = 0
const val NO_STYLE_ATTR = 0

@Suppress("NOTHING_TO_INLINE")
inline fun Context.withTheme(theme: Int) = ContextThemeWrapper(this, theme)

fun Context.wrapCtxIfNeeded(theme: Int): Context {
    return if (theme == NO_THEME) this else withTheme(theme)
}

inline fun <V : View> ViewBuilder.view(
    factory: ViewFactory<V>,
    @IdRes id: Int = View.NO_ID,
    @StyleRes themeRes: Int = NO_THEME,
    @AttrRes styleAttr: Int = NO_STYLE_ATTR,
    @StyleRes styleRes: Int = NO_STYLE_RES,
    initView: V.() -> Unit = {}
): V {
    val viewBuilder: ViewBuilder = this
    return factory(
        ctx.wrapCtxIfNeeded(themeRes),
        styleAttr,
        styleRes,
    ).apply {
        if (id != View.NO_ID) {
            this.id = id
        }
        if (viewBuilder is AddingViewBuilder) {
            with(viewBuilder) {
                addToParent()
            }
        }
        initView()
    }
}

inline fun <reified V : View> ViewBuilder.view(
    @IdRes id: Int = View.NO_ID,
    @StyleRes themeRes: Int = NO_THEME,
    @AttrRes styleAttr: Int = NO_STYLE_ATTR,
    @StyleRes styleRes: Int = NO_STYLE_RES,
    initView: V.() -> Unit = {}
): V = view(factoryFor(), id, themeRes, styleAttr, styleRes, initView)

inline fun <reified V : View, reified U : Ui<V>> ViewBuilder.include(
    @IdRes id: Int = View.NO_ID,
    ui: U,
    initUi: U.() -> Unit = {}
) = view(id = id, factory = simpleFactory { ui.root }).also { ui.initUi() }

inline fun <reified V : View> ViewBuilder.include(
    @IdRes id: Int = View.NO_ID,
    view: V,
    initView: V.() -> Unit = {}
) = view(id = id, factory = simpleFactory { view }, initView = initView)

inline fun <reified V : View> ViewBuilder.include(
    @IdRes id: Int = View.NO_ID,
    crossinline uiFactory: (Context) -> Ui<V>,
) = view(id = id, factory = simpleFactory { uiFactory(ctx).root })

inline fun <reified V : View> ViewBuilder.xmlLayout(
    @LayoutRes xmlLayoutRes: Int,
    @IdRes id: Int = View.NO_ID,
    @StyleRes themeRes: Int = NO_THEME,
    init: V.() -> Unit = {}
): V = view<V>(id = id, themeRes = themeRes, factory = simpleFactory { ctx -> ctx.inflate(xmlLayoutRes) }).apply(init)
