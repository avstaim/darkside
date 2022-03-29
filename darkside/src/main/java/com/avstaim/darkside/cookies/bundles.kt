@file:Suppress("NOTHING_TO_INLINE")

package com.avstaim.darkside.cookies

import android.os.Bundle
import android.os.Parcelable
import java.io.Serializable
import kotlin.reflect.KClass

internal inline fun <reified T : Serializable> Bundle.getSerializableAndCast(key: String): T? =
    getSerializable(key) as? T

internal inline fun <reified T : Serializable> Bundle.requireSerializable(key: String): T =
    getSerializable(key) as? T ?: error("can't get required serializable $key")

internal inline fun <T : Parcelable> Bundle.requireParcelable(key: String): T =
    getParcelable(key) ?: error("can't get required parcelable $key")

internal inline fun <T : Parcelable> Bundle.requireParcelableArrayList(key: String): ArrayList<T> =
    getParcelableArrayList(key) ?: error("can't get required parcelable array list $key")

internal inline fun Bundle.requireString(key: String): String =
    getString(key) ?: error("can't get required string $key")

internal inline fun Bundle.requireBundle(key: String): Bundle =
    getBundle(key) ?: error("can't get required bundle $key")

inline fun emptyBundle() = Bundle()

inline fun bundleOf(init: Bundle.() -> Unit) = Bundle().also(init)

internal inline fun <T : Enum<T>> Bundle.putEnum(key: String, value: T) = putInt(key, value.ordinal)
internal inline fun <reified T : Enum<T>> Bundle.getEnum(key: String): T? = getEnum(T::class, key)
internal inline fun <reified T : Enum<T>> Bundle.requireEnum(key: String): T = requireEnum(T::class, key)

internal inline fun <T : Enum<T>> Bundle.getEnum(klass: KClass<T>, key: String): T? = getInt(key).let { ordinal ->
    klass.java.enumConstants?.find { it.ordinal == ordinal }
}
internal inline fun <T : Enum<T>> Bundle.requireEnum(klass: KClass<T>, key: String): T =
    getEnum<T>(klass, key) ?: error("can't get required enum $key")
