package com.avstaim.darkside.dsl.views

import android.view.View

/**
 * True if layout direction is **left to right**, like in English.
 *
 * This is always true on API 16 and below since layout direction support only came in API 17.
 *
 * @see isRtl
 */
inline val View.isLtr get() = layoutDirection == View.LAYOUT_DIRECTION_LTR

/**
 * True if layout direction is **right to left**, like in Arabic.
 *
 * This is always false on API 16 and below since layout direction support only came in API 17.
 *
 * @see isLtr
 */
inline val View.isRtl get() = !isLtr
