@file:Suppress("unused")

package com.avstaim.darkside.dsl.views

import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.IdRes
import androidx.annotation.StyleRes
import androidx.recyclerview.widget.RecyclerView

inline fun ViewBuilder.recyclerView(
    @IdRes id: Int = View.NO_ID,
    @StyleRes themeRes: Int = NO_THEME,
    @AttrRes styleAttr: Int = NO_STYLE_ATTR,
    @StyleRes styleRes: Int = NO_STYLE_RES,
    init: RecyclerView.() -> Unit = {}
): RecyclerView = view(id, themeRes, styleAttr, styleRes, init)

/**
 * [RecyclerView.ViewHolder] wrapper for [Ui].
 */
abstract class UiViewHolder<U: Ui<out View>, D>(val ui: U) : RecyclerView.ViewHolder(ui.root) {

    fun bind(data: D) = with(ui) { doBind(data) }
    abstract fun U.doBind(data: D)
}
