/*
 * Copyright 2019 Louis Cognault Ayeva Derman. Use of this source code is governed by the Apache 2.0 license.
 */

package com.avstaim.darkside.cookies

import android.os.Looper

/** This main looper cache avoids synchronization overhead when accessed repeatedly. */
val mainLooper: Looper = Looper.getMainLooper() ?: error("No main looper")
val mainThread: Thread = mainLooper.thread

val isMainThread: Boolean inline get() = mainThread === Thread.currentThread()

@PublishedApi
internal val currentThread: Any? inline get() = Thread.currentThread()
