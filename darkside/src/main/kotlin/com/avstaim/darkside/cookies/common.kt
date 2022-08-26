@file:Suppress("NOTHING_TO_INLINE", "unused")

package com.avstaim.darkside.cookies

/**
 * Checks if optional is `null`
 */
inline fun <T> T?.isNull(): Boolean = this == null

/**
 * Checks if optional is not `null`
 */
inline fun <T> T?.isNotNull(): Boolean = this != null

/**
 * Links to itself as function. To use as property reference.
 */
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
 * Allows to smartcast var's on the fly.
 */
inline fun <reified T, R> Any?.runIfIs(block: (T) -> R): R? =
    if (this is T) {
        block(this)
    } else null


/**
 * Inverted version of [ifIs].
 */
inline fun <reified T, R> Any?.runIfIsNot(block: (Any?) -> R): R? =
    if (this !is T) {
        block(this)
    } else null

/**
 * Just a stub to ignore any function's return value and return [Unit].
 */
@Suppress("NOTHING_TO_INLINE")
inline fun <T> T?.ignoreReturnValue() = Unit

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
 * Same as [apply], but calling only if criterion is `true`.
 */
inline fun <T> T.applyIf(criterion: Boolean, block: T.() -> Unit): T =
    if (criterion) apply(block) else this

/**
 * Same as [apply], but calling only if criterion is `false`.
 */
inline fun <T> T.applyUnless(criterion: Boolean, block: T.() -> Unit): T =
    if (!criterion) apply(block) else this

/**
 * Same as [also], but calling only if criterion is `true`.
 */
inline fun <T> T.alsoIf(criterion: Boolean, block: (T) -> Unit): T =
    if (criterion) also(block) else this

/**
 * Same as [also], but calling only if criterion is `false`.
 */
inline fun <T> T.alsoUnless(criterion: Boolean, block: (T) -> Unit): T =
    if (!criterion) also(block) else this

/**
 * Execute [block] if criterion is `true`, just return [Unit] otherwise.
 */
inline fun <T> T.doIf(criterion: Boolean, block: T.() -> Unit): Unit =
    if (criterion) block() else Unit

/**
 * Execute [block] if criterion is `false`, just return [Unit] otherwise.
 */
inline fun <T> T.doUnless(criterion: Boolean, block: T.() -> Unit): Unit =
    if (!criterion) block() else Unit

/**
 * Calls the specified function [block] with `this` value as its receiver when current receiver equals to [candidate].
 */
inline fun <T, R> T.ifEquals(candidate: T, block: T.() -> R) {
    if (this == candidate) block()
}

/**
 * Calls the specified function [block] with `this` value as its receiver when current receiver NOT equals to [candidate].
 */
inline fun <T, R> T.unlessEquals(candidate: T, block: T.() -> R) {
    if (this != candidate) block()
}

/**
 * Calls the specified function [block] with `this` value as its receiver and returns its result if and only
 * if current receiver equals to [candidate].
 * When [candidate] is not equals `null` is returned.
 */
inline fun <T, R> T.runIfEquals(candidate: T, block: T.() -> R): R? {
    return if (this == candidate) block() else null
}

/**
 * Calls the specified function [block] with `this` value as its receiver and returns its result unless
 * current receiver equals to [candidate].
 * When [candidate] is equals `null` is returned.
 */
inline fun <T, R> T.runUnlessEquals(candidate: T, block: T.() -> R): R? {
    return if (this != candidate) block() else null
}

/**
 * Same as [apply], but calling only if current receiver equals to [candidate].
 */
inline fun <T> T.applyIfEquals(candidate: T, block: T.() -> Unit): T =
    if (this == candidate) apply(block) else this

/**
 * Same as [apply], but calling only unless current receiver equals to [candidate].
 */
inline fun <T> T.applyUnlessEquals(candidate: T, block: T.() -> Unit): T =
    if (this != candidate) apply(block) else this

/**
 * Same as [also], but calling only if current receiver equals to [candidate].
 */
inline fun <T> T.alsoIfEquals(candidate: T, block: (T) -> Unit): T =
    if (this == candidate) also(block) else this

/**
 * Same as [also], but calling only unless current receiver equals to [candidate].
 */
inline fun <T> T.alsoUnlessEquals(candidate: T, block: (T) -> Unit): T =
    if (this != candidate) also(block) else this

/**
 * Returns `this` if receiver is not `null`, or calls [recovery] to get default value.
 */
inline fun <T> T?.recoverIfNull(recovery: () -> T): T = this ?: recovery()
