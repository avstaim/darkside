@file:Suppress("unused")

package com.avstaim.darkside.cookies

import java.util.Random

fun IntRange.random() =
    Random().nextInt((endInclusive + 1) - start) + start

fun <T> List<T>.randomElement(): T = (0 until size).random().let { this[it] }
