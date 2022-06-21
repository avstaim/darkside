@file:Suppress("unused")

package com.avstaim.darkside.cookies.domain

import com.avstaim.darkside.cookies.asSuccessResult
import com.avstaim.darkside.cookies.coroutines.delay
import com.avstaim.darkside.cookies.time.CommonTime
import com.avstaim.darkside.service.KAssert
import com.avstaim.darkside.service.KLog
import kotlinx.coroutines.CoroutineDispatcher

open class RetryingUseCase<TParams, TResult>(
    dispatcher: CoroutineDispatcher,
    private val baseUseCase: UseCase<TParams, Result<TResult>>,
) : ResultAwareUseCase<TParams, TResult>(dispatcher) {

    var retryDelay = CommonTime(millis = 300)
        set(value) {
            if (value < CommonTime(millis = 10) || value > CommonTime(hours = 24)) {
                KLog.e { "wrong delay value $value" }
                return
            }
            field = value
        }

    var retryAttempts = 3
        set(value) {
            if (value < 1 || value > 256) {
                KLog.e { "wrong retryAttempts value $value" }
                return
            }
            field = value
        }

    override suspend fun run(params: TParams): Result<TResult> {
        var retryCounter = 0
        do baseUseCase.execute(params).fold(
            onSuccess = {
                return it.asSuccessResult()
            },
            onFailure = { throwable ->
                if (retryCounter++ >= retryAttempts || !shouldRetry(throwable)) {
                    return Result.failure(throwable)
                }
                delay(retryDelay)
            }
        )
        while (retryCounter < retryAttempts)
        KAssert.fail { "Internal error" }
        return Result.failure(RuntimeException("Internal error"))
    }

    open fun shouldRetry(throwable: Throwable): Boolean = true
}
