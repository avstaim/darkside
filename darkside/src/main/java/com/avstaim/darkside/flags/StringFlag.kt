package com.avstaim.darkside.flags

/**
 * Experiment flag that represents a string
 */
class StringFlag(key: String, defaultValue: String) : Flag<String?>(key, defaultValue) {
    override val type = Type.STRING
}
