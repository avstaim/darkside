package com.avstaim.darkside.cookies.mvi

fun interface Reducer<S, A> {
    fun reduce(state: S, action: A): S
}
