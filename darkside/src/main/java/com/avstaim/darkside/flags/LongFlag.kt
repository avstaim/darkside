package com.avstaim.darkside.flags

/**
 * Experiment flag that represents a long value.
 */
class LongFlag(key: String, defaultValue: Long) : Flag<Long>(key, defaultValue) {
    override val type = Type.LONG
}
