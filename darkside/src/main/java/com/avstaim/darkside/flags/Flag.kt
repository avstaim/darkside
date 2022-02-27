package com.avstaim.darkside.flags

/**
 * Abstract parent to all the experiment flags
 *
 * @param <T> A type of value this flag should return by corresponding get*Value of [FlagConfig]
</T> */
abstract class Flag<T> internal constructor(
    /**
     * A unique identifier for a flag
     * A common usage - a key in a config response json
     * This is used in [.equals] implementation
     */
    val key: String,

    /**
     * A placeholder if flag is absent.
     * A preference manager may use this value
     */
    val defaultValue: T,
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null || javaClass != other.javaClass) {
            return false
        }
        val that = other as Flag<*>
        return key == that.key
    }

    override fun hashCode(): Int {
        return key.hashCode()
    }

    /**
     * A way for client applications to determine type of a flag
     *
     * @return enum value that corresponds to certain implementation of [Flag]
     */
    abstract val type: Type

    /**
     * Indicates implementation of [Flag]
     */
    enum class Type {
        INT, LONG, BOOLEAN, ENUM, STRING, FLOAT
    }
}