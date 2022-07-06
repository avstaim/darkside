@file:Suppress("unused")

package com.avstaim.darkside.cookies.coroutines

import kotlinx.coroutines.Deferred

/**
 * Get value without suspending if deferred was completed,
 * or `null` when it was cancelled or still active.
 */
fun <T> Deferred<T>.getOrNull(): T? =
    when {
        isActive -> null
        isCancelled -> null
        isCompleted -> getCompleted()
        else -> null
    }
