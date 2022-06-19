@file:Suppress("unused")

package com.avstaim.darkside.cookies.recycler

import android.annotation.SuppressLint
import android.os.Build
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.avstaim.darkside.cookies.interfaces.Bindable
import com.avstaim.darkside.cookies.recycler.AdapterChunk
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

@RequiresApi(Build.VERSION_CODES.KITKAT)
open class ChunkedAdapter<D : Any>(
    protected open val chunks: List<AdapterChunk<D>> = emptyList(),
    initial: List<D> = emptyList(),
) : ListAdapter<D, ChunkViewHolder<D>>(DiffCallback()), Bindable<List<D>> {

    init {
        if (initial.isNotEmpty()) {
            submitList(initial.toMutableList())
        }
    }

    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChunkViewHolder<D> =
        chunks[viewType].onCreateViewHolder(parent.context)

    final override fun onBindViewHolder(holder: ChunkViewHolder<D>, position: Int) = holder.bind(getItem(position))

    final override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        chunks.forEachIndexed { index, chunk ->
            if (chunk.isForViewTypeOf(item)) {
                return index
            }
        }
        error("No matching chunk for item $item at position $position")
    }

    final override fun getItemCount() = super.getItemCount()

    final override fun bind(data: List<D>) = submitList(data.toMutableList())
    final override fun submitList(list: MutableList<D>?) = super.submitList(list)

    suspend fun bindAndWait(data: Collection<D>) {
        suspendCancellableCoroutine<Unit> { continuation ->
            submitList(data.toMutableList()) {
                if (continuation.isActive) {
                    continuation.resume(Unit)
                }
            }
        }
    }
}

internal class DiffCallback<D : Any> : DiffUtil.ItemCallback<D>() {

    override fun areItemsTheSame(oldItem: D, newItem: D): Boolean = (oldItem == newItem)
    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: D, newItem: D): Boolean = (oldItem == newItem)
}
