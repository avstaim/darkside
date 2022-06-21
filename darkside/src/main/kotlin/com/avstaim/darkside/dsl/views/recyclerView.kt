@file:Suppress("unused")

package com.avstaim.darkside.dsl.views

import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.IdRes
import androidx.annotation.StyleRes
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.avstaim.darkside.common.HorizontalDirection
import com.avstaim.darkside.common.VerticalDirection
import com.avstaim.darkside.cookies.noGetter

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

inline var RecyclerView.itemTouchHelper: ItemTouchHelper
    get() = noGetter()
    set(value) {
        value.attachToRecyclerView(this)
    }

inline fun <reified VH : RecyclerView.ViewHolder> horizontalSwipeItemTouchHelper(
    crossinline onSwipe: (VH, HorizontalDirection) -> Unit,
) =
    ItemTouchHelper(
        object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

            override fun isLongPressDragEnabled() = false
            override fun isItemViewSwipeEnabled() = true

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder
            ): Boolean = false // Moving not supported

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                (viewHolder as? VH)?.let { vh ->
                    val direction = when (swipeDir) {
                        ItemTouchHelper.LEFT -> HorizontalDirection.RIGHT_TO_LEFT
                        ItemTouchHelper.RIGHT -> HorizontalDirection.LEFT_TO_RIGHT
                        else -> return
                    }
                    onSwipe(vh, direction)
                }
            }
        }
    )

inline fun <reified VH : RecyclerView.ViewHolder> verticalSwipeItemTouchHelper(
    crossinline onSwipe: (VH, VerticalDirection) -> Unit,
) =
    ItemTouchHelper(
        object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

            override fun isLongPressDragEnabled() = false
            override fun isItemViewSwipeEnabled() = true

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder
            ): Boolean = false // Moving not supported

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                (viewHolder as? VH)?.let { vh ->
                    val direction = when (swipeDir) {
                        ItemTouchHelper.DOWN -> VerticalDirection.TOP_TO_BOTTOM
                        ItemTouchHelper.UP -> VerticalDirection.BOTTOM_TO_TOP
                        else -> return
                    }
                    onSwipe(vh, direction)
                }
            }
        }
    )
