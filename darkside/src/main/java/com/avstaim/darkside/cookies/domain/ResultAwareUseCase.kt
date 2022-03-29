@file:Suppress("unused")

package com.avstaim.darkside.cookies.domain

import kotlinx.coroutines.CoroutineDispatcher

abstract class ResultAwareUseCase<TParams, TResult>(dispatcher: CoroutineDispatcher) :
    UseCase<TParams, Result<TResult>>(dispatcher)
