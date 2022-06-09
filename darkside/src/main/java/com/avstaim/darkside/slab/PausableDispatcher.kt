package com.avstaim.darkside.slab

import com.avstaim.darkside.service.KLog
import kotlinx.coroutines.CoroutineDispatcher
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.CoroutineContext

/**
 * [CoroutineDispatcher] able to pause it's dispatching when needed.
 */
internal class PausableDispatcher(
    private val wrapped: CoroutineDispatcher,
) : CoroutineDispatcher() {

    private var count = AtomicInteger()

    @Volatile
    private var isPaused = true
    private val pausedQueue = LinkedBlockingQueue<Task>()

    override fun dispatch(context: CoroutineContext, block: Runnable) {
        val task = Task(count.incrementAndGet(), context, block, wrapped)
        if (isPaused) {
            pausedQueue.offer(task)
        } else {
            task.dispatch()
        }
    }

    fun pause() {
        isPaused = true
    }

    fun resume() {
        isPaused = false
        val iterator = pausedQueue.iterator()
        while (iterator.hasNext()) {
            val task = iterator.next()
            iterator.remove()
            task.dispatch()
        }
    }

    fun reset() {
        isPaused = true
        pausedQueue.clear()
    }

    inner class Task(
        val id: Int,
        private val context: CoroutineContext,
        private val runnable: Runnable,
        private val original: CoroutineDispatcher,
    ) {
        fun dispatch() {
            KLog.v { "$original dispatch $id" }
            original.dispatch(context, runnable)
        }
    }
}