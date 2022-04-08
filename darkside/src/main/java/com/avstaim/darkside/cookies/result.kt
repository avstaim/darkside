@file:Suppress("unused")

package com.avstaim.darkside.cookies

fun <T> T.asSuccessResult(): Result<T> = if (this !is Result<*>) Result.success(this) else this as Result<T>
fun <T> Exception.asFailedResult(): Result<T> = Result.failure(this)

inline fun <R, T> Result<T>.flatMapCatching(transform: (value: T) -> Result<R>): Result<R> =
    mapCatching {
        transform(it).getOrThrow()
    }
