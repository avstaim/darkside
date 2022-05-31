@file:Suppress("unused", "NOTHING_TO_INLINE")

package com.avstaim.darkside.cookies.delegates.preference

import android.content.SharedPreferences
import androidx.core.content.edit
import kotlin.properties.ReadWriteProperty

/**
 * Delegates [Set] of [String] property to save to [SharedPreferences].
 *
 * Usage:
 *
 * val/var myProperty by sharedPreferences.setPreference(defaultValue)
 */
inline fun SharedPreferences.setPreference(
    default: Set<String> = emptySet(),
    name: String? = null,
    commit: Boolean = false,
): ReadWriteProperty<Any?, Set<String>> =
    SetPreferenceProperty(
        sharedPreferences = this,
        defaultValue = default,
        name, commit,
    )

@PublishedApi
internal class SetPreferenceProperty(
    sharedPreferences: SharedPreferences,
    defaultValue: Set<String>,
    name: String?,
    commit: Boolean,
): AbstractPreferenceProperty<Set<String>>(sharedPreferences, defaultValue, name, commit) {

    override fun SharedPreferences.readValue(name: String, defaultValue: Set<String>): Set<String> =
        getStringSet(name, defaultValue) ?: defaultValue

    override fun SharedPreferences.writeValue(name: String, value: Set<String>, commit: Boolean) =
        edit(commit) {
            putStringSet(name, value)
        }
}
