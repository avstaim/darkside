package com.avstaim.darkside.cookies.mvi

import kotlinx.coroutines.flow.Flow

fun interface StatelessActor<A> {
    fun act(actions: Flow<A>): Flow<A>
}

fun interface StatelessActors<A> {
    fun get(): List<StatelessActor<A>>
}
