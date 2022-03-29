@file:Suppress("unused")

package com.avstaim.darkside.cookies.delegates

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

open class StateHolder<T>(initial: T) {

    private val mutableFlow: MutableStateFlow<T> = MutableStateFlow(initial)
    val stateFlow: StateFlow<T> get() = mutableFlow

    fun delegate(
        listener: (old: T, new: T) -> Unit = { _, _ -> },
    ): ReadWriteProperty<Any, T> = Delegate(mutableFlow, listener)

    private class Delegate<T>(
        private val flow: MutableStateFlow<T>,
        private val listener: (old: T, new: T) -> Unit,
    ) : ReadWriteProperty<Any, T> {

        override fun getValue(thisRef: Any, property: KProperty<*>) = flow.value

        override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
            if (value == flow.value) return
            listener.invoke(flow.value, value)
            flow.value = value
        }
    }
}
