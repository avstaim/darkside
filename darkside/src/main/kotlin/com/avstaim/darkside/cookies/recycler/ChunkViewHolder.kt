@file:Suppress("unused")

package com.avstaim.darkside.cookies.recycler

import androidx.recyclerview.widget.RecyclerView
import com.avstaim.darkside.cookies.interfaces.Bindable
import com.avstaim.darkside.slab.Slab

class ChunkViewHolder<T>(
    slab: Slab<*>,
    private val bindable: Bindable<T>,
) : RecyclerView.ViewHolder(slab.extractView()), Bindable<T> {

    override fun bind(data: T) = bindable.bind(data)
}
