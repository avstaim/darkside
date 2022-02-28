package com.avstaim.darkside.dsl.views.layouts.constraint

import android.os.Build
import android.view.View

internal fun generateViewIdCompat() =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
        View.generateViewId()
    } else {
        (1..Int.MAX_VALUE).random()
    }