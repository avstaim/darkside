// Copyright (c) 2020 Yandex LLC. All rights reserved.
// Author: Alex Sher <avstaim@yandex-team.ru>

@file:Suppress("NOTHING_TO_INLINE", "unused", "MemberVisibilityCanBePrivate")

package com.avstaim.darkside.views.layouts.constraint

import android.view.View
import androidx.annotation.IdRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.transition.Transition
import androidx.transition.TransitionManager
import com.avstaim.darkside.cookies.noGetter
import com.avstaim.darkside.views.layouts.constraint.ConstraintSetBuilder.Connection.BasicConnection
import com.avstaim.darkside.views.layouts.constraint.ConstraintSetBuilder.Side

/**
 * Anko-inspired DSL constraint builder for [ConstraintLayout].
 */

fun ConstraintLayout.applyConstraints(init: ConstraintSetBuilder.() -> Unit) {
    val constraintSet = ConstraintSetBuilder()
    constraintSet.clone(this)
    constraintSet.init()
    constraintSet.applyTo(this)
}

fun ConstraintLayout.applyConstraintsWithTransition(
    transition: Transition = Transitions.AUTO,
    init: ConstraintSetBuilder.() -> Unit
) {
    val constraintSet = ConstraintSetBuilder()
    constraintSet.clone(this)
    constraintSet.init()
    TransitionManager.beginDelayedTransition(this, transition)
    constraintSet.applyTo(this)
}

class ConstraintSetBuilder : ConstraintSet() {

    /**
     * Build a set of constrains for a given view defined as receiver.
     *
     * @receiver direct reference to view constrains are about to be built for
     */
    operator fun View.invoke(init: ViewConstraintBuilder.() -> Unit) {
        ViewConstraintBuilder(this.id, this@ConstraintSetBuilder).apply(init)
    }

    infix fun Side.of(@IdRes viewId: Int) = when (this) {
        Side.LEFT -> ViewSide.Left(viewId)
        Side.RIGHT -> ViewSide.Right(viewId)
        Side.TOP -> ViewSide.Top(viewId)
        Side.BOTTOM -> ViewSide.Bottom(viewId)
        Side.BASELINE -> ViewSide.Baseline(viewId)
        Side.START -> ViewSide.Start(viewId)
        Side.END -> ViewSide.End(viewId)
    }

    infix fun Side.of(view: View) = this of view.id

    infix fun Pair<ViewSide, Side>.of(@IdRes viewId: Int) = first to (second of viewId)

    infix fun Pair<ViewSide, Side>.of(view: View) = first to (second of view.id)

    infix fun ViewSide.to(targetSide: ViewSide) = BasicConnection(this, targetSide)

    infix fun BasicConnection.margin(margin: Int) =
        Connection.MarginConnection(from, to, margin)

    fun connect(vararg connections: Connection) {
        for (connection in connections) {
            when (connection) {
                is Connection.MarginConnection -> connect(
                    connection.from.viewId,
                    connection.from.sideId,
                    connection.to.viewId,
                    connection.to.sideId,
                    connection.margin
                )
                is BasicConnection -> connect(
                    connection.from.viewId,
                    connection.from.sideId,
                    connection.to.viewId,
                    connection.to.sideId
                )
            }
        }
    }

    enum class Side {
        LEFT,
        RIGHT,
        TOP,
        BOTTOM,
        BASELINE,
        START,
        END,
    }

    sealed class ViewSide(@IdRes val viewId: Int) {
        class Left(@IdRes viewId: Int) : ViewSide(viewId)
        class Right(@IdRes viewId: Int) : ViewSide(viewId)
        class Top(@IdRes viewId: Int) : ViewSide(viewId)
        class Bottom(@IdRes viewId: Int) : ViewSide(viewId)
        class Baseline(@IdRes viewId: Int) : ViewSide(viewId)
        class Start(@IdRes viewId: Int) : ViewSide(viewId)
        class End(@IdRes viewId: Int) : ViewSide(viewId)

        val sideId: Int
            get() = when(this) {
                is Left -> LEFT
                is Right -> RIGHT
                is Top -> TOP
                is Bottom -> BOTTOM
                is Baseline -> BASELINE
                is Start -> START
                is End -> END
            }
    }

    sealed class Connection(val from: ViewSide, val to: ViewSide) {
        class BasicConnection(from: ViewSide, to: ViewSide) : Connection(from, to)
        class MarginConnection(from: ViewSide, to: ViewSide, val margin: Int) : Connection(from, to)
    }
}

open class ViewConstraintBuilder(
    @IdRes private val viewId: Int,
    private val constraintSetBuilder: ConstraintSetBuilder
) {
    val parentId = ConstraintSet.PARENT_ID

    infix fun Pair<Side, Side>.of(@IdRes targetViewId: Int): BasicConnection =
        constraintSetBuilder.run { (first of viewId) to (second of targetViewId) }

    infix fun Pair<Side, Side>.of(targetView: View): BasicConnection = this of targetView.id

    fun setMargin(sideId: Int, value: Int) {
        constraintSetBuilder.setMargin(viewId, sideId, value)
    }

    fun setGoneMargin(sideId: Int, value: Int) {
        constraintSetBuilder.setGoneMargin(viewId, sideId, value)
    }

    fun setGoneMargin(side: Side, value: Int) {
        setGoneMargin(constraintSetBuilder.run { (side of viewId).sideId }, value)
    }

    fun clear() {
        constraintSetBuilder.clear(viewId)
    }

    fun clear(sideId: Int) {
        constraintSetBuilder.clear(viewId, sideId)
    }

    fun clear(side: Side) {
        constraintSetBuilder.clear(viewId, constraintSetBuilder.run { (side of viewId).sideId })
    }

    var visibility: Int
        get() = noGetter()
        set(value) {
            constraintSetBuilder.setVisibility(viewId, value)
        }

    var width: Int
        get() = noGetter()
        set(value) {
            constraintSetBuilder.constrainWidth(viewId, value)
        }

    var height: Int
        get() = noGetter()
        set(value) {
            constraintSetBuilder.constrainHeight(viewId, value)
        }
}

class AttachedConstraintBuilder(
    @IdRes viewId: Int,
    private val constraintSetBuilder: ConstraintSetBuilder
) : ViewConstraintBuilder(viewId, constraintSetBuilder) {

    fun connect(vararg connections: ConstraintSetBuilder.Connection) = constraintSetBuilder.connect(*connections)

    infix fun Side.of(@IdRes viewId: Int) = with(constraintSetBuilder) { of(viewId) }
    infix fun BasicConnection.margin(margin: Int) = with(constraintSetBuilder) { margin(margin) }
}
