@file:Suppress("NOTHING_TO_INLINE", "unused")

package com.avstaim.darkside.cookies

inline fun <T> T?.isNull(): Boolean = this == null
inline fun <T> T?.isNotNull(): Boolean = this != null

inline fun <T> itself(t: T): T = t

/**
 * Allows to smartcast var's on the fly.
 */
inline fun <reified T> Any?.ifIs(block: (T) -> Unit) {
    if (this is T) {
        block(this)
    }
}

/**
 * Inverted version of [ifIs].
 */
inline fun <reified T> Any?.ifIsNot(block: (Any?) -> Unit) {
    if (this !is T) {
        block(this)
    }
}


/**
 * Calls the specified function [block] and returns its result if and only if [condition] is satisfied.
 * When [condition] is not satisfied `null` is returned.
 */
inline fun <R> runIf(condition: Boolean, block: () -> R): R? {
    return if (condition) block() else null
}

/**
 * Calls the specified function [block] with `this` value as its receiver and returns its result if and only
 * if [condition] is satisfied.
 * When [condition] is not satisfied `null` is returned.
 */
inline fun <T, R> T.runIf(condition: Boolean, block: T.() -> R): R? {
    return if (condition) block() else null
}

/**
 * Calls the specified function [block] and returns its result unless [condition] is satisfied.
 * When [condition] is satisfied `null` is returned.
 */
inline fun <R> runUnless(condition: Boolean, block: () -> R): R? {
    return if (!condition) block() else null
}

/**
 * Calls the specified function [block] with `this` value as its receiver and returns its result unless [condition]
 * is satisfied.
 * When [condition] is satisfied `null` is returned.
 */
inline fun <T, R> T.runUnless(condition: Boolean, block: T.() -> R): R? {
    return if (!condition) block() else null
}

/**
 * Just a stub to ignore any function's return value and return [Unit].
 */
@Suppress("NOTHING_TO_INLINE")
inline fun <T> T?.ignoreReturnValue() = Unit

/**
 * Same as [apply], but calling only if criterion is `true`.
 */
inline fun <T> T.applyIf(criterion: Boolean, block: T.() -> Unit): T =
    if (criterion) apply(block) else this

/**
 * Same as [also], but calling only if criterion is `true`.
 */
inline fun <T> T.alsoIf(criterion: Boolean, block: (T) -> Unit): T =
    if (criterion) also(block) else this

