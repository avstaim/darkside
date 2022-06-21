@file:Suppress("unused")

package com.avstaim.darkside.dsl.preferences

import com.avstaim.darkside.flags.Flag

interface FlagPreferenceProvider {

    fun <T> putFlag(flag: Flag<T>, value: T)
    fun <T> getFlag(flag: Flag<T>) : T

    fun <T> getFlagString(flag: Flag<T>): String
    fun <T> putFlagString(flag: Flag<T>, value: String?)

    object Empty : FlagPreferenceProvider {

        override fun <T> putFlag(flag: Flag<T>, value: T) = Unit
        override fun <T> putFlagString(flag: Flag<T>, value: String?) = Unit

        override fun <T> getFlag(flag: Flag<T>): T = error("No flags supported")
        override fun <T> getFlagString(flag: Flag<T>): String = error("No flags supported")
    }
}
