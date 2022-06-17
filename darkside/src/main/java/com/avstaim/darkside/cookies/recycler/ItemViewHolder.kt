@file:Suppress("unused")

package com.avstaim.darkside.cookies.recycler

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.avstaim.darkside.cookies.interfaces.Bindable
import com.avstaim.darkside.dsl.views.Ui

/**
 * Convenience ViewHolder class for list items.
 *
 * @param D data to bind into this [ItemViewHolder]
 * @param V view to be stored in view holder
 * @param U ui to layout in view holder
 * @param H host to dispatch Ui interaction events on
 * @param VT view type to distinguish view holder from others
 */
abstract class ItemViewHolder<D : Any, out B : D, U : Ui<out View>, H, out VT>(
    private val host: H,
    private val ui: U,
    val viewType: VT,
) : RecyclerView.ViewHolder(ui.root), Bindable<D> {

    /**
     * Don't create objects, use non inlined lambdas, or call methods doing so in this callback
     * as it may be called a lot of times as the user scrolls faster and faster, and allocating
     * memory could affect the UI smoothness.
     * @see dataToBind
     * @see bind
     */
    protected abstract fun U.onBind(dataToBind: @UnsafeVariance B, host: H)

    @Suppress("UNCHECKED_CAST")
    final override fun bind(data: D) = ui.onBind(data as B, host)
}
