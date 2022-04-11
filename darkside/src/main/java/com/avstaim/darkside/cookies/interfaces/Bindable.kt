@file:Suppress("unused")

package com.avstaim.darkside.cookies.interfaces

fun interface Bindable<T> {
    fun bind(data: T)
}

inline fun <T, V : Bindable<T>> V.andBind(data: T) = also { it.bind(data) }
