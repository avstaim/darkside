// Copyright (c) 2020 Yandex LLC. All rights reserved.
// Author: Alex Sher <avstaim@yandex-team.ru>

@file:Suppress("NOTHING_TO_INLINE", "unused")

package com.avstaim.darkside.dsl.views.layouts.constraint

import android.graphics.PointF
import android.view.View
import androidx.annotation.IdRes
import androidx.constraintlayout.widget.Barrier
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.transition.Transition
import androidx.transition.TransitionManager
import com.avstaim.darkside.cookies.noGetter
import com.avstaim.darkside.dsl.views.Transitions
import com.avstaim.darkside.dsl.views.layouts.constraint.ConstraintSetBuilder.Connection.BasicConnection
import com.avstaim.darkside.dsl.views.layouts.constraint.ConstraintSetBuilder.Side

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

    infix fun Connection.BasicConnection.margin(margin: Int) =
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
                is Connection.BasicConnection -> connect(
                    connection.from.viewId,
                    connection.from.sideId,
                    connection.to.viewId,
                    connection.to.sideId
                )
            }
        }
    }

    fun verticalChain(
        connection: Connection,
        style: ChainStyle,
        init: WeightedViewConsumer.() -> Unit,
    ) = verticalChain(
        top = connection.from,
        bottom = connection.to,
        style,
        init,
    )

    fun verticalChain(
        top: ViewSide,
        bottom: ViewSide,
        style: ChainStyle,
        init: WeightedViewConsumer.() -> Unit,
    ) {
        val viewsConsumer = WeightedViewConsumer().also(init)
        createVerticalChain(
            top.viewId, top.sideId,
            bottom.viewId, bottom.sideId,
            viewsConsumer.viewIds,
            viewsConsumer.weights,
            style.value,
        )
    }

    fun horizontalChain(
        connection: Connection,
        style: ChainStyle,
        init: WeightedViewConsumer.() -> Unit,
    ) = horizontalChain(
        top = connection.from,
        bottom = connection.to,
        style,
        init,
    )

    fun horizontalChain(
        top: ViewSide,
        bottom: ViewSide,
        style: ChainStyle,
        init: WeightedViewConsumer.() -> Unit,
    ) {
        val viewsConsumer = WeightedViewConsumer().also(init)
        createHorizontalChain(
            top.viewId, top.sideId,
            bottom.viewId, bottom.sideId,
            viewsConsumer.viewIds,
            viewsConsumer.weights,
            style.value,
        )
    }

    fun verticalGuideline(
        id: Int = generateViewId(),
        begin: Int = -1,
        end: Int = -1,
        percent: Float = -1f,
    ): Int = guideline(ConstraintLayout.LayoutParams.VERTICAL, id, begin, end, percent)

    fun horizontalGuideline(
        id: Int = generateViewId(),
        begin: Int = -1,
        end: Int = -1,
        percent: Float = -1f,
    ): Int = guideline(ConstraintLayout.LayoutParams.HORIZONTAL, id, begin, end, percent)

    private fun guideline(orientation: Int, id: Int, begin: Int = -1, end: Int = -1, percent: Float = -1f): Int {
        create(id, orientation)
        if (begin != -1) {
            setGuidelineBegin(id, begin)
        }
        if (end != -1) {
            setGuidelineEnd(id, end)
        }
        if (percent != -1f) {
            setGuidelinePercent(id, percent)
        }
        return id
    }

    fun barrier(id: Int = generateViewId(), init: BarrierBuilder.() -> Unit): Int {
        BarrierBuilder(id).apply {
            init()
            val barrierDirection = when (direction) {
                Side.LEFT -> Barrier.LEFT
                Side.RIGHT -> Barrier.RIGHT
                Side.TOP -> Barrier.TOP
                Side.BOTTOM -> Barrier.BOTTOM
                Side.START -> Barrier.START
                Side.END -> Barrier.END
                Side.BASELINE -> Barrier.START.also { error("BASELINE not supported for barriers") }
            }
            createBarrier(id, barrierDirection, margin, *viewIds)
        }
        return id
    }

    enum class Side(val sideId: Int) {
        LEFT(ConstraintSet.LEFT),
        RIGHT(ConstraintSet.RIGHT),
        TOP(ConstraintSet.TOP),
        BOTTOM(ConstraintSet.BOTTOM),
        BASELINE(ConstraintSet.BASELINE),
        START(ConstraintSet.START),
        END(ConstraintSet.END),
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
                is Left -> ConstraintSet.LEFT
                is Right -> ConstraintSet.RIGHT
                is Top -> ConstraintSet.TOP
                is Bottom -> ConstraintSet.BOTTOM
                is Baseline -> ConstraintSet.BASELINE
                is Start -> ConstraintSet.START
                is End -> ConstraintSet.END
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

    fun setMargin(side: Side, value: Int) {
        constraintSetBuilder.setMargin(viewId, side.sideId, value)
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

    fun clear(side: ConstraintSetBuilder.Side) {
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

    var horizontalBias: Float
        get() = noGetter()
        set(value) = constraintSetBuilder.setHorizontalBias(viewId, value)

    var verticalBias: Float
        get() = noGetter()
        set(value) = constraintSetBuilder.setHorizontalBias(viewId, value)

    var dimensionRatio: String
        get() = noGetter()
        set(value) = constraintSetBuilder.setDimensionRatio(viewId, value)

    var elevation: Float
        get() = noGetter()
        set(value) = constraintSetBuilder.setElevation(viewId, value)

    var rotation: Float
        get() = noGetter()
        set(value) = constraintSetBuilder.setRotation(viewId, value)

    var rotationX: Float
        get() = noGetter()
        set(value) = constraintSetBuilder.setRotationX(viewId, value)

    var rotationY: Float
        get() = noGetter()
        set(value) = constraintSetBuilder.setRotationY(viewId, value)

    var scaleX: Float
        get() = noGetter()
        set(value) = constraintSetBuilder.setScaleX(viewId, value)

    var scaleY: Float
        get() = noGetter()
        set(value) = constraintSetBuilder.setScaleY(viewId, value)

    var pivot: PointF
        get() = noGetter()
        set(value) = constraintSetBuilder.setTransformPivot(viewId, value.x, value.y)

    var translationX: Float
        get() = noGetter()
        set(value) = constraintSetBuilder.setTranslationX(viewId, value)

    var translationY: Float
        get() = noGetter()
        set(value) = constraintSetBuilder.setTranslationY(viewId, value)

    var translationZ: Float
        get() = noGetter()
        set(value) = constraintSetBuilder.setTranslationZ(viewId, value)

    var translation: PointF
        get() = noGetter()
        set(value) = constraintSetBuilder.setTranslation(viewId, value.x, value.y)

    var maxWidth: Int
        get() = noGetter()
        set(value) = constraintSetBuilder.constrainMaxWidth(viewId, value)

    var maxHeight: Int
        get() = noGetter()
        set(value) = constraintSetBuilder.constrainMaxHeight(viewId, value)

    var minWidth: Int
        get() = noGetter()
        set(value) = constraintSetBuilder.constrainMinWidth(viewId, value)

    var minHeight: Int
        get() = noGetter()
        set(value) = constraintSetBuilder.constrainMinHeight(viewId, value)

    var percentWidth: Float
        get() = noGetter()
        set(value) = constraintSetBuilder.constrainPercentWidth(viewId, value)

    var percentHeight: Float
        get() = noGetter()
        set(value) = constraintSetBuilder.constrainPercentHeight(viewId, value)

    var verticalWeight: Float
        get() = noGetter()
        set(value) = constraintSetBuilder.setVerticalWeight(viewId, value)

    var horizontalWeight: Float
        get() = noGetter()
        set(value) = constraintSetBuilder.setHorizontalWeight(viewId, value)

    var verticalChainStyle: ChainStyle
        get() = noGetter()
        set(value) = constraintSetBuilder.setVerticalChainStyle(viewId, value.value)

    var horizontalChainStyle: ChainStyle
        get() = noGetter()
        set(value) = constraintSetBuilder.setHorizontalChainStyle(viewId, value.value)

    fun addToHorizontalChain(views: ViewIdPair) = constraintSetBuilder.addToHorizontalChainRTL(viewId, views.first, views.second)

    fun addToVerticalChain(views: ViewIdPair) = constraintSetBuilder.addToVerticalChain(viewId, views.first, views.second)
}

class AttachedConstraintBuilder(
    @IdRes viewId: Int,
    private val constraintSetBuilder: ConstraintSetBuilder
) : ViewConstraintBuilder(viewId, constraintSetBuilder) {

    fun connect(vararg connections: ConstraintSetBuilder.Connection) = constraintSetBuilder.connect(*connections)

    infix fun Side.of(@IdRes viewId: Int) = with(constraintSetBuilder) { of(viewId) }
    infix fun BasicConnection.margin(margin: Int) = with(constraintSetBuilder) { margin(margin) }
}

class BarrierBuilder(val barrierId: Int) : ViewConsumer() {
    var direction: Side = Side.BASELINE
    var margin: Int = 0
}
