@file:Suppress("unused")

package com.avstaim.darkside.dsl.preferences

import android.content.Context
import androidx.annotation.ArrayRes
import androidx.annotation.IdRes
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceCategory
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceGroup
import androidx.preference.PreferenceScreen
import com.avstaim.darkside.cookies.noGetter

/**
 * Common code to use for preferences DSL.
 */

inline val PreferenceFragmentCompat.preferenceContext: Context get() = preferenceManager.context

inline fun PreferenceFragmentCompat.preferenceScreen(init: PreferenceScreen.() -> Unit) {
    preferenceScreen = preferenceManager.createPreferenceScreen(preferenceContext)
    preferenceScreen.init()
}

inline fun PreferenceGroup.preferenceCategory(init: PreferenceCategory.() -> Unit): PreferenceCategory {
    val category = PreferenceCategory(context)
    addPreference(category)
    category.init()
    return category
}

inline fun PreferenceGroup.preference(init: Preference.() -> Unit): Preference {
    val preference = Preference(context)
    preference.init()
    addPreference(preference)
    return preference
}

inline fun Preference.onClick(crossinline listener: (Preference) -> Unit) {
    onPreferenceClickListener = Preference.OnPreferenceClickListener { preference ->
        listener(preference)
        true
    }
}

inline fun Preference.onChanged(crossinline listener: (Any) -> Unit) {
    val wrapped = onPreferenceChangeListener
    onPreferenceChangeListener = Preference.OnPreferenceChangeListener { preference, newValue ->
        (wrapped?.onPreferenceChange(preference, newValue) ?: true).also {
            listener(newValue)
        }
    }
}

inline var Preference.titleResource: Int
    get() = noGetter()
    set(value) {
        title = context.resources.getString(value)
    }

inline var Preference.keyResource: Int
    get() = noGetter()
    set(value) {
        key = context.resources.getString(value)
    }

inline var ListPreference.entryResources: Pair<Int, Int>
    get() = noGetter()
    set(value) {
        setEntries(value.first)
        setEntryValues(value.second)
    }

inline var ListPreference.entryTitleResource: Int
    get() = noGetter()
    set(@ArrayRes value) {
        setEntries(value)
    }

inline var ListPreference.entryValueResource: Int
    get() = noGetter()
    set(@ArrayRes value) {
        setEntryValues(value)
    }

inline var Preference.viewId: Int
    get() = noGetter()
    set(@IdRes value) = setViewId(value)
