// Copyright (c) 2020 Yandex LLC. All rights reserved.
// Author: Alex Sher <avstaim@yandex-team.ru>

package com.avstaim.darkside.dsl.preferences

import android.os.Bundle
import androidx.preference.EditTextPreference
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.TwoStatePreference
import com.avstaim.darkside.cookies.noGetter
import com.avstaim.darkside.flags.BooleanFlag
import com.avstaim.darkside.flags.EnumFlag
import com.avstaim.darkside.flags.Flag

class DslPreferenceFragment(
    private val flagPreferenceProvider: FlagPreferenceProvider,
    private val init: DslPreferenceFragment.() -> Unit = {}
) : PreferenceFragmentCompat() {

    var onPreferencesCreated: ((DslPreferenceFragment) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        init()
        onPreferencesCreated?.invoke(this)
    }

    var TwoStatePreference.flag: BooleanFlag
        get() = noGetter()
        set(flag) {
            key = flag.key
            setting = Setting(
                getter = { flagPreferenceProvider.getBoolean(flag) },
                setter = { value -> flagPreferenceProvider.setBoolean(flag, value) }
            )
        }

    var ListPreference.flag: EnumFlag<*>
        get() = noGetter()
        set(flag) {
            key = flag.key
            arrayEntries = flag.cls.enumConstants as Array<*>
            stringSetting = Setting(
                getter = { flagPreferenceProvider.getEnumAsString(flag) },
                setter = { value -> flagPreferenceProvider.setString(flag, value) }
            )
        }

    var EditTextPreference.flag: Flag<*>
        get() = noGetter()
        set(flag) {
            key = flag.key
            setting = Setting(
                getter = { flagPreferenceProvider.getString(flag) },
                setter = { value -> flagPreferenceProvider.setString(flag, value) }
            )
        }
}
