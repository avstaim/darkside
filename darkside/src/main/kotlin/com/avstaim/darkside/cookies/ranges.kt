@file:Suppress("unused")

package com.avstaim.darkside.cookies

import kotlin.math.roundToInt

/**
 * Utilities for ranges.
 */

/**
 * Translates value from one range to another using formula:
 *
 * (x - x1) / (x2 - x1) = (y - y1) / (y2 - y1)
 *
 * [Int] version.
 */
fun Int.translateRange(from: ClosedRange<Int>, to: ClosedRange<Int>): Int {
    if (this <= from.start) return to.start
    if (this >= from.endInclusive) return to.endInclusive

    val res =
        ((this - from.start).toFloat() * to.delta.toFloat() / from.delta.toFloat()) + to.start
    return res.roundToInt()
}

/**
 * Translates value from one range to another using formula:
 *
 * (x - x1) / (x2 - x1) = (y - y1) / (y2 - y1)
 *
 * [Float] version.
 */
fun Float.translateRange(from: ClosedRange<Float>, to: ClosedRange<Float>): Float {
    if (this <= from.start) return to.start
    if (this >= from.endInclusive) return to.endInclusive

    return ((this - from.start) * to.delta / from.delta) + to.start
}

val ClosedRange<Int>.delta: Int
    get() = this.endInclusive - this.start

val ClosedRange<Float>.delta: Float
    get() = this.endInclusive - this.start

val ClosedRange<Int>.middle: Int
    get() = this.start + this.delta / 2

val ClosedRange<Float>.middle: Float
    get() = this.start + this.delta / 2f

fun <T : Comparable<T>> ClosedRange<T>.isDegenerate() = endInclusive == start
