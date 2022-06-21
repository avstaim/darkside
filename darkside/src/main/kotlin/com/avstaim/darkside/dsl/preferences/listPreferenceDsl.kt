@file:Suppress("unused")

package com.avstaim.darkside.dsl.preferences

import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceGroup
import com.avstaim.darkside.cookies.noGetter

/**
 * Dsl for [ListPreference].
 */

fun PreferenceGroup.listPreference(init: ListPreference.() -> Unit): ListPreference {
    val preference = ListPreference(context).apply {
        init()
        if (key == null) {
            key = title?.toString()
        }
    }
    addPreference(preference)
    return preference
}

var ListPreference.arrayEntries: Array<*>
    get() = noGetter()
    set(value) {
        entries = value.map { it.toString() }.toTypedArray()
        entryValues = entries
    }

var ListPreference.stringSetting: Setting<String?>
    get() = noGetter()
    set(value) {
        val initialValue = value.getter().toString()
        summary = initialValue
        setValue(initialValue)
        onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, newValue ->
            val stringValue = newValue as String
            value.setter(stringValue)
            summary = stringValue
            true
        }
    }
