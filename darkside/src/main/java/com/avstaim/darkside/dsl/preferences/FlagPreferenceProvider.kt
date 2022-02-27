// Copyright (c) 2020 Yandex LLC. All rights reserved.
// Author: Alex Sher <avstaim@yandex-team.ru>

package com.avstaim.darkside.dsl.preferences

import com.avstaim.darkside.flags.BooleanFlag
import com.avstaim.darkside.flags.EnumFlag
import com.avstaim.darkside.flags.Flag
import com.avstaim.darkside.flags.LongFlag

interface FlagPreferenceProvider {

    fun getBoolean(flag: BooleanFlag): Boolean
    fun setBoolean(flag: BooleanFlag, value: Boolean)

    fun <T> getString(flag: Flag<T>): String
    fun <T> setString(flag: Flag<T>, value: String?)

    fun getLong(flag: LongFlag) = getString(flag).toLong()

    fun <T : Enum<T>> getEnum(flag: EnumFlag<T>): T
    fun getEnumAsString(flag: EnumFlag<*>): String
}
