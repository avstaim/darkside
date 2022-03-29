package com.avstaim.darkside.cookies.mvi

fun interface Renderer<S> {
    fun render(state: S)
}
