@file:Suppress("NOTHING_TO_INLINE", "unused")

package com.avstaim.darkside.dsl.views

import android.app.Activity
import android.content.Context
import android.view.View
import androidx.annotation.LayoutRes
import splitties.views.inflate

interface Ui<V : View> {
    val ctx: Context
    val root: V
}

abstract class LayoutUi<V : View>(
    final override val ctx: Context
) : Ui<V>, ViewBuilder by ctx.viewBuilder() {

    override val root: V by lazy {
        layout()
    }

    abstract fun ViewBuilder.layout(): V
}

class DslUi<V : View>(ctx: Context, private val layoutBuilder: ViewBuilder.() -> V) : LayoutUi<V>(ctx) {
    override fun ViewBuilder.layout(): V = layoutBuilder()
}

inline fun <reified V : View> ui(
    ctx: Context,
    crossinline init: ViewBuilder.() -> V,
): Ui<V> = object : LayoutUi<V>(ctx) {
    override fun ViewBuilder.layout(): V = init()
}

inline fun Activity.setContentView(ui: Ui<out View>) = setContentView(ui.root)

abstract class XmlUi<V: View>(
    final override val ctx: Context,
    @LayoutRes resId: Int,
) : Ui<V> {

    override val root = ctx.inflate<V>(resId)
    protected val views = ViewAccessor { root }
}
