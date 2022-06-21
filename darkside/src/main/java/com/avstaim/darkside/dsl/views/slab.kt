package com.avstaim.darkside.dsl.views

import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.IdRes
import androidx.annotation.StyleRes
import com.avstaim.darkside.slab.SlabSlot
import com.avstaim.darkside.slab.SlotView

/**
 * Safe wrapper for [SlotView] in given [Ui]. When using unwrapped [SlotView] as view holder pattern, after
 * first [SlotView.insert] [SlotView] becomes invalid and can't be used for :
 *  - view attribute set
 *  - layout
 *  - further slab insert.
 *
 *  [SlabSlot] solves this problem by holding always actual reference to current [Slot] and it's view.
 */
inline fun ViewBuilder.slot(
    @IdRes id: Int = View.NO_ID,
    @StyleRes themeRes: Int = NO_THEME,
    @AttrRes styleAttr: Int = NO_STYLE_ATTR,
    @StyleRes styleRes: Int = NO_STYLE_RES,
    init: SlotView.() -> Unit = {}
): SlabSlot = slot(view(id, themeRes, styleAttr, styleRes), init)

/**
 * Variant to use in legacy xml Ui's.
 * Creates [SlabSlot] by [SlotView] as a child of given [View] found by it's id.
 *
 * @see [slot]
 */
inline fun View.findSlabSlotById(
    @IdRes id: Int,
    init: SlotView.() -> Unit = {}
): SlabSlot = slot(findViewById(id), init)

/**
 * Variant to use in legacy xml Ui's.
 * Creates [SlabSlot] by [SlotView] as a child of given [Ui] found by it's id.
 *
 * @see [slot]
 */
inline fun Ui<*>.findSlabSlotById(
    @IdRes id: Int,
    init: SlotView.() -> Unit = {}
): SlabSlot = slot(root.findViewById(id), init)

/**
 * @see [slot]
 */
inline fun slot(
    view: SlotView,
    init: SlotView.() -> Unit = {}
): SlabSlot {
    val slotWrapper = SlabSlot(view)
    view.init()
    return slotWrapper
}

inline fun LayoutBuilder<*>.include(
    slabSlot: SlabSlot,
    init: View.() -> Unit = {},
) = include(view = slabSlot.currentView, initView = init)
