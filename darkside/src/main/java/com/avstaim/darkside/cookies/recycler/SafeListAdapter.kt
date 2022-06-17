@file:Suppress("unused")

package com.avstaim.darkside.cookies.recycler

import android.annotation.SuppressLint
import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.avstaim.darkside.cookies.interfaces.Bindable
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.reflect.KClass

abstract class AbstractSafeListAdapter<D : Any, VT : Enum<VT>, VH : ItemViewHolder<D, *, *, *, VT>>(
    private val vtClass: KClass<VT>,
    initial: Collection<D>,
) : ListAdapter<D, VH>(DiffCallback()), Bindable<Collection<D>> {

    init {
        submitList(initial.toMutableList())
    }

    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val vt = vtClass.java.enumConstants?.find { it.ordinal == viewType } ?: error("Unsupported viewType $viewType")
        return createViewHolder(parent.context, vt)
    }

    final override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(getItem(position))
    final override fun getItemCount() = super.getItemCount()
    final override fun getItemViewType(position: Int): Int = resolveItemType(getItem(position)).ordinal
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

    abstract fun createViewHolder(context: Context, viewType: VT): VH
    abstract fun resolveItemType(item: D): VT
}

open class SafeListAdapter<D : Any, VT : Enum<VT>, VH : ItemViewHolder<D, *, *, *, VT>>(
    vtClass: KClass<VT>,
    initial: Collection<D>,
    private val factory: ViewHolderFactory<D, VT, VH>,
    private val resolver: ItemTypeResolver<D, VT>,
) : AbstractSafeListAdapter<D, VT, VH>(vtClass, initial) {

    override fun createViewHolder(context: Context, viewType: VT): VH = factory.create(context, viewType)
    override fun resolveItemType(item: D): VT = resolver.resolve(item)
}

fun interface ViewHolderFactory<D : Any, VT : Enum<VT>, VH : ItemViewHolder<in D, *, *, *, VT>> {

    fun create(context: Context, viewType: VT): VH
}

fun interface ItemTypeResolver<D, VT> {

    fun resolve(item: D): VT
}

internal class DiffCallback<D : Any> : DiffUtil.ItemCallback<D>() {

    override fun areItemsTheSame(oldItem: D, newItem: D): Boolean = (oldItem == newItem)
    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: D, newItem: D): Boolean = (oldItem == newItem)
}

inline fun <D : Any, reified VT : Enum<VT>, VH : ItemViewHolder<D, *, *, *, VT>> adapter(
    factory: ViewHolderFactory<D, VT, VH>,
    resolver: ItemTypeResolver<D, VT>,
    initial: Collection<D> = emptyList(),
): SafeListAdapter<D, VT, VH> = SafeListAdapter(VT::class, initial, factory, resolver)
