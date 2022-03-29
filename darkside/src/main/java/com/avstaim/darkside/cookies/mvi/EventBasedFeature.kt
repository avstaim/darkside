@file:Suppress("unused")

package com.avstaim.darkside.cookies.mvi

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

open class EventBasedFeature<E, W, A, S : Any>(
    initialState: S,
    reducer: Reducer<S, A>,
    actors: Actors<A, S>,
    statelessActors: StatelessActors<A>,
    wishMapper: WishMapper<W, S, A>,
    private val events: Flow<E>,
    private val eventsMapper: EventMapper<E, A>,
) : Feature<W, A, S>(reducer, actors, statelessActors, wishMapper, initialState) {

    override fun wireWith(coroutineScope: CoroutineScope) {
        super.wireWith(coroutineScope)

        events
            .map(eventsMapper::map)
            .onEach(actions::emit)
            .launchIn(coroutineScope)
    }

}
