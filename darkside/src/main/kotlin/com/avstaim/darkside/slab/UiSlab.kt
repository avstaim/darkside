package com.avstaim.darkside.slab

import android.view.View
import com.avstaim.darkside.dsl.views.Ui

abstract class UiSlab<V : View, U : Ui<V>> : Slab<V>() {

    protected abstract val ui: U
    override val view: V get() = ui.root
}
