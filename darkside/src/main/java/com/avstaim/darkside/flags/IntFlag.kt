package com.avstaim.darkside.flags

/**
 * Experiment flag that represents an int value.
 */
class IntFlag(key: String, defaultValue: Int) : Flag<Int>(key, defaultValue) {
    override val type = Type.INT
}