package com.avstaim.darkside.cookies.coroutines

/**
 * Alternative for [run] method to use in suspend environments as method reference.
 */
@Suppress("REDUNDANT_INLINE_SUSPEND_FUNCTION_TYPE")
suspend inline fun <T, R> T.runSuspend(block: suspend T.() -> R): R = block()

/**
 * Alternative for [let] method to use in suspend environments as method reference.
 */
@Suppress("REDUNDANT_INLINE_SUSPEND_FUNCTION_TYPE")
suspend inline fun <T, R> T.letSuspend(block: suspend (T) -> R): R = block(this)

/**
 * Alternative for [apply] method to use in suspend environments as method reference.
 */
@Suppress("REDUNDANT_INLINE_SUSPEND_FUNCTION_TYPE")
suspend inline fun <T> T.applySuspend(block: suspend T.() -> Unit): T {
    block()
    return this
}

/**
 * Alternative for [also] method to use in suspend environments as method reference.
 */
@Suppress("REDUNDANT_INLINE_SUSPEND_FUNCTION_TYPE")
suspend inline fun <T> T.alsoSuspend(block: suspend (T) -> Unit): T {
    block(this)
    return this
}
