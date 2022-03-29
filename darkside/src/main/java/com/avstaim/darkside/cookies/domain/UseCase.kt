@file:Suppress("unused")

package com.avstaim.darkside.cookies.domain

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

/**
 * Abstract class to execute app business logic using coroutines
 * All use case's executions a moved on the injected dispatcher
 */
abstract class UseCase<TParams, TResult>(private val dispatcher: CoroutineDispatcher) {

    suspend fun execute(params: TParams): TResult = withContext(dispatcher) {
        run(params)
    }

    protected abstract suspend fun run(params: TParams): TResult
}

suspend inline fun <T> UseCase<Unit, T>.execute() = execute(Unit)
