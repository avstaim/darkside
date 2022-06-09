@file:Suppress("unused")

package com.avstaim.darkside.mvi

fun interface Renderer<S> {
    fun render(state: S)
}
