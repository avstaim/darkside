@file:Suppress("unused")

package com.avstaim.darkside.cookies.coroutines

import com.avstaim.darkside.cookies.time.CommonTime

/**
 * @see [kotlinx.coroutines.delay], [CommonTime]
 */

suspend inline fun delay(time: CommonTime) = kotlinx.coroutines.delay(time.millis)

suspend inline fun delay(
    hours: Long = 0,
    minutes: Long = 0,
    seconds: Long = 0,
    millis: Long = 0,
) = delay(CommonTime(hours, minutes, seconds, millis))

suspend inline fun delay(
    hours: Int = 0,
    minutes: Int = 0,
    seconds: Int = 0,
    millis: Int = 0,
) = delay(CommonTime(hours, minutes, seconds, millis))
