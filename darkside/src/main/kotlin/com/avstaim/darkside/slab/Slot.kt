package com.avstaim.darkside.slab

import android.view.View

/**
 * Represents slot that can be filled with [Slab].
 *
 * Each instance of [Slot] acts like a "token" and can be used only once.
 */
interface Slot {

    /**
     * Fills actual slot with a [Slab]'s [View].
     * If actual [Slot] is currently filled with [Slab] full detach cycle will be performed.
     *
     * No-op if slot is already filled with the given [Slab].
     *
     * @return new [Slot] instance to replace slot content next time.
     */
    fun insert(slab: Slab<*>): Slot

    /**
     * @return the [View] that currently occupies the slot.
     */
    val view: View

    /**
     * Sets a listener to know when a [Slab] is inserted into this [Slot].
     */
    fun onInsertListener(listener: (Slab<*>, View, Slot) -> Unit)

    /**
     * @return true when the [Slot] was already used.
     */
    val isUsed: Boolean
}