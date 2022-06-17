@file:Suppress("unused")

package com.avstaim.darkside.cookies.recycler

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.avstaim.darkside.cookies.interfaces.Bindable
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

abstract class AbstractSimpleListAdapter<D : Any, VH : SimpleViewHolder<in D, *, *, *>>(
    initial: Collection<D>,
) : ListAdapter<D, VH>(DiffCallback()), Bindable<Collection<D>> {

    init {
        submitList(initial.toMutableList())
    }

    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH = createViewHolder(parent.context)

    final override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(getItem(position))
    final override fun getItemCount() = super.getItemCount()
    final override fun getItemViewType(position: Int): Int = 1
    final override fun bind(data: Collection<D>) = submitList(data.toMutableList())
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

    abstract fun createViewHolder(context: Context): VH
}

class SimpleListAdapter<D : Any, VH : SimpleViewHolder<in D, *, *, *>>(
    initial: Collection<D>,
    private val factory: (Context) -> VH,
) : AbstractSimpleListAdapter<D, VH>(initial) {

    override fun createViewHolder(context: Context): VH = factory.invoke(context)
}

fun <D : Any, VH : SimpleViewHolder<in D, *, *, *>> simpleAdapter(
    initial: Collection<D> = emptyList(),
    factory: (Context) -> VH,
): SimpleListAdapter<D, VH> = SimpleListAdapter(initial, factory)
