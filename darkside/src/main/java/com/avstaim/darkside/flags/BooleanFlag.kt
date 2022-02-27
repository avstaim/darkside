package com.avstaim.darkside.flags

/**
 * Experiment flag that represents boolean value.
 */
class BooleanFlag(key: String, defaultValue: Boolean) : Flag<Boolean?>(key, defaultValue) {

    override val type = Type.BOOLEAN
}
