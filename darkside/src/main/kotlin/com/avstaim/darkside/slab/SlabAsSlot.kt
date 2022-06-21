package com.avstaim.darkside.slab

import android.view.View

internal class SlabAsSlot(
    private val currentSlab: Slab<*>,
    private val currentView: View,
) : Slot {

    override var isUsed = false
        private set

    private var onInsertListener: ((Slab<*>, View, Slot) -> Unit)? = null


    override fun insert(slab: Slab<*>): Slot {
        if (slab === currentSlab) return this
        val asSlot = currentSlab.replaceWith(slab) as SlabAsSlot

        onInsertListener?.invoke(slab, slab.view, asSlot)
        onInsertListener = null

        isUsed = true
        return asSlot
    }

    override val view: View
        get() {
            // Slab is not attached to the window
            checkNotNull(currentView.parent)
            return currentView
        }

    override fun onInsertListener(listener: (Slab<*>, View, Slot) -> Unit) {
        onInsertListener = listener
    }
}