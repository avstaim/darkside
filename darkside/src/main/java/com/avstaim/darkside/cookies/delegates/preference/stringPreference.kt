@file:Suppress("unused", "NOTHING_TO_INLINE")

package com.avstaim.darkside.cookies.delegates.preference

import android.content.SharedPreferences
import androidx.core.content.edit
import com.avstaim.darkside.cookies.itself
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Delegates non-null [String] property to save to [SharedPreferences].
 *
 * Usage:
 *
 * val/var myProperty by sharedPreferences.stringPreference(defaultValue)
 */
inline fun SharedPreferences.stringPreference(
    default: String,
    name: String? = null,
    commit: Boolean = false,
): ReadWriteProperty<Any?, String> =
    StringPreferenceProperty(
        sharedPreferences = this,
        defaultValue = default,
        name, commit,
        reader = ::itself,
        writer = ::itself,
    )

/**
 * Delegates optional (nullable) [String] property to save to [SharedPreferences].
 *
 * Usage:
 *
 * val/var myProperty by sharedPreferences.optionalStringPreference(defaultValue)
 */
inline fun SharedPreferences.optionalStringPreference(
    default: String? = null,
    name: String? = null,
    commit: Boolean = false,
): ReadWriteProperty<Any?, String?> =
    StringPreferenceProperty(
        sharedPreferences = this,
        defaultValue = default,
        name, commit,
        reader = ::itself,
        writer = { it ?: "" },
    )

/**
 * Delegates non-null serializable property to save to [SharedPreferences].
 *
 * Usage:
 *
 * val/var myProperty by sharedPreferences.preference(defaultValue, reader, writer)
 *
 * @param writer lambda to serialize given type [T] to [String]
 * @param reader lambda to deserialize [String] to given type [T]
 */
inline fun <reified T> SharedPreferences.preference(
    default: T,
    name: String? = null,
    commit: Boolean = false,
    noinline writer: (T) -> String,
    noinline reader: (String) -> T,
): ReadWriteProperty<Any?, T> =
    StringPreferenceProperty(
        sharedPreferences = this,
        defaultValue = default,
        name, commit,
        reader = reader,
        writer = writer,
    )

/**
 * Delegates optional (nullable) serializable property to save to [SharedPreferences].
 *
 * Usage:
 *
 * val/var myProperty by sharedPreferences.optionalPreference(defaultValue, reader, writer)
 *
 * @param writer lambda to serialize given type [T] to [String]
 * @param reader lambda to deserialize [String] to given type [T]
 */
inline fun <reified T> SharedPreferences.optionalPreference(
    default: T? = null,
    name: String? = null,
    commit: Boolean = false,
    noinline writer: (T) -> String,
    noinline reader: (String) -> T,
): ReadWriteProperty<Any?, T?> =
    StringPreferenceProperty(
        sharedPreferences = this,
        defaultValue = default,
        name, commit,
        reader = { reader(it) },
        writer = { it?.let(writer) ?: "" },
    )

@PublishedApi
internal class StringPreferenceProperty<T>(
    private val sharedPreferences: SharedPreferences,
    private val defaultValue: T,
    private val name: String?,
    private val commit: Boolean,
    private val reader: (String) -> T,
    private val writer: (T) -> String,
): ReadWriteProperty<Any?, T> {

    private var cachedValue: T? = null

    override fun getValue(thisRef: Any?, property: KProperty<*>): T =
        cachedValue
            ?: readValue(name ?: property.name).also { cachedValue = it }
            ?: defaultValue

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        cachedValue = value
        writeValue(name ?: property.name, value)
    }

    private fun readValue(name: String): T? =
        sharedPreferences.getString(name, null)?.run(reader)

    private fun writeValue(name: String, value: T) {
        sharedPreferences.edit(commit) {
            if (value != null) {
                putString(name, writer(value))
            } else {
                remove(name)
            }
        }
    }
}
