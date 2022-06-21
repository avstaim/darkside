package com.avstaim.darkside.cookies.stack

/**
 * Current java lacks good Stack interface and implementations.
 *
 * Using [java.util.Stack] is not an option.
 */
interface Stack<T> : Iterable<T> {

    /**
     * Gets the head of the stack or 'null' when it is empty.
     */
    val head: T?

    /**
     * Is stack empty or not?
     */
    val isEmpty: Boolean

    /**
     * Get element at index or 'null' if no element at such index.
     */
    operator fun get(index: Int): T?

    /**
     * Pushes element into stack.
     */
    fun push(element: T)

    /**
     * Pops (and removes) element from the stack.
     *
     * @return popped element or 'null' if the stack was empty.
     */
    fun pop(): T?

    /**
     * Removes head element from the stack.
     *
     * @return 'true' when elemnt was removed, 'false' when the stack was empty.
     */
    fun remove(): Boolean

    /**
     * Clears the whole stack.
     */
    fun clear()
}
