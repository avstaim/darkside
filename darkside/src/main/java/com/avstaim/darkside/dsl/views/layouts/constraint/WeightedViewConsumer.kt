package com.avstaim.darkside.dsl.views.layouts.constraint

import android.os.Build
import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.RequiresApi
import com.avstaim.darkside.slab.SlabSlot

class WeightedViewConsumer {

    val viewIds: IntArray get() = consumed.map { it.first }.toIntArray()
    val weights: FloatArray get() = consumed.map { it.second }.toFloatArray()

    private val consumed = mutableListOf<Pair<Int, Float>>()

    operator fun View.invoke(weight: Float) = consumed.add(this.id to weight)
    operator fun View.invoke() = consumed.add(this.id to 1f)

    operator fun @receiver:IdRes Int.invoke(weight: Float) = consumed.add(this to weight)
    operator fun @receiver:IdRes Int.invoke() = consumed.add(this to 1f)

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    operator fun SlabSlot.invoke(weight: Float) = consumed.add(this.currentView.id to weight)
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    operator fun SlabSlot.invoke() = consumed.add(this.currentView.id to 1f)
}
