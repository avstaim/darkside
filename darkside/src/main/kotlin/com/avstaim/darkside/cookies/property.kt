package com.avstaim.darkside.cookies

import kotlin.reflect.KProperty0

fun <T> KProperty0<T>.getOrIllegalArg(): T = try {
    get()
} catch (e: UninitializedPropertyAccessException) {
    illegalArg("$name required")
}
