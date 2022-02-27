package com.avstaim.darkside.flags

/**
 * Experiment flag that represents a float value
 */
class FloatFlag(key: String, defaultValue: Float) : Flag<Float?>(key, defaultValue) {
    override val type = Type.FLOAT
}
