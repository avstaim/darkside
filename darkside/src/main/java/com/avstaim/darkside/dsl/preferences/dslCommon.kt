package com.avstaim.darkside.dsl.preferences

import android.content.Context
import androidx.preference.Preference
import androidx.preference.PreferenceCategory
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceGroup
import androidx.preference.PreferenceScreen
import com.avstaim.darkside.cookies.noGetter

/**
 * Common code to use for preferences DSL.
 */

val PreferenceFragmentCompat.preferenceContext: Context get() = preferenceManager.context

fun PreferenceFragmentCompat.preferenceScreen(init: PreferenceScreen.() -> Unit) {
    preferenceScreen = preferenceManager.createPreferenceScreen(preferenceContext)
    preferenceScreen.init()
}

fun PreferenceGroup.preferenceCategory(init: PreferenceCategory.() -> Unit): PreferenceCategory {
    val category = PreferenceCategory(context)
    addPreference(category)
    category.init()
    return category
}

fun PreferenceGroup.preference(init: Preference.() -> Unit): Preference {
    val preference = Preference(context)
    preference.init()
    addPreference(preference)
    return preference
}

fun Preference.onClick(listener: (Preference) -> Unit) {
    onPreferenceClickListener = Preference.OnPreferenceClickListener { preference ->
        listener(preference)
        true
    }
}

fun Preference.onChanged(listener: (Any) -> Unit) {
    val wrapped = onPreferenceChangeListener
    onPreferenceChangeListener = Preference.OnPreferenceChangeListener { preference, newValue ->
        (wrapped?.onPreferenceChange(preference, newValue) ?: true).also {
            listener(newValue)
        }
    }
}

var Preference.titleResource: Int
    get() = noGetter()
    set(value) {
        title = context.resources.getString(value)
    }

var Preference.keyResource: Int
    get() = noGetter()
    set(value) {
        key = context.resources.getString(value)
    }
