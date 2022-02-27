// Copyright (c) 2020 Yandex LLC. All rights reserved.
// Author: Alex Sher <avstaim@yandex-team.ru>

package com.avstaim.darkside.dsl.preferences

import androidx.preference.Preference
import androidx.preference.PreferenceGroup
import androidx.preference.SwitchPreference
import androidx.preference.TwoStatePreference
import com.avstaim.darkside.cookies.noGetter

/**
 * Dsl for [SwitchPreference].
 */

fun PreferenceGroup.switchPreference(dependencyKey: String? = null, init: SwitchPreference.() -> Unit): SwitchPreference =
        with(SwitchPreference(context)) {
            isPersistent = false
            init()
            addPreference(this)
            dependencyKey?.let { dependency = it }
            this
        }

var TwoStatePreference.setting: Setting<Boolean>
    get() = noGetter()
    set(value) {
        isChecked = value.getter()
        onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, newValue ->
            value.setter(newValue as Boolean)
            true
        }
    }
