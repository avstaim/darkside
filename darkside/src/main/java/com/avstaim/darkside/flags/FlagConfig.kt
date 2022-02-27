package com.avstaim.darkside.flags

import androidx.annotation.AnyThread

/**
 * Provides parameters for experiments.
 */
class FlagConfig {

    /**
     * Retrieve a value associated with supplied experiment flag.
     *
     * This method is intended to be **overridden** in client apps to provide actual flag values.
     *
     * @return concrete value for this flag
     */
    @AnyThread
    fun <T> getValue(flag: Flag<T>): T = flag.defaultValue
}
