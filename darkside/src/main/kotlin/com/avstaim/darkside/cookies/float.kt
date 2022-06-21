@file:Suppress("unused")

package com.avstaim.darkside.cookies

import kotlin.math.abs

infix fun Double.eq(other: Double) = abs(this - other) < 0.000001
infix fun Float.eq(other: Float) = abs(this - other) < 0.000001f

infix fun Double.neq(other: Double) = (this eq other).not()
infix fun Float.neq(other: Float) = (this eq other).not()
