@file:Suppress("unused")

package com.avstaim.darkside.cookies.domain

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

/**
 * Abstract class to execute app business logic using coroutine's flow
 * Flow execution is moved on the injected dispatcher
 */
abstract class FlowUseCase<TParams, TResult>(private val dispatcher: CoroutineDispatcher) {

    fun execute(params: TParams): Flow<TResult> = run(params).flowOn(dispatcher)

    protected abstract fun run(params: TParams): Flow<TResult>
}

fun <T> FlowUseCase<Unit, T>.execute() = execute(Unit)
