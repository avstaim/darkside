@file:Suppress("unused", "NOTHING_TO_INLINE")

package com.avstaim.darkside.cookies.delegates.preference

import android.content.SharedPreferences
import androidx.core.content.edit
import kotlin.properties.ReadWriteProperty

/**
 * Delegates [Long] property to save to [SharedPreferences].
 *
 * Usage:
 *
 * val/var myProperty by sharedPreferences.longPreference(defaultValue)
 */
inline fun SharedPreferences.longPreference(
    default: Long,
    name: String? = null,
    commit: Boolean = false,
): ReadWriteProperty<Any?, Long> =
    LongPreferenceProperty(
        sharedPreferences = this,
        defaultValue = default,
        name, commit,
    )

@PublishedApi
internal class LongPreferenceProperty(
    sharedPreferences: SharedPreferences,
    defaultValue: Long,
    name: String?,
    commit: Boolean,
): AbstractPreferenceProperty<Long>(sharedPreferences, defaultValue, name, commit) {

    override fun SharedPreferences.readValue(name: String, defaultValue: Long): Long =
        getLong(name, defaultValue)

    override fun SharedPreferences.writeValue(name: String, value: Long, commit: Boolean) =
        edit(commit) {
            putLong(name, value)
        }
}
