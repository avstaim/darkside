package com.avstaim.darkside.cookies.mvi

fun interface EventMapper<E, A> {
    fun map(event: E): A
}
