@file:Suppress("unused")

package com.avstaim.darkside.cookies.interfaces

interface Transactable<T> {
    fun transaction(): Transaction<T>
    fun commit(action: T.() -> Unit)
}

interface Transaction<T> {
    fun operation(action: T.() -> Unit)
    fun commit(action: T.() -> Unit = {})
}
