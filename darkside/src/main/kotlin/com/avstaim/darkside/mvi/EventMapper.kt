@file:Suppress("unused")

package com.avstaim.darkside.mvi

fun interface EventMapper<E, A> {
    fun map(event: E): A
}
