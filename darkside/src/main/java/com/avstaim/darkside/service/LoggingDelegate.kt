@file:Suppress("unused")

package com.avstaim.darkside.service

import android.util.Log
import com.avstaim.darkside.cookies.ignoreReturnValue

interface LoggingDelegate {

    val isEnabled: Boolean

    fun log(logLevel: LogLevel, tag: String, message: String)
    fun log(logLevel: LogLevel, tag: String, message: String, th: Throwable)


    object Android : LoggingDelegate {

        override val isEnabled = true

        override fun log(logLevel: LogLevel, tag: String, message: String) {
            Log.println(logLevel.value, tag, message)
        }

        override fun log(logLevel: LogLevel, tag: String, message: String, th: Throwable) =
            when (logLevel) {
                LogLevel.VERBOSE -> Log.v(tag, message, th)
                LogLevel.DEBUG -> Log.d(tag, message, th)
                LogLevel.INFO -> Log.i(tag, message, th)
                LogLevel.WARN -> Log.w(tag, message, th)
                LogLevel.ERROR -> Log.e(tag, message, th)
                LogLevel.ASSERT -> Log.wtf(tag, message, th)
            }.ignoreReturnValue()
    }

    object NoOp : LoggingDelegate {

        override val isEnabled = false
        override fun log(logLevel: LogLevel, tag: String, message: String) = Unit
        override fun log(logLevel: LogLevel, tag: String, message: String, th: Throwable) = Unit
    }
}