package com.avstaim.darkside.cookies

import android.app.Activity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope

val Activity.lifecycleOwner: LifecycleOwner?
    get() = this as? LifecycleOwner

val Activity.coroutineScope: CoroutineScope
    get() = lifecycleOwner?.lifecycleScope ?: error("Using an Activity that is not LifecycleOwner: $this")
