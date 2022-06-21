package com.avstaim.darkside.cookies

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager

inline val AccessibilityService.windowManager get() = getSystemService(Context.WINDOW_SERVICE) as WindowManager
inline val Context.windowManager get() = getSystemService(Context.WINDOW_SERVICE) as WindowManager
inline val View.windowManager get() = context.windowManager
inline val Context.layoutInflater: LayoutInflater get() = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
inline val View.layoutInflater: LayoutInflater get() = context.layoutInflater
