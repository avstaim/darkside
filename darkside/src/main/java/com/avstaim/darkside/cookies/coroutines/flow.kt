@file:Suppress("unused")

package com.avstaim.darkside.cookies.coroutines

import androidx.core.util.Consumer
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.fold
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.reduce
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicReference
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Terminal flow operator that [launches][launch] the [collection][collect] of the given flow in the [scope]
 * Catches all exceptions of the flow and proceed it to an [onError]
 */
internal inline fun <T> Flow<T>.launchSafe(
    scope: CoroutineScope,
    crossinline onError: (Throwable) -> Unit = {},
    crossinline onEach: (T) -> Unit
): Job {
    return catch { e -> onError.invoke(e) }.onEach { onEach.invoke(it) }.launchIn(scope)
}

fun <T> Flow<T>.launchIn(scope: CoroutineScope, consumer: Consumer<T>): Job {
    return onEach { consumer.accept(it) }.launchIn(scope)
}

fun <T> Flow<T>.observe(scope: CoroutineScope): ReadOnlyProperty<Any?, T?> = FlowPropertyDelegate(this, scope)

private class FlowPropertyDelegate<T>(flow: Flow<T>, scope: CoroutineScope): ReadOnlyProperty<Any?, T?> {

    private var currentValue: T? = null

    init {
        scope.launch {
            flow.collect {
                currentValue = it
            }
        }
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T? = currentValue
}

suspend fun <T> Flow<T>.last(): T = reduce { _, value -> value }
suspend fun <T> Flow<T>.lastOrNull(): T? = fold<T?, T?>(null) { _, value -> value }

fun <A, B : Any, R> Flow<A>.withLatestFrom(other: Flow<B>, transform: suspend (A, B) -> R): Flow<R> = flow {
    coroutineScope {
        val latestB = AtomicReference<B?>()
        val outerScope = this
        launch {
            try {
                other.collect { latestB.set(it) }
            } catch (e: CancellationException) {
                outerScope.cancel(e) // cancel outer scope on cancellation exception, too
            }
        }
        collect { a: A ->
            latestB.get()?.let { b -> emit(transform(a, b)) }
        }
    }
}

/**
 * The terminal operator that returns the first element emitted by the flow matching the given [predicate] mapped to
 * [V] and then cancels flow's collection. Throws [NoSuchElementException] if the flow has not contained elements
 * matching the [predicate].
 *
 * @see first
 */
suspend fun <T, V> Flow<T>.firstOf(predicate: suspend (T) -> V?): V {
    var result: V? = null

    first {
        result = predicate(it)
        result != null
    }

    return result ?: throw NoSuchElementException("Expected at least one element matching the predicate $predicate")
}

/**
 * Returns a flow without any values of the original flow.
 */
fun <T> Flow<T>.dropAll(): Flow<T> = filter { false }

/**
 * Returns a flow consuming all values with the given action
 */
fun <T> Flow<T>.consumeWithAction(action: suspend (T) -> Unit): Flow<T> = onEach(action).dropAll()
