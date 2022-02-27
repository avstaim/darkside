@file:Suppress("NOTHING_TO_INLINE")

package com.avstaim.darkside.dsl.views

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.ViewManager
import androidx.annotation.Px

interface LayoutBuilder<LP : ViewGroup.LayoutParams> : AddingViewBuilder {

    operator fun <V : View> V.invoke(init: V.() -> Unit): V

    @InternalApi
    fun generateLayoutParams(@Px width: Int, @Px height: Int): LP
}

/**
 * Advanced view builder, that automatically adds views to it's parent.
 */
@InternalApi
interface AddingViewBuilder : ViewBuilder {

    fun attachTo(viewManager: ViewManager)
    fun View.addToParent()
}

@InternalApi
internal interface SimplifiedAddingViewBuilder : AddingViewBuilder {

    override fun attachTo(viewManager: ViewManager) = Unit
}

@InternalApi
class LayoutBuilderImpl<LP : ViewGroup.LayoutParams>(
    override val ctx: Context,
    val lparamsProvider: LProvider<LP>,
) : LayoutBuilder<LP> {

    private var viewManager: ViewManager? = null

    override fun attachTo(viewManager: ViewManager) {
        this.viewManager = viewManager
    }

    override fun View.addToParent() {
        parent?.let { parent ->
            if (parent == viewManager) return
            when (parent) {
                viewManager -> return
                is ViewGroup -> parent.removeView(this)
                else -> error("View is attached to unknown parent $parent")
            }
        }
        when (val manager = viewManager) {
            is ViewGroup -> manager.addView(this)
            is Activity -> manager.addView(this, null)
            null -> throw IllegalStateException("viewManager is not attached")
            else -> throw IllegalStateException("$manager is the wrong parent")
        }
    }

    override fun <V : View> V.invoke(init: V.() -> Unit): V {
        addToParent()
        init()
        return this
    }

    @InternalApi
    override fun generateLayoutParams(width: Int, height: Int): LP = lparamsProvider.invoke(width, height)
}

inline fun <LP : ViewGroup.LayoutParams> ViewGroup.layoutBuilder(
    noinline lparamsProvider: LProvider<LP>
): LayoutBuilder<LP> =
    LayoutBuilderImpl<LP>(context, lparamsProvider).apply { attachTo(viewManager = this@layoutBuilder) }

inline fun <LP : ViewGroup.LayoutParams> Context.layoutBuilder(
    noinline lparamsProvider: LProvider<LP>
): LayoutBuilder<LP> = LayoutBuilderImpl(ctx = this, lparamsProvider)

typealias LProvider<LP> = ((Int, Int) -> LP)