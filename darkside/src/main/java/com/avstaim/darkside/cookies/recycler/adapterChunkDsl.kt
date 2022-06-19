@file:RequiresApi(Build.VERSION_CODES.KITKAT)
@file:Suppress("unused")

package com.avstaim.darkside.cookies.recycler

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.avstaim.darkside.cookies.recycler.AdapterChunk
import com.avstaim.darkside.slab.BindableSlab

/**
 * @param T type of adapter
 * @param I subtype of [T] handled by this chunk
 */
inline fun <T, reified I : T> adapterChunk(
    noinline slabFactory: (Context) -> BindableSlab<*, *, I>,
): AdapterChunk<T> = DslAdapterChunk(slabFactory) { item -> item is I}

fun <T : Any> chunkedAdapter(initial: List<T> = emptyList(), vararg chunks: AdapterChunk<T>) =
    ChunkedAdapter(chunks.asList(), initial)

fun <T : Any> simpleAdapter(
    initial: List<T> = emptyList(),
    slabFactory: (Context) -> BindableSlab<*, *, T>,
) = chunkedAdapter(initial, DslAdapterChunk(slabFactory) { true })
