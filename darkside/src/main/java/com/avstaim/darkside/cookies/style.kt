package com.avstaim.darkside.cookies

import android.view.View

fun interface Style<V : View> {
    fun apply(view: V)
}

inline var <reified V: View> V.style: Style<in V>
    get() = noGetter()
    set(value) = value.apply(this)

inline fun <reified V: View> style(crossinline block: V.() -> Unit): Style<V> =
    Style { view -> block(view) }

inline fun <reified V: View> style(parent: Style<in V>, crossinline block: V.() -> Unit): Style<V> =
    Style { view ->
        parent.apply(view)
        block(view)
    }
