@file:Suppress("unused", "NOTHING_TO_INLINE")

package com.avstaim.darkside.cookies.delegates.preference

import android.content.SharedPreferences
import androidx.core.content.edit
import kotlin.properties.ReadWriteProperty

/**
 * Delegates [Int] property to save to [SharedPreferences].
 *
 * Usage:
 *
 * val/var myProperty by sharedPreferences.intPreference(defaultValue)
 */
inline fun SharedPreferences.intPreference(
    default: Int,
    name: String? = null,
    commit: Boolean = false,
): ReadWriteProperty<Any?, Int> =
    IntPreferenceProperty(
        sharedPreferences = this,
        defaultValue = default,
        name, commit,
    )

@PublishedApi
internal class IntPreferenceProperty(
    sharedPreferences: SharedPreferences,
    defaultValue: Int,
    name: String?,
    commit: Boolean,
): AbstractPreferenceProperty<Int>(sharedPreferences, defaultValue, name, commit) {

    override fun SharedPreferences.readValue(name: String, defaultValue: Int): Int =
        getInt(name, defaultValue)

    override fun SharedPreferences.writeValue(name: String, value: Int, commit: Boolean) =
        edit(commit) {
            putInt(name, value)
        }
}
