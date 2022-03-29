package com.avstaim.darkside.cookies.mvi

import kotlinx.coroutines.flow.Flow

fun interface Source<W> {

    fun getWishes(): Flow<W>
}
