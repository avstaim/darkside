@file:Suppress("unused")

package com.avstaim.darkside.slab

import android.content.Context
import android.view.View
import com.avstaim.darkside.dsl.views.Ui

class SlabUi<V : View>(val slab: Slab<out V>) : Ui<V> {

    @Suppress("UNCHECKED_CAST")
    private val slabView: V by lazy {
        slab.extractView() as V
    }

    override val ctx: Context
        get() = root.context

    override val root: V
        get() = slabView
}

fun <V : View> Slab<V>.toUi(): Ui<V> = SlabUi(slab = this)
