@file:Suppress("unused")

package com.avstaim.darkside.mvi

import kotlinx.coroutines.flow.Flow

fun interface Middleware<A> {
    fun accept(actions: Flow<A>): Flow<A>
}

fun interface Middlewares<A> {
    fun get(): List<Middleware<A>>
}
