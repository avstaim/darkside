@file:Suppress("unused")

package com.avstaim.darkside.cookies.delegates

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

fun <T> doubleCheck(init: () -> T): ReadOnlyProperty<Any?, T> = DoubleCheck(init)

private class DoubleCheck<T>(private val init: () -> T): ReadOnlyProperty<Any?, T> {

    private var value: T? = null

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return value ?: synchronized(this) {
            value ?: init().also { value = it }
        }
    }
}
