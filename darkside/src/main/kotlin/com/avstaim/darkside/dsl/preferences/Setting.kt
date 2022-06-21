@file:Suppress("unused")

package com.avstaim.darkside.dsl.preferences

import kotlin.reflect.KMutableProperty0

class Setting<T>(val getter: () -> T, val setter: (T) -> Unit)

fun <T> KMutableProperty0<T>.toSetting() = Setting(getter = { this.get() }, setter = { this.set(it) })

fun <T : Enum<T>> KMutableProperty0<T>.toStringSetting(values: () -> Array<T>): Setting<String?> {
    val enumSetting = toSetting()
    return Setting(
        getter = { enumSetting.getter().toString() },
        setter = { value ->
            enumSetting.setter(
                values().let {
                    it.find { it.toString() == value } ?: it[0]
                }
            )
        })
}
