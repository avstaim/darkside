package com.avstaim.darkside.cookies.mvi

import androidx.annotation.CallSuper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach

open class Feature<W, A, S : Any>(
    private val reducer: Reducer<S, A>,
    private val actors: Actors<A, S>,
    private val statelessActors: StatelessActors<A>,
    private val wishMapper: WishMapper<W, S, A>,
    initialState: S,
) {

    protected val states = MutableStateFlow(initialState)

    /**
     * extraBufferCapacity - buffer for emissions to the shared flow, allowing slow subscribers to get values
     * from the buffer without suspending emitters.
     * The buffer space determines how much slow subscribers can lag from the fast ones.
     */
    protected val actions = MutableSharedFlow<A>(extraBufferCapacity = 5)

    @CallSuper
    open fun wireWith(coroutineScope: CoroutineScope) {
        actions
            .onEach { action ->
                val prevState = states.value
                val newState = reducer.reduce(states.value, action)
                if (prevState != newState) {
                    states.emit(newState)
                }
            }
            .launchIn(coroutineScope)

        actors.get()
            .map { actor -> actor.act(actions, states) }
            .merge()
            .onEach(actions::emit)
            .launchIn(coroutineScope)

        statelessActors.get()
            .map { actor -> actor.act(actions) }
            .merge()
            .onEach(actions::emit)
            .launchIn(coroutineScope)
    }

    suspend fun bindSource(source: Source<W>) {
        source.getWishes()
            .map { userAction -> wishMapper.map(userAction, states.value) }
            .collect(actions::emit)
    }

    suspend fun bindRenderer(renderer: Renderer<S>): Nothing = states.collect(renderer::render)
}
