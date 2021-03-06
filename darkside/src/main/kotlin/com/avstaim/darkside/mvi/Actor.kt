@file:Suppress("unused")

package com.avstaim.darkside.mvi

import kotlinx.coroutines.flow.Flow

fun interface Actor<A, S> {
    fun act(actions: Flow<A>, state: Flow<S>): Flow<A>
}

fun interface Actors<A, S> {
    fun get(): List<Actor<A, S>>
}
