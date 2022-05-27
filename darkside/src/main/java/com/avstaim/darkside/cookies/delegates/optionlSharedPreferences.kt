@file:Suppress("unused")

package com.avstaim.darkside.cookies.delegates

import android.content.SharedPreferences
import androidx.core.content.edit
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

/**
 * Usage:
 *
 * val/var myProperty by sharedPreferences.preference(defaultValue)
 *
 * NB! Only String, Boolean, Float, Int, Long or Set<String> are supported.
 */
inline fun <reified T : Any> SharedPreferences.optionalPreference(
    defValue: T? = null,
): ReadWriteProperty<Any?, T?> =
    OptionalSharedPreferencesProperty(this, T::class, defValue)

class OptionalSharedPreferencesProperty<T : Any>(
    private val sharedPreferences: SharedPreferences,
    private val klass: KClass<T>,
    private val defValue: T?,
): ReadWriteProperty<Any?, T?> {

    private var cachedValue: T? = null

    override fun getValue(thisRef: Any?, property: KProperty<*>): T? {
        return cachedValue ?: readValue(property.name).also { cachedValue = it } ?: error("internal error")
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
        cachedValue = value
        writeValue(property.name, value)
    }

    private fun readValue(name: String): T? =
        if (sharedPreferences.contains(name)) {
            when (klass) {
                String::class -> sharedPreferences.getString(name, getDefaultValue())
                Boolean::class -> sharedPreferences.getBoolean(name, getDefaultValue(false))
                Int::class -> sharedPreferences.getInt(name, getDefaultValue(0))
                Long::class -> sharedPreferences.getLong(name, getDefaultValue(0L))
                Float::class -> sharedPreferences.getFloat(name, getDefaultValue(0f))
                Set::class -> sharedPreferences.getStringSet(name, getDefaultValue())
                else -> error("Unsupported data type: only String, Boolean, Float, Int, Long or Set<String> are supported.")
            } as T?
        } else {
            defValue
        }

    private fun writeValue(name: String, value: T?) {
        sharedPreferences.edit {
            when (value) {
                is String -> putString(name, value)
                is Boolean -> putBoolean(name, value)
                is Int -> putInt(name, value)
                is Long -> putLong(name, value)
                is Float -> putFloat(name, value)
                is Set<*> -> putStringSet(name, value as Set<String>)
                else -> error("Unsupported data type: only String, Boolean, Float, Int, Long or Set<String> are supported.")
            }
        }
    }

    private inline fun <reified V> getDefaultValue(): V? = defValue as? V
    private inline fun <reified V> getDefaultValue(fallback: V): V = defValue as? V ?: fallback
}
