@file:Suppress("unused")

package com.avstaim.darkside.cookies.recycler

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.avstaim.darkside.cookies.interfaces.Bindable
import com.avstaim.darkside.slab.Slab
import com.avstaim.darkside.slab.SlabSlot
import com.avstaim.darkside.slab.SlotView

@RequiresApi(Build.VERSION_CODES.KITKAT)
class ChunkViewHolder<T>(
    context: Context,
    private val slab: Slab<*>,
    private val bindable: Bindable<T>,
) : RecyclerView.ViewHolder(SlotView(context)), Bindable<T> {

    private val slot = SlabSlot(itemView as SlotView)

    override fun bind(data: T) {
        slot.insert(slab)
        bindable.bind(data)
    }
}
