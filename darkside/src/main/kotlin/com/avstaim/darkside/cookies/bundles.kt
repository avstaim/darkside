@file:Suppress("NOTHING_TO_INLINE", "unused")

package com.avstaim.darkside.cookies

import androidx.core.os.bundleOf
import android.os.Bundle
import android.os.Parcelable
import java.io.Serializable
import kotlin.reflect.KClass

inline fun <reified T : Serializable> Bundle.getSerializableAndCast(key: String): T? =
    getSerializable(key) as? T

inline fun <reified T : Serializable> Bundle.requireSerializable(key: String): T =
    getSerializable(key) as? T ?: error("can't get required serializable $key")

inline fun <T : Parcelable> Bundle.requireParcelable(key: String): T =
    getParcelable(key) ?: error("can't get required parcelable $key")

inline fun <T : Parcelable> Bundle.requireParcelableArrayList(key: String): ArrayList<T> =
    getParcelableArrayList(key) ?: error("can't get required parcelable array list $key")

inline fun Bundle.requireString(key: String): String =
    getString(key) ?: error("can't get required string $key")

inline fun Bundle.requireBundle(key: String): Bundle =
    getBundle(key) ?: error("can't get required bundle $key")

inline fun emptyBundle() = Bundle()

inline fun bundleOf(init: Bundle.() -> Unit) = Bundle().also(init)

inline fun <T : Enum<T>> Bundle.putEnum(key: String, value: T) = putInt(key, value.ordinal)
inline fun <reified T : Enum<T>> Bundle.getEnum(key: String): T? = getEnum(T::class, key)
inline fun <reified T : Enum<T>> Bundle.requireEnum(key: String): T = requireEnum(T::class, key)

inline fun <T : Enum<T>> Bundle.getEnum(klass: KClass<T>, key: String): T? = getInt(key).let { ordinal ->
    klass.java.enumConstants?.find { it.ordinal == ordinal }
}

inline fun <T : Enum<T>> Bundle.requireEnum(klass: KClass<T>, key: String): T =
    getEnum<T>(klass, key) ?: error("can't get required enum $key")

fun Array<out Pair<String, Any?>>.asBundle(): Bundle = bundleOf(*this)