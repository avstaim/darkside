@file:Suppress("unused")

package com.avstaim.darkside.cookies.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.currentCoroutineContext

suspend inline fun currentCoroutineScope(): CoroutineScope = CoroutineScope(currentCoroutineContext())

fun CoroutineScope.subScope(): CoroutineScope =
    CoroutineScope(coroutineContext + SupervisorJob(coroutineContext[Job]))