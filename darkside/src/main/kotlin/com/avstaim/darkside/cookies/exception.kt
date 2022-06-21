@file:Suppress("unused")

package com.avstaim.darkside.cookies

import kotlin.reflect.KClass

internal val Throwable.runtimeException: RuntimeException
    get() = if (this is RuntimeException) this else RuntimeException(this)

internal inline infix fun <T> (() -> T).invokeCatching(catchBlock: (Throwable) -> T): T =
    try {
        this.invoke()
    } catch (e: Throwable) {
        catchBlock.invoke(e)
    }

internal inline fun <T> (() -> T).invokeCatching(
    vararg throwables: KClass<out Throwable>,
    catchBlock: (Throwable) -> T,
): T =
    try {
        this.invoke()
    } catch (e: Throwable) {
        if (throwables.contains(e::class)) {
            catchBlock.invoke(e)
        } else {
            throw e
        }
    }
