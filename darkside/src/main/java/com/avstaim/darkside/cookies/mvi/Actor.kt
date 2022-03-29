package com.avstaim.darkside.cookies.mvi

import kotlinx.coroutines.flow.Flow

fun interface Actor<A, S> {
    fun act(actions: Flow<A>, state: Flow<S>): Flow<A>
}

fun interface Actors<A, S> {
    fun get(): List<Actor<A, S>>
}
