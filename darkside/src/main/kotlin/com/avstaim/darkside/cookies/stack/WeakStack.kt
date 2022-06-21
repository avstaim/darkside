@file:Suppress("unused")

package com.avstaim.darkside.cookies.stack

import java.lang.ref.WeakReference
import java.util.LinkedList

/**
 * [Stack] implementation, where all the references are kept in a weak manner.
 */
class WeakStack<T> : Stack<T> {

    private val linkedList = LinkedList<WeakReference<T>>()

    override val head: T?
        get() = try {
            cleanUpEmptyReferences()
            linkedList.first?.get()
        } catch (e: NoSuchElementException) {
            null
        }

    override val isEmpty: Boolean
        get() {
            cleanUpEmptyReferences()
            return linkedList.isEmpty()
        }

    override operator fun get(index: Int): T? = try {
        cleanUpEmptyReferences()
        linkedList[index].get()
    } catch (e: IndexOutOfBoundsException) {
        null
    }

    override fun push(element: T) = linkedList.push(WeakReference(element))

    override fun pop(): T? = try {
        cleanUpEmptyReferences()
        linkedList.pop()?.get()
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

    override fun iterator(): Iterator<T> {
        cleanUpEmptyReferences()
        return WrappedIterator(linkedList.iterator())
    }

    private fun cleanUpEmptyReferences() {
        val iterator = linkedList.iterator()
        while (iterator.hasNext()) {
            if (iterator.next().get() == null) {
                iterator.remove()
            }
        }
    }

    private class WrappedIterator<T>(private val wrapped: Iterator<WeakReference<T>>) : Iterator<T> {

        override fun hasNext(): Boolean = wrapped.hasNext()
        override fun next(): T = wrapped.next().get() ?: error("No such element")
    }
}
