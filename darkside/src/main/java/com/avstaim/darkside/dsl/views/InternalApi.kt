package com.avstaim.darkside.dsl.views

/**
 * Marks code that should not be used from outside world and made public for inlining purposes only.
 */
@Retention(value = AnnotationRetention.BINARY)
@RequiresOptIn(level = RequiresOptIn.Level.ERROR)
internal annotation class InternalApi
