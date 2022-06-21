@file:Suppress("NOTHING_TO_INLINE", "unused")

package com.avstaim.darkside.service

import android.os.Build
import android.os.Looper
import com.avstaim.darkside.service.KAssert.isEnabled

/**
 * Kotlin assertion wrapper, using inline [isEnabled] checks and inlining lambdas.
 */
object KAssert {

    /**
     * Use delegate for assertion failure calls.
     */
    var delegate: AssertionDelegate = AssertionDelegate.NoOp

    var isEnabled: Boolean
        get() = delegate.isEnabled
        set(value) {
            delegate = if (value) AssertionDelegate.Default else AssertionDelegate.NoOp
        }

    /**
     * Fails with the given message.
     */
    inline fun fail(message: () -> String) {
        if (isEnabled) {
            doFail(message())
        }
    }

    /**
     * Fails with the given message and throwable that caused the failure.
     */
    inline fun fail(cause: Throwable?, message: () -> String = { "" }) {
        if (isEnabled) {
            doFail(message(), cause)
        }
    }

    /**
     * [KAssert.fail] + [KLog.e]
     */
    inline fun failWithLog(tag: String, message: () -> String) {
        KLog.e(tag, message)
        fail(message)
    }

    /**
     * [KAssert.fail] + [KLog.e]
     */
    inline fun failWithLog(tag: String, cause: Throwable, message: () -> String = { "" }) {
        KLog.e(tag, cause, message)
        fail(cause, message)
    }

    /**
     * Asserts that a condition is true. If it isn't it throws an [AssertionError] with the given message.
     */
    inline fun assertTrue(condition: Boolean, message: () -> String = { "" }) {
        if (isEnabled && !condition) {
            doFail(message())
        }
    }

    /**
     * Asserts that a condition is false. If it isn't it throws an [AssertionError] with the given message.
     */
    inline fun assertFalse(condition: Boolean, message: () -> String = { "" }) {
        if (isEnabled && condition) {
            doFail(message())
        }
    }

    /**
     * Asserts that a condition is true. If it isn't it throws an [AssertionError] with the given message.
     */
    inline fun assertTrue(condition: () -> Boolean, message: () -> String = { "" }) {
        if (isEnabled && !condition()) {
            doFail(message())
        }
    }

    /**
     * Asserts that a condition is false. If it isn't it throws an [AssertionError] with the given message.
     */
    inline fun assertFalse(condition: () -> Boolean, message: () -> String = { "" }) {
        if (isEnabled && condition()) {
            doFail(message())
        }
    }

    /**
     * Asserts that two objects are equal. If they are not, an {@link AssertionError} is thrown with the given message.
     * If [expected] and [actual] are [null], they are considered equal.
     */
    inline fun assertEquals(expected: Any?, actual: Any?, message: () -> String = { "" }) {
        if (isEnabled) {
            doAssertEquals(message(), expected, actual)
        }
    }

    /**
     * Asserts that an object isn't null. If it is an [AssertionError] is thrown with the given message.
     */
    inline fun assertNotNull(nullable: Any?, message: () -> String = { "" }) {
        if (isEnabled && nullable == null) {
            doFail(message())
        }
    }

    /**
     *Asserts that an object is null. If it is an [AssertionError] is thrown with the given message.
     */
    inline fun assertNull(nullable: Any?, message: () -> String = { "" }) {
        if (isEnabled && nullable != null) {
            doFail(message())
        }
    }

    /**
     *  Asserts that two objects refer to the same object. If they are not, an [AssertionError] is thrown with the given message.
     */
    inline fun assertSame(expected: Any?, actual: Any?, message: () -> String = { "" }) {
        if (isEnabled) {
            doAssertSame(message(), expected, actual)
        }
    }

    /**
     * Asserts that two objects refer to the same object. If they are not, an [AssertionError] is thrown with the given message.
     */
    inline fun assertNotSame(expected: Any?, actual: Any?, message: () -> String = { "" }) {
        if (isEnabled) {
            doAssertNotSame(message(), expected, actual)
        }
    }

    /**
     * Asserts that code is running on main thread. If it is not, an [AssertionError] is thrown.
     */
    inline fun assertMainThread() {
        if (isRunningInRobolectric()) return
        assertSame(
            Looper.getMainLooper(),
            Looper.myLooper()
        ) { "Code run not in main thread!" }
    }

    /**
     * Asserts that code is running on worker thread. If it is not, an [AssertionError] is thrown.
     */
    inline fun assertNotMainThread() {
        if (isRunningInRobolectric()) return
        assertNotSame(
            Looper.getMainLooper(),
            Looper.myLooper()
        ) { "Code run in main thread!" }
    }

    /**
     * For inlining purposes only. No direct calls.
     */
    fun doFail(message: String?, cause: Throwable? = null) = delegate.failAssert(message, cause)

    /**
     * For inlining purposes only. No direct calls.
     */
    fun doAssertEquals(message: String?, expected: Any?, actual: Any?) {
        if (expected == null && actual == null) {
            return
        }
        if (expected != null && expected == actual) {
            return
        }
        doFailNotEquals(message, expected, actual)
    }

    /**
     * For inlining purposes only. No direct calls.
     */
    fun doAssertSame(message: String?, expected: Any?, actual: Any?) {
        if (expected === actual) {
            return
        }
        failNotSame(message, expected, actual);
    }

    /**
     * For inlining purposes only. No direct calls.
     */
    fun doAssertNotSame(message: String?, expected: Any?, actual: Any?) {
        if (expected !== actual) {
            return
        }
        failSame(message)
    }

    private fun doFailNotEquals(message: String?, expected: Any?, actual: Any?) =
        doFail(format(message, expected, actual))

    private fun format(message: String?, expected: Any?, actual: Any?): String {
        val formatted = if (message != null && message != "") "$message " else ""
        val expectedString = expected.toString()
        val actualString = actual.toString()
        return if (expectedString == actualString) {
            "${formatted}expected: ${formatClassAndValue(expected, expectedString)} " +
                "but was: ${formatClassAndValue(actual, actualString)}"
        } else {
            "${formatted}expected:<$expectedString> but was:<$actualString>"
        }
    }

    private fun formatClassAndValue(value: Any?, valueString: String?): String? {
        val className = if (value == null) "null" else value.javaClass.name
        return "$className<$valueString>"
    }

    private fun failSame(message: String?) {
        val formatted = if (message != null && message != "") "$message " else ""
        doFail("${formatted}expected not same")
    }

    private fun failNotSame(message: String?, expected: Any?, actual: Any?) {
        val formatted = if (message != null && message != "") "$message " else ""
        doFail("${formatted}expected same:<$expected> was not:<$actual>")
    }

    fun isRunningInRobolectric(): Boolean {
        return "robolectric" == Build.FINGERPRINT
    }
}

