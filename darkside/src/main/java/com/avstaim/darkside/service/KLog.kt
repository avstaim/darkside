package com.avstaim.darkside.service

/**
 * Kotlin wrapper for [Log] using inline [isEnabled] checks and inlining lambda.
 *
 * Used to prevent unneeded logging string calculation when logging is disabled.
 */
object KLog {

    @field:Volatile
    var isEnabled: Boolean = false

    inline fun v(tag: String, message: () -> String) {
        if (isEnabled) {
            print(android.util.Log.VERBOSE, tag, message())
        }
    }

    inline fun v(tag: String, th: Throwable, message: () -> String) {
        if (isEnabled) {
            android.util.Log.v(tag, message(), th)
        }
    }

    inline fun d(tag: String, message: () -> String) {
        if (isEnabled) {
            print(android.util.Log.DEBUG, tag, message())
        }
    }

    inline fun d(tag: String, th: Throwable, message: () -> String) {
        if (isEnabled) {
            android.util.Log.d(tag, message(), th)
        }
    }

    inline fun w(tag: String, message: () -> String) {
        if (isEnabled) {
            print(android.util.Log.WARN, tag, message())
        }
    }

    inline fun w(tag: String, th: Throwable, message: () -> String) {
        if (isEnabled) {
            android.util.Log.w(tag, message(), th)
        }
    }

    inline fun i(tag: String, message: () -> String) {
        if (isEnabled) {
            print(android.util.Log.INFO, tag, message())
        }
    }

    inline fun i(tag: String, th: Throwable, message: () -> String) {
        if (isEnabled) {
            android.util.Log.i(tag, message(), th)
        }
    }

    inline fun e(tag: String, message: () -> String) {
        if (isEnabled) {
            print(android.util.Log.ERROR, tag, message())
        }
    }

    inline fun e(tag: String, th: Throwable?, message: () -> String = { "" }) {
        if (isEnabled) {
            android.util.Log.e(tag, message(), th)
        }
    }

    inline fun v(message: () -> String) {
        if (isEnabled) {
            print(android.util.Log.VERBOSE, tag = null, message())
        }
    }

    inline fun v(th: Throwable, message: () -> String) {
        if (isEnabled) {
            print(android.util.Log.VERBOSE, tag = null, message(), th)
        }
    }

    inline fun d(message: () -> String) {
        if (isEnabled) {
            print(android.util.Log.DEBUG, tag = null, message())
        }
    }

    inline fun d(th: Throwable, message: () -> String) {
        if (isEnabled) {
            print(android.util.Log.DEBUG, tag = null, message(), th)
        }
    }

    inline fun w(message: () -> String) {
        if (isEnabled) {
            print(android.util.Log.WARN, tag = null, message())
        }
    }

    inline fun w(th: Throwable, message: () -> String) {
        if (isEnabled) {
            print(android.util.Log.WARN, tag = null, message(), th)
        }
    }

    inline fun i(message: () -> String) {
        if (isEnabled) {
            print(android.util.Log.INFO, tag = null, message())
        }
    }

    inline fun i(th: Throwable, message: () -> String) {
        if (isEnabled) {
            print(android.util.Log.INFO, tag = null, message(), th)
        }
    }

    inline fun e(message: () -> String) {
        if (isEnabled) {
            print(android.util.Log.ERROR, tag = null, message())
        }
    }

    inline fun e(th: Throwable?, message: () -> String = { "" }) {
        if (isEnabled) {
            print(android.util.Log.ERROR, tag = null, message(), th)
        }
    }

    fun print(priority: Int, tag: String? = null, message: String, th: Throwable? = null) {
        if (!isEnabled) return
        val tag = tag ?: generateTag()
        if (th == null) {
            android.util.Log.println(priority, tag, message)
        } else {
            when(priority) {
                android.util.Log.VERBOSE -> android.util.Log.v(tag, message, th)
                android.util.Log.DEBUG -> android.util.Log.d(tag, message, th)
                android.util.Log.INFO -> android.util.Log.i(tag, message, th)
                android.util.Log.WARN -> android.util.Log.w(tag, message, th)
                android.util.Log.ERROR -> android.util.Log.e(tag, message, th)
                android.util.Log.ASSERT -> android.util.Log.wtf(tag, message, th)
                else -> error("wrong priority: $priority")
            }
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

inline fun <T> T.andAlsoLogD(message: (T) -> String): T = also { KLog.d { message(it) } }
