@file:Suppress("unused")

package com.avstaim.darkside.service

interface AssertionDelegate {

    val isEnabled: Boolean

    fun failAssert(message: String?, cause: Throwable? = null)

    object Default : AssertionDelegate {

        override val isEnabled = true

        override fun failAssert(message: String?, cause: Throwable?): Nothing =
            throw AssertionError(message ?: "").also {
                it.initCause(cause)
            }
    }

    object NoOp : AssertionDelegate {

        override val isEnabled = false
        override fun failAssert(message: String?, cause: Throwable?) = Unit
    }
}
