package com.avstaim.darkside.cookies.delegates.preference

import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

@PublishedApi
internal abstract class AbstractPreferenceProperty<T>(
    private val sharedPreferences: SharedPreferences,
    private val defaultValue: T,
    private val name: String?,
    private val commit: Boolean,
): ReadWriteProperty<Any?, T> {

    private var cachedValue: T? = null

    override fun getValue(thisRef: Any?, property: KProperty<*>): T =
        cachedValue ?: sharedPreferences.readValue(
            name = name ?: property.name,
            defaultValue = defaultValue,
        ).also { cachedValue = it }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        cachedValue = value
        sharedPreferences.writeValue(name ?: property.name, value, commit)
    }

    abstract fun SharedPreferences.readValue(name: String, defaultValue: T): T
    abstract fun SharedPreferences.writeValue(name: String, value: T, commit: Boolean)
}
