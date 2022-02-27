package com.avstaim.darkside.flags

/**
 * Experiment flag that represents an enum
 * @param <T> type of the enum
</T> */
class EnumFlag<T : Enum<T>>(key: String, val cls: Class<T>, defaultValue: T) :
    Flag<T>(key, defaultValue) {

    override val type = Type.ENUM

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null || javaClass != other.javaClass) {
            return false
        }
        if (!super.equals(other)) {
            return false
        }
        val that = other as EnumFlag<*>
        return cls == that.cls
    }

    override fun hashCode(): Int {
        val result = super.hashCode()
        return 31 * result + cls.hashCode()
    }
}
