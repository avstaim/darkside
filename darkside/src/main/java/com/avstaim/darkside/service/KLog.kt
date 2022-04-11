@file:Suppress("unused")

package com.avstaim.darkside.service


/**
 * Kotlin wrapper for logging using inline [isEnabled] checks and inlining lambda.
 *
 * Used to prevent unneeded logging string calculation when logging is disabled.
 */
object KLog {

    /**
     * Use delegate for system logging calls.
     */
    var delegate: LoggingDelegate = LoggingDelegate.NoOp

    var isEnabled: Boolean
        get() = delegate.isEnabled
        set(value) {
            delegate = if (value) LoggingDelegate.Android else LoggingDelegate.NoOp
        }

    inline fun v(tag: String, message: () -> String) {
        if (isEnabled) {
            print(LogLevel.VERBOSE, tag, message())
        }
    }

    inline fun v(tag: String, th: Throwable, message: () -> String) {
        if (isEnabled) {
            print(LogLevel.VERBOSE, tag, message(), th)
        }
    }

    inline fun d(tag: String, message: () -> String) {
        if (isEnabled) {
            print(LogLevel.DEBUG, tag, message())
        }
    }

    inline fun d(tag: String, th: Throwable, message: () -> String) {
        if (isEnabled) {
            print(LogLevel.DEBUG, tag, message(), th)
        }
    }

    inline fun w(tag: String, message: () -> String) {
        if (isEnabled) {
            print(LogLevel.WARN, tag, message())
        }
    }

    inline fun w(tag: String, th: Throwable, message: () -> String) {
        if (isEnabled) {
            print(LogLevel.WARN, tag, message(), th)
        }
    }

    inline fun i(tag: String, message: () -> String) {
        if (isEnabled) {
            print(LogLevel.INFO, tag, message())
        }
    }

    inline fun i(tag: String, th: Throwable, message: () -> String) {
        if (isEnabled) {
            print(LogLevel.INFO, tag, message(), th)
        }
    }

    inline fun e(tag: String, message: () -> String) {
        if (isEnabled) {
            print(LogLevel.ERROR, tag, message())
        }
    }

    inline fun e(tag: String, th: Throwable?, message: () -> String = { "" }) {
        if (isEnabled) {
            print(LogLevel.ERROR, tag, message(), th)
        }
    }

    inline fun v(message: () -> String) {
        if (isEnabled) {
            print(LogLevel.VERBOSE, tag = null, message())
        }
    }

    inline fun v(th: Throwable, message: () -> String) {
        if (isEnabled) {
            print(LogLevel.VERBOSE, tag = null, message(), th)
        }
    }

    inline fun d(message: () -> String) {
        if (isEnabled) {
            print(LogLevel.DEBUG, tag = null, message())
        }
    }

    inline fun d(th: Throwable, message: () -> String) {
        if (isEnabled) {
            print(LogLevel.DEBUG, tag = null, message(), th)
        }
    }

    inline fun w(message: () -> String) {
        if (isEnabled) {
            print(LogLevel.WARN, tag = null, message())
        }
    }

    inline fun w(th: Throwable, message: () -> String) {
        if (isEnabled) {
            print(LogLevel.WARN, tag = null, message(), th)
        }
    }

    inline fun i(message: () -> String) {
        if (isEnabled) {
            print(LogLevel.INFO, tag = null, message())
        }
    }

    inline fun i(th: Throwable, message: () -> String) {
        if (isEnabled) {
            print(LogLevel.INFO, tag = null, message(), th)
        }
    }

    inline fun e(message: () -> String) {
        if (isEnabled) {
            print(LogLevel.ERROR, tag = null, message())
        }
    }

    inline fun e(th: Throwable?, message: () -> String = { "" }) {
        if (isEnabled) {
            print(LogLevel.ERROR, tag = null, message(), th)
        }
    }

    fun print(level: LogLevel, tag: String? = null, message: String, th: Throwable? = null) {
        if (!isEnabled) return
        if (th == null) {
            delegate.log(level, tag = tag ?: generateTag(), message)
        } else {
            delegate.log(level, tag = tag ?: generateTag(), message, th)
        }
    }

    private fun generateTag(): String {
        if (isEnabled) {
            val stElements = Thread.currentThread().stackTrace
            for (i in 1 until stElements.size) {
                val element = stElements[i]
                if (element.className != KLog::class.java.name
                    && element.className.indexOf("java.lang.Thread") != 0
                ) {
                    return element.className.substringAfterLast('.') + "[" + element.lineNumber + "]"
                }
            }
        }
        return "Passport"
    }
}

inline fun <T> T.log(level: LogLevel = LogLevel.DEBUG, message: (T) -> String): T =
    also {
        if (KLog.isEnabled) {
            KLog.print(level, message = message(it))
        }
    }
