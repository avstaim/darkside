@file:Suppress("unused")

package com.avstaim.darkside.dsl.views.util

import android.animation.LayoutTransition
import android.view.ViewGroup

inline var ViewGroup.animateLayoutChanges: Boolean
    get() = layoutTransition != null
    set(value) {
        layoutTransition = if (value) LayoutTransition() else null
    }
