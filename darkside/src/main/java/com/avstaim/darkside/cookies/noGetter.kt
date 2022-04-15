package com.avstaim.darkside.cookies

@PublishedApi
internal const val NO_GETTER = "Property does not have a getter"

fun noGetter(): Nothing = throw UnsupportedOperationException("Property does not have a getter")
