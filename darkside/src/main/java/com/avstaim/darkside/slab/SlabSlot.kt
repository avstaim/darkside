@file:Suppress("unused")

package com.avstaim.darkside.slab

import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import com.avstaim.darkside.service.KAssert

/**
 * Wraps [Slot] and it's [View] for safe usage in ViewHolder pattern.
 */
@RequiresApi(Build.VERSION_CODES.KITKAT)
class SlabSlot(initialSlot: Slot) {

    private var _currentView: View = if (initialSlot is SlotView) initialSlot else initialSlot.view
    private var currentSlot: Slot = initialSlot
    private var currentSlab: Slab<*>? = null

    private var currentSlabSlotViewWrapper: SlabSlotViewWrapper? = null

    init {
        initialSlot.attachToWrapper()
    }

    val currentView: View
        get() = _currentView

    fun insert(slab: Slab<*>) {
        if (slab === currentSlab) return

        currentSlabSlotViewWrapper?.detach()
        currentSlabSlotViewWrapper = null

        if (currentSlot.canBeUsed()) {
            currentSlot.performInsert(slab)
        } else {
            currentSlot.wrapSlotViewToInsert(slab)
        }
    }

    private fun Slot.performInsert(slab: Slab<*>) = insert(slab).attachToWrapper()

    private fun Slot.attachToWrapper() {
        KAssert.assertFalse(isUsed) { "Trying to wrap already used slot: $this" }
        onInsertListener(::onInsert)
    }

    private fun onInsert(slab: Slab<*>, replacedView: View, slot: Slot) {
        currentSlab = slab
        currentSlot = slot
        _currentView = replacedView
    }

    private fun Slot.canBeUsed(): Boolean = this !is SlotView || parent != null

    /**
     * We need this as we can't add a slab to slotView when it has no parent.
     */
    private fun Slot.wrapSlotViewToInsert(slab: Slab<*>) {
        currentSlabSlotViewWrapper = SlabSlotViewWrapper(wrapped = this as SlotView) { slotView ->
            slotView.performInsert(slab)
            currentSlabSlotViewWrapper = null
        }
    }
}

@RequiresApi(Build.VERSION_CODES.KITKAT)
private class SlabSlotViewWrapper(
    private val wrapped: SlotView,
    private val onAttach: (SlotView) -> Unit,
) : SlabViewLifecycle() {

    init {
        attachTo(wrapped)
    }

    override fun onAttach() {
        super.onAttach()
        onAttach(wrapped)
        detach()
    }
}
