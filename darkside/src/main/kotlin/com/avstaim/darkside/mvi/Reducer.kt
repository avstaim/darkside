@file:Suppress("unused")

package com.avstaim.darkside.mvi

fun interface Reducer<S, A> {
    fun reduce(state: S, action: A): S
}
