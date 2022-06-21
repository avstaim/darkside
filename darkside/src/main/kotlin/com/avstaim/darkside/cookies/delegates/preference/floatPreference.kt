@file:Suppress("unused", "NOTHING_TO_INLINE")

package com.avstaim.darkside.cookies.delegates.preference

import android.content.SharedPreferences
import androidx.core.content.edit
import kotlin.properties.ReadWriteProperty

/**
 * Delegates [Float] property to save to [SharedPreferences].
 *
 * Usage:
 *
 * val/var myProperty by sharedPreferences.floatPreference(defaultValue)
 */
inline fun SharedPreferences.floatPreference(
    default: Float,
    name: String? = null,
    commit: Boolean = false,
): ReadWriteProperty<Any?, Float> =
    FloatPreferenceProperty(
        sharedPreferences = this,
        defaultValue = default,
        name, commit,
    )

@PublishedApi
internal class FloatPreferenceProperty(
    sharedPreferences: SharedPreferences,
    defaultValue: Float,
    name: String?,
    commit: Boolean,
): AbstractPreferenceProperty<Float>(sharedPreferences, defaultValue, name, commit) {

    override fun SharedPreferences.readValue(name: String, defaultValue: Float): Float =
        getFloat(name, defaultValue)

    override fun SharedPreferences.writeValue(name: String, value: Float, commit: Boolean) =
        edit(commit) {
            putFloat(name, value)
        }
}
