package com.avstaim.darkside.cookies.stack

import java.util.LinkedList

/**
 * Simple implementation of stack based on [LinkedList] for the cases when only the stack functionality is needed.
 *
 * NB! Using [java.util.Stack] is discouraged.
 */
class SimpleStack<T> : Stack<T> {

    private val linkedList = LinkedList<T>()

    override val head: T?
        get() = try {
            linkedList.first
        } catch (e: NoSuchElementException) {
            null
        }

    override val isEmpty: Boolean
        get() = linkedList.isEmpty()

    override operator fun get(index: Int): T? = try {
        linkedList[index]
    } catch (e: IndexOutOfBoundsException) {
        null
    }

    override fun push(element: T) = linkedList.push(element)

    override fun pop(): T? = try {
        linkedList.pop()
    } catch (e: NoSuchElementException) {
        null
    }

    override fun remove(): Boolean =
        if (isEmpty) {
            false
        } else {
            linkedList.removeFirst() != null
        }

    override fun clear() = linkedList.clear()

    override fun iterator(): Iterator<T> = linkedList.iterator()
}
