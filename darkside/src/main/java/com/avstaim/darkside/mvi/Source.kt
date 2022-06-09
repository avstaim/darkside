@file:Suppress("unused")

package com.avstaim.darkside.mvi

import kotlinx.coroutines.flow.Flow

fun interface Source<W> {

    fun getWishes(): Flow<W>
}
