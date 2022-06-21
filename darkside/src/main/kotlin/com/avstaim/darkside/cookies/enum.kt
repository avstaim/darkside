@file:Suppress("unused")

package com.avstaim.darkside.cookies

inline fun <reified T : Enum<T>> Int.asEnumOriginal(): T? = this.let { ordinal ->
    T::class.java.enumConstants?.find { it.ordinal == ordinal }
}
