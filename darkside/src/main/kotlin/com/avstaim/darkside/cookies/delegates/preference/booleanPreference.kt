@file:Suppress("unused", "NOTHING_TO_INLINE")

package com.avstaim.darkside.cookies.delegates.preference

import android.content.SharedPreferences
import androidx.core.content.edit
import kotlin.properties.ReadWriteProperty

/**
 * Delegates [Boolean] property to save to [SharedPreferences].
 *
 * Usage:
 *
 * val/var myProperty by sharedPreferences.booleanPreference(defaultValue)
 */
inline fun SharedPreferences.booleanPreference(
    default: Boolean,
    name: String? = null,
    commit: Boolean = false,
): ReadWriteProperty<Any?, Boolean> =
    BooleanPreferenceProperty(
        sharedPreferences = this,
        defaultValue = default,
        name, commit,
    )

@PublishedApi
internal class BooleanPreferenceProperty(
    sharedPreferences: SharedPreferences,
    defaultValue: Boolean,
    name: String?,
    commit: Boolean,
): AbstractPreferenceProperty<Boolean>(sharedPreferences, defaultValue, name, commit) {

    override fun SharedPreferences.readValue(name: String, defaultValue: Boolean): Boolean =
        getBoolean(name, defaultValue)

    override fun SharedPreferences.writeValue(name: String, value: Boolean, commit: Boolean) =
        edit(commit) {
            putBoolean(name, value)
        }
}