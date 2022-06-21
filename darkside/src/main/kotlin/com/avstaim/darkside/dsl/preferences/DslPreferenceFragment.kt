@file:Suppress("unused")

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
    private val flagPreferenceProvider: FlagPreferenceProvider = FlagPreferenceProvider.Empty,
    private val init: DslPreferenceFragment.() -> Unit = {}
) : PreferenceFragmentCompat() {

    var onPreferencesCreated: ((DslPreferenceFragment) -> Unit)? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        init()
        onPreferencesCreated?.invoke(this)
    }

    var TwoStatePreference.flag: BooleanFlag
        get() = noGetter()
        set(flag) {
            key = flag.key
            setting = Setting(
                getter = { flagPreferenceProvider.getFlag(flag) },
                setter = { value -> flagPreferenceProvider.putFlag(flag, value) }
            )
        }

    var ListPreference.flag: EnumFlag<*>
        get() = noGetter()
        set(flag) {
            key = flag.key
            arrayEntries = flag.values
            stringSetting = Setting(
                getter = { flagPreferenceProvider.getFlagString(flag) },
                setter = { value -> flagPreferenceProvider.putFlagString(flag, value) }
            )
        }

    var EditTextPreference.flag: Flag<*>
        get() = noGetter()
        set(flag) {
            key = flag.key
            setting = Setting(
                getter = { flagPreferenceProvider.getFlagString(flag) },
                setter = { value -> flagPreferenceProvider.putFlagString(flag, value) }
            )
        }

    var TextWithSuggestsPreference.flag: Flag<*>
        get() = noGetter()
        set(flag) {
            key = flag.key
            setting = Setting(
                getter = { flagPreferenceProvider.getFlagString(flag) },
                setter = { value -> flagPreferenceProvider.putFlagString(flag, value) }
            )
        }
}
