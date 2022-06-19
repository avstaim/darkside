@file:Suppress("unused")
@file:RequiresApi(Build.VERSION_CODES.KITKAT)

package com.avstaim.darkside.cookies.recycler

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.avstaim.darkside.cookies.recycler.AdapterChunk
import com.avstaim.darkside.slab.BindableSlab

@PublishedApi
internal class DslAdapterChunk<T, I : T>(
    private val slabFactory: (Context) -> BindableSlab<*, *, I>,
    private val isForViewTypeDelegate: (T) -> Boolean,
) : AdapterChunk<T> {

    override fun onCreateViewHolder(context: Context): ChunkViewHolder<T> {
        val slab = slabFactory.invoke(context)
        return ChunkViewHolder<T>(context, slab) { itemToBind ->
            @Suppress("UNCHECKED_CAST")
            slab.bind(itemToBind as I)
        }
    }

    override fun isForViewTypeOf(item: T) = isForViewTypeDelegate(item)
    override fun onBindViewHolder(item: T, holder: ChunkViewHolder<T>) = holder.bind(item)
}
