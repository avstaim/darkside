@file:Suppress("unused")

package com.avstaim.darkside.cookies.mvi

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

open class StateFlowRenderer<S>(initial: S) : Renderer<S> {

    val flow: StateFlow<S> get() = mutableFlow

    private val mutableFlow = MutableStateFlow(initial)

    override fun render(state: S) {
        mutableFlow.value = state
    }
}
