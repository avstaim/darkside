@file:Suppress("unused")

package com.avstaim.darkside.cookies.coroutines

import com.avstaim.darkside.cookies.time.CommonTime
import kotlinx.coroutines.CoroutineScope

suspend fun <T> withTimeout(time: CommonTime, block: suspend CoroutineScope.() -> T)
        = kotlinx.coroutines.withTimeout(time.millis, block)

suspend fun <T> withTimeoutOrNull(time: CommonTime, block: suspend CoroutineScope.() -> T)
        = kotlinx.coroutines.withTimeoutOrNull(time.millis, block)

suspend fun <T> withTimeout(
    hours: Long = 0,
    minutes: Long = 0,
    seconds: Long = 0,
    millis: Long = 0,
    block: suspend CoroutineScope.() -> T,
) = withTimeout(CommonTime(hours, minutes, seconds, millis), block)

suspend fun <T> withTimeoutOrNull(
    hours: Long = 0,
    minutes: Long = 0,
    seconds: Long = 0,
    millis: Long = 0,
    block: suspend CoroutineScope.() -> T,
) = withTimeoutOrNull(CommonTime(hours, minutes, seconds, millis), block)

suspend fun <T> withTimeout(
    hours: Int = 0,
    minutes: Int = 0,
    seconds: Int = 0,
    millis: Int = 0,
    block: suspend CoroutineScope.() -> T,
) = withTimeout(CommonTime(hours, minutes, seconds, millis), block)

suspend fun <T> withTimeoutOrNull(
    hours: Int = 0,
    minutes: Int = 0,
    seconds: Int = 0,
    millis: Int = 0,
    block: suspend CoroutineScope.() -> T,
) = withTimeoutOrNull(CommonTime(hours, minutes, seconds, millis), block)
