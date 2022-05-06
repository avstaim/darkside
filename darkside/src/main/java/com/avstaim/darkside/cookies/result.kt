@file:Suppress("unused")

package com.avstaim.darkside.cookies

import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine

fun <T> T.asSuccessResult(): Result<T> = if (this !is Result<*>) Result.success(this) else this as Result<T>
fun <T> Exception.asFailedResult(): Result<T> = Result.failure(this)

fun <T> T?.asResult(): Result<T> = this?.asSuccessResult() ?: Result.failure(NullPointerException())

inline fun <R, T> Result<T>.flatMapCatching(transform: (value: T) -> Result<R>): Result<R> =
    mapCatching {
        transform(it).getOrThrow()
    }

suspend inline fun <T> suspendCancellableAsResult(
    crossinline block: (CancellableContinuation<T>) -> Unit
): Result<T> = runCatching {
    suspendCancellableCoroutine(block)
}
