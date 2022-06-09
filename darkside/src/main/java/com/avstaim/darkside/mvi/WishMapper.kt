@file:Suppress("unused")

package com.avstaim.darkside.mvi

fun interface WishMapper<W, S, A> {

    fun map(wish: W, state: S): A
}
