@file:Suppress("unused")

package com.avstaim.darkside.cookies

fun String?.nullIfEmpty(): String? = this?.takeIf { it.isNotEmpty() }

fun String?.emptyIfNull(): String = this ?: ""

fun String.removeNonAscii(): String = replace("[^\\x00-\\x7F]".toRegex(), "")
