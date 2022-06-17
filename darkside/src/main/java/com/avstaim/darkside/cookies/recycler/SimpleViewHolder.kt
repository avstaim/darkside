@file:Suppress("unused")

package com.avstaim.darkside.cookies.recycler

import android.view.View
import com.avstaim.darkside.dsl.views.Ui

abstract class SimpleViewHolder<D : Any, U : Ui<out View>, H>(host: H, ui: U)
    : ItemViewHolder<D, U, H, Unit>(host, ui, Unit)

abstract class UiViewHolder<D : Any, V : View, U : Ui<V>>(ui: U) : SimpleViewHolder<D, U, Unit>(Unit, ui) {

    override fun U.onBind(data: D, host: Unit) = onBind(data)
    protected abstract fun U.onBind(data: D)
}
