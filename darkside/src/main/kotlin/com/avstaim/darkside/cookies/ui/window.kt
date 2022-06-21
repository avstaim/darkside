package com.avstaim.darkside.cookies.ui

import android.graphics.Rect
import android.view.View
import com.avstaim.darkside.cookies.Size

inline val View.windowHeight: Size
    get() = Rect()
        .also { getWindowVisibleDisplayFrame(it) }
        .height()
        .let { Size.px(it) }
