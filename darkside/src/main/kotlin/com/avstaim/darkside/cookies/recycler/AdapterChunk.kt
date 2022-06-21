@file:Suppress("unused")

package com.avstaim.darkside.cookies.recycler

import android.content.Context

interface AdapterChunk<T> {

    fun isForViewTypeOf(item: T): Boolean

    fun onCreateViewHolder(context: Context): ChunkViewHolder<T>

    fun onBindViewHolder(
        item: T,
        holder: ChunkViewHolder<T>,
    )
}
