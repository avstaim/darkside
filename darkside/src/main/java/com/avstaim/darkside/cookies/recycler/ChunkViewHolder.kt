@file:Suppress("unused")

package com.avstaim.darkside.cookies.recycler

import android.content.Context
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.avstaim.darkside.cookies.interfaces.Bindable
import com.avstaim.darkside.dsl.views.matchParent
import com.avstaim.darkside.slab.Slab
import com.avstaim.darkside.slab.SlabSlot
import com.avstaim.darkside.slab.SlotView

class ChunkViewHolder<T>(
    context: Context,
    private val slab: Slab<*>,
    private val bindable: Bindable<T>,
) : RecyclerView.ViewHolder(FrameLayout(context)), Bindable<T> {

    private val slot: SlabSlot

    init {
        val frameLayout = itemView as FrameLayout
        val slotView = SlotView(context)
        frameLayout.addView(slotView, matchParent, matchParent)
        slot = SlabSlot(slotView)
    }

    override fun bind(data: T) {
        slot.insert(slab)
        bindable.bind(data)
    }
}
