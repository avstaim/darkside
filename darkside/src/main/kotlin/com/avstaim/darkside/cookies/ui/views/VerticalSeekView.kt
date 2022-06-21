@file:Suppress("UsePropertyAccessSyntax", "unused")

package com.avstaim.darkside.cookies.ui.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.ColorInt
import com.avstaim.darkside.cookies.delta
import com.avstaim.darkside.cookies.dp
import com.avstaim.darkside.cookies.dpF
import kotlin.math.roundToInt

private val SEEK_LINE_WIDTH = dp(2)
private val SEEK_BUBBLE_RADIUS = dpF(5)
private val SEEK_BUBBLE_RADIUS_ACTIVE = dpF(8)
private val ROUND_RECT_RADIUS = dpF(4)

/**
 * An alternative to [android.widget.SeekBar] vertically oriented and following KISS principle.
 *
 * TODO(avstaim): xml declareStyleable support
 */
class VerticalSeekView  @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    @ColorInt
    var mainColor = Color.GRAY
        set(value) {
            field = value
            activePaint.color = value
            roundRectDrawable.paint.color = value
            invalidate()
        }

    @ColorInt
    var inactiveColor = Color.WHITE
        set(value) {
            field = value
            inactivePaint.color = value
            invalidate()
        }

    var range: IntRange = 0..100
        set(newRange) {
            val lastPosition = position
            field = newRange
            position = lastPosition
        }

    var value: Int = 0
        set(value) {
            field = value.coerceIn(range)
            invalidate()
        }

    var seekListener: SeekListener? = null

    private var position: Float
        get() = value.toFloat() / range.delta
        set(newPosition) {
            value = range.first + (newPosition * range.delta).roundToInt()
        }

    private val activePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = mainColor
    }

    private val inactivePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = inactiveColor
    }

    private val roundRectDrawable = ShapeDrawable(
        RoundRectShape(ROUND_RECT_RADIUS.let { floatArrayOf(it, it, it, it, it, it, it, it) }, null, null)
    ).apply {
        paint.color = mainColor
    }

    private var seekSession: SeekSession? = null

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val paddingWidth = paddingLeft + paddingRight
        val paddingHeight = paddingTop + paddingBottom

        val realWidth = width - paddingWidth
        val realHeight = height - paddingHeight

        val seekLineWidth = SEEK_LINE_WIDTH
        val seekLineXPositionMid = paddingLeft + realWidth / 2f
        val seekLineXPosition = seekLineXPositionMid - seekLineWidth / 2
        val seekerYPosition = (realHeight * (1f - position)) + paddingTop

        canvas.drawRect(seekLineXPosition, paddingTop.toFloat(), seekLineXPosition + seekLineWidth, seekerYPosition,
                        inactivePaint)
        canvas.drawRect(seekLineXPosition, seekerYPosition, seekLineXPosition + seekLineWidth,
                        height - paddingBottom.toFloat(), activePaint)

        val bubbleRadius = if (seekSession == null) SEEK_BUBBLE_RADIUS else SEEK_BUBBLE_RADIUS_ACTIVE
        canvas.drawCircle(seekLineXPositionMid, seekerYPosition, bubbleRadius, activePaint)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            seekSession = SeekSession()
        }

        return seekSession?.onTouchEvent(event) ?: false
    }

    private inner class SeekSession {

        private val initialPosition = position
        private val realHeight get() = (height - (paddingTop + paddingBottom)).toFloat()

        init {
            seekListener?.onSeekStart(value)
        }

        fun onTouchEvent(event: MotionEvent): Boolean {
            when (event.action) {
                MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> onMove(event.y)
                MotionEvent.ACTION_UP -> {
                    onMove(event.y)
                    endSession()
                }
                MotionEvent.ACTION_CANCEL -> {
                    position = initialPosition
                    endSession()
                }
                else -> return false
            }
            return true
        }

        private fun onMove(y: Float) {
            val realY = (y - paddingTop).coerceIn(0f..realHeight)
            position = (1f - realY / realHeight)
            seekListener?.onSeekProgress(value)
        }

        fun endSession() {
            seekListener?.onSeekFinish(value)
            seekSession = null
        }
    }

    interface SeekListener {

        fun onSeekStart(value: Int) = Unit
        fun onSeekProgress(value: Int) = Unit
        fun onSeekFinish(value: Int) = Unit
    }
}
