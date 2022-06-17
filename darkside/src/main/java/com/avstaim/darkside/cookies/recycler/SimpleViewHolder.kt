package com.avstaim.darkside.cookies.recycler

import android.view.View
import com.avstaim.darkside.dsl.views.Ui

abstract class SimpleViewHolder<D : Any, V : View, U : Ui<V>, H>(host: H, ui: U)
    : ItemViewHolder<D, V, U, H, Unit>(host, ui, Unit)

abstract class UiViewHolder<D : Any, V : View, U : Ui<V>>(ui: U) : SimpleViewHolder<D, V, U, Unit>(Unit, ui) {

    override fun U.onBind(data: D, host: Unit) = onBind(data)
    protected abstract fun U.onBind(data: D)
}
