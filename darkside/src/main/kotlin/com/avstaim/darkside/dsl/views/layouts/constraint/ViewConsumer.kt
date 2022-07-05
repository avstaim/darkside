package com.avstaim.darkside.dsl.views.layouts.constraint

import android.view.View
import androidx.annotation.IdRes
import com.avstaim.darkside.slab.SlabSlot

open class ViewConsumer {

    val viewIds: IntArray get() = consumed.toIntArray()

    protected val consumed = mutableListOf<Int>()

    operator fun View.invoke() = consumed.add(this.id)

    operator fun @receiver:IdRes Int.invoke() = consumed.add(this)

    operator fun SlabSlot.invoke() = consumed.add(this.currentView.id)
}
