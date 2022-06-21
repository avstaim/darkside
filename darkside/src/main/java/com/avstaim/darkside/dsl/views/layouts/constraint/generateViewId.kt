/*
 * Copyright 2019 Louis Cognault Ayeva Derman. Use of this source code is governed by the Apache 2.0 license.
 */

package com.avstaim.darkside.dsl.views.layouts.constraint

import android.view.View
import com.avstaim.darkside.cookies.isMainThread

/**
 * 1. Generates a View id that doesn't collide with AAPT generated ones (`R.id.xxx`),
 * more efficiently than [View.generateViewId] if called on the main thread.
 * 2. Assigns it to the View.
 * 3. Disables instance state saving for increased efficiency.
 * as state can't be restored to a view that has its id changing across Activity restarts.
 * 4. Returns the generated id.
 *
 * Note that unlike [View.generateViewId], this method is backwards compatible, below API 17.
 */
fun View.assignAndGetGeneratedId(): Int = generateViewId().also { generatedId ->
    id = generatedId
    isSaveEnabled = false // New id will be generated, so can't restore saved state.
}

/**
 * If this [View] has a valid id (different from 0/[View.NO_ID]), returns it.
 * Otherwise, calls [assignAndGetGeneratedId], and returns the new assigned id.
 */
val View.existingOrNewId: Int
    get() = id.let { currentId ->
        if (currentId == View.NO_ID) assignAndGetGeneratedId() else currentId
    }

/**
 * Generates a View id that doesn't collide with AAPT generated ones (`R.id.xxx`).
 *
 * Specially **optimized for usage on main thread** to be synchronization free.
 * **Backwards compatible** below API 17.
 */
fun generateViewId(): Int = when {
    isMainThread -> mainThreadLastGeneratedId.also {
        // Decrement here to avoid any collision with other generated ids which are incremented.
        mainThreadLastGeneratedId = (if (it == 1) aaptIdsStart else it) - 1
    }
    else -> View.generateViewId()
}

/** aapt-generated IDs have the high byte nonzero. Clamp to the range under that. */
private const val aaptIdsStart = 0x00FFFFFF
private var mainThreadLastGeneratedId = aaptIdsStart - 1

internal fun generateViewIdCompat() = generateViewId()

