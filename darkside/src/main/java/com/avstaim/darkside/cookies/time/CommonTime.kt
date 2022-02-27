@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.avstaim.darkside.cookies.time

import java.util.concurrent.TimeUnit
import kotlin.math.roundToLong

/**
 * Universal class, representing time.
 */
@Suppress("EXPERIMENTAL_FEATURE_WARNING")
inline class CommonTime(private val durationMs: Long) : Comparable<CommonTime> {

    constructor(hours: Long = 0,
                minutes: Long = 0,
                seconds: Long = 0,
                millis: Long = 0) : this (
        TimeUnit.HOURS.toMillis(hours) +
                TimeUnit.MINUTES.toMillis(minutes) +
                TimeUnit.SECONDS.toMillis(seconds) +
                TimeUnit.MILLISECONDS.toMillis(millis)
    )

    constructor(hours: Int = 0,
                minutes: Int = 0,
                seconds: Int = 0,
                millis: Int = 0) : this(hours.toLong(), minutes.toLong(), seconds.toLong(), millis.toLong())

    val millis get() = TimeUnit.MILLISECONDS.toMillis(durationMs)
    val seconds get() = TimeUnit.MILLISECONDS.toSeconds(durationMs)
    val minutes get() = TimeUnit.MILLISECONDS.toMinutes(durationMs)
    val hours get() = TimeUnit.MILLISECONDS.toHours(durationMs)
    val days get() = TimeUnit.MILLISECONDS.toDays(durationMs)

    val intMillis get() = millis.toInt()
    val intSeconds get() = seconds.toInt()
    val intMinutes get() = minutes.toInt()
    val intHours get() = hours.toInt()
    val intDays get() = days.toInt()

    val isZero get() = millis == 0L

    val reducedHours get() = minutes / 60
    val reducedMinutes get() = minutes - reducedHours * 60
    val reducedSeconds get() = seconds - minutes * 60
    val reducedMillis get() = millis - seconds * 1000

    override fun toString(): String {
        return if (reducedHours > 0) {
            format("HH:mm:ss")
        } else {
            format("mm:ss")
        }
    }

    fun toStringWithHours(): String {
        return format("HH:mm:ss")
    }

    /**
     * y - year
     * M - month
     * d - day
     * h - hour
     * m - minute
     * s - second
     * S - millis
     */
    fun format(format: String): String {
        return CommonTimeFormatHelper.formatDuration(durationMs, format, true)
    }

    fun addMillis(millis: Int) = addMillis(millis.toLong())
    fun addSeconds(seconds: Int) = addSeconds(seconds.toLong())
    fun addMinutes(minutes: Int) = addMinutes(minutes.toLong())
    fun addHours(hours: Int) = addHours(hours.toLong())

    fun addMillis(millis: Long) = this + CommonTime(millis = millis)
    fun addSeconds(seconds: Long) = this + CommonTime(seconds = seconds)
    fun addMinutes(minutes: Long) = this + CommonTime(minutes = minutes)
    fun addHours(hours: Long) = this + CommonTime(hours = hours)

    fun subtractMillis(millis: Int) = subtractMillis(millis.toLong())
    fun subtractSeconds(seconds: Int) = subtractSeconds(seconds.toLong())
    fun subtractMinutes(minutes: Int) = subtractMinutes(minutes.toLong())
    fun subtractHours(hours: Int) = subtractHours(hours.toLong())

    fun subtractMillis(millis: Long) = this - CommonTime(millis = millis)
    fun subtractSeconds(seconds: Long) = this - CommonTime(seconds = seconds)
    fun subtractMinutes(minutes: Long) = this - CommonTime(minutes = minutes)
    fun subtractHours(hours: Long) = this - CommonTime(hours = hours)

    operator fun times(c: Float): CommonTime = CommonTime((durationMs * c.toDouble()).roundToLong())

    operator fun div(other: CommonTime): Float = (durationMs.toDouble() / other.durationMs.toDouble()).toFloat()

    operator fun minus(other: CommonTime): CommonTime = CommonTime(durationMs - other.durationMs)
    operator fun plus(other: CommonTime): CommonTime = CommonTime(durationMs + other.durationMs)

    override operator fun compareTo(other: CommonTime) = durationMs.compareTo(other.durationMs)

    companion object {

        val ZERO = CommonTime(durationMs = 0L)
    }
}
