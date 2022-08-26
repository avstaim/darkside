@file:Suppress("unused")

package com.avstaim.darkside.cookies

import java.util.Collections

fun <K,V> mapOfNotNull(vararg pair: Pair<K,V>?): Map<K,V> = listOfNotNull(*pair).toMap()

fun <K, V> Map<K, V?>.filterNotNullValues(): Map<K, V> =
    buildMap { for ((k, v) in this@filterNotNullValues) if (v != null) put(k, v) }

fun <T> MutableList<T>.toUnmodifiableList(): List<T> = Collections.unmodifiableList(this.toList())

fun <K,V> MutableMap<K, V>.toUnmodifiableMap(): Map<K, V> = Collections.unmodifiableMap(this.toMap())


inline fun <T> List<T>?.whenNotEmpty(action: (List<T>) -> Unit) {
    if (this != null && isNotEmpty()) {
        action(this)
    }
}

fun <K, V> Map<out K, V>.getOrThrow(key: K, message: String? = null): V {
    return get(key) ?: throw NoSuchElementException(message)
}

fun <K, V> MutableMap<out K, V>.removeOrThrow(key: K, message: String? = null): V {
    return remove(key) ?: throw NoSuchElementException(message)
}

inline fun <K, V> MutableMap<K, V>.removeIf(predicate: (Map.Entry<K, V>) -> Boolean): MutableMap<K, V> {
    val iterator = this.iterator()
    while (iterator.hasNext()) {
        if (predicate(iterator.next())) {
            iterator.remove()
        }
    }
    return this
}

inline fun <reified K, reified V> mapOfNotNullValues(
    vararg pairs: Pair<K, V?>,
): Map<K, V> {

    return pairs
        .filter { it.second != null }
        .map { it as Pair<K, V> }
        .toMap()
}

inline fun <K, V, R> Map<out K, V>.mapValuesOnly(transform: (V) -> R): Map<K, R> =
    mapValues { entry -> transform(entry.value) }

