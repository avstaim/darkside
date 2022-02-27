@file:Suppress("NOTHING_TO_INLINE")

package com.avstaim.darkside.dsl.views

import android.content.Context

/**
 * Simple marker interface to create views.
 */
interface ViewBuilder {
    val ctx: Context
}

class SimpleViewBuilder(override val ctx: Context) : ViewBuilder

inline fun Context.viewBuilder(): ViewBuilder = SimpleViewBuilder(this)
