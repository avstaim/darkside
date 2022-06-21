@file:Suppress("unused")

package com.avstaim.darkside.mvi

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

open class EventBasedStore<E, W, A, S : Any>(
    initialState: S,
    reducer: Reducer<S, A>,
    actors: Actors<A, S>,
    middlewares: Middlewares<A>,
    wishMapper: WishMapper<W, S, A>,
    private val events: Flow<E>,
    private val eventsMapper: EventMapper<E, A>,
) : Store<W, A, S>(reducer, actors, middlewares, wishMapper, initialState) {

    override fun wireWith(coroutineScope: CoroutineScope) {
        super.wireWith(coroutineScope)

        events
            .map(eventsMapper::map)
            .onEach(actions::emit)
            .launchIn(coroutineScope)
    }

}
