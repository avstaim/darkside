@file:Suppress("unused")

package com.avstaim.darkside.cookies

import android.os.Handler
import android.os.Looper
import androidx.annotation.WorkerThread

private val uiHandler = Handler(Looper.getMainLooper())

@WorkerThread
fun postOnUiThread(callback: () -> Unit) {
    uiHandler.post(callback)
}
