package com.avstaim.darkside.cookies.mvi

fun interface WishMapper<W, S, A> {

    fun map(wish: W, state: S): A
}
