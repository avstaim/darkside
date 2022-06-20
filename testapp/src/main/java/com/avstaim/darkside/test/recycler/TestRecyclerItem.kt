package com.avstaim.darkside.test.recycler

import android.graphics.drawable.Drawable

sealed interface TestRecyclerItem {
    object ConstItem : TestRecyclerItem
    data class IntItem(val i: Int) : TestRecyclerItem
    data class StringItem(val s: String) : TestRecyclerItem
    data class DrawableItem(val d: Drawable) : TestRecyclerItem
}