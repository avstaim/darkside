package com.avstaim.darkside.slab

import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import android.view.AbsSavedState
import android.view.View

/**
 * View to place into layout to be replaced with actual [View] from [Slab] instance in [insert] method.
 *
 * See [Slot] for details.
 */
class SlotView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : View(context, attrs, defStyleAttr), Slot {

    override var isUsed = false
        private set

    private var onInsertListener: ((Slab<*>, View, Slot) -> Unit)? = null

    init {
        setWillNotDraw(true)
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        // Slot have no state, but may receive state from the slabs if it inserted after onRestoreInstanceState.
        super.onRestoreInstanceState(AbsSavedState.EMPTY_STATE)
    }

    override fun insert(slab: Slab<*>): Slot {
        check(!isUsed)
        checkNotNull(parent)
        val replaced = slab.replaceWithSelf(this)
        isUsed = true
        val asSlot = SlabAsSlot(slab, replaced)
        onInsertListener?.invoke(slab, replaced, asSlot)
        onInsertListener = null
        return asSlot
    }

    override val view: View
        get() {
            check(!isUsed)
            checkNotNull(parent)
            return this
        }

    override fun onInsertListener(listener: (Slab<*>, View, Slot) -> Unit) {
        check(!isUsed)
        onInsertListener = listener
    }
}