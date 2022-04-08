package com.avstaim.darkside.flags

import org.json.JSONArray

sealed class Flag<T>(val key: String, val defaultValue: T) {

    /**
     * A way for client applications to determine type of a flag
     *
     * @return enum value that corresponds to certain implementation of [Flag]
     */
    abstract val type: Type

    abstract fun deserialize(value: String?): T
    abstract fun serialize(value: T?): String?

    fun serializeWithKey(value: T) = key to serialize(value)

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

    override fun hashCode(): Int = key.hashCode()

    enum class Type {
        BOOLEAN,
        INT,
        LONG,
        FLOAT,
        STRING,
        STRING_LIST,
        ENUM,
    }
}

class BooleanFlag(key: String, defaultValue: Boolean) : Flag<Boolean>(key, defaultValue) {

    override val type: Type = Type.BOOLEAN

    override fun deserialize(value: String?): Boolean =
        when (value) {
            "0" -> false
            "1" -> true
            else -> defaultValue
        }

    override fun serialize(value: Boolean?): String? =
        when (value) {
            null -> null
            true -> "1"
            false -> "0"
        }
}

class IntFlag(key: String, defaultValue: Int) : Flag<Int>(key, defaultValue) {

    override val type: Type = Type.INT

    override fun deserialize(value: String?): Int = value?.toIntOrNull() ?: defaultValue
    override fun serialize(value: Int?): String? = value?.toString()
}

class LongFlag(key: String, defaultValue: Long) : Flag<Long>(key, defaultValue) {

    override val type: Type = Type.LONG

    override fun deserialize(value: String?): Long = value?.toLongOrNull() ?: defaultValue
    override fun serialize(value: Long?): String? = value?.toString()
}

class FloatFlag(key: String, defaultValue: Float) : Flag<Float>(key, defaultValue) {

    override val type: Type = Type.FLOAT

    override fun deserialize(value: String?): Float = value?.toFloatOrNull() ?: defaultValue
    override fun serialize(value: Float?): String? = value?.toString()
}

class StringFlag(key: String, defaultValue: String) : Flag<String>(key, defaultValue) {

    override val type: Type = Type.STRING

    override fun deserialize(value: String?): String = value ?: defaultValue
    override fun serialize(value: String?): String? = value
}

class EnumFlag<T : Enum<T>>(
    key: String,
    defaultValue: T,
    val values: Array<T>,
) : Flag<T>(key, defaultValue) {

    override val type: Type = Type.ENUM

    override fun deserialize(value: String?): T {
        val index = value?.toIntOrNull() ?: return defaultValue
        if (0 <= index && index < values.size) {
            return values[index]
        }
        return defaultValue
    }

    override fun serialize(value: T?): String? = value?.ordinal?.toString()
}

class StringListFlag(key: String, defaultValue: List<String>) : Flag<List<String>>(
    key,
    defaultValue,
) {

    override val type: Type = Type.STRING_LIST

    override fun deserialize(value: String?): List<String> {
        if (value == null) {
            return defaultValue
        }
        val data: List<String>
        try {
            data = JSONArray(value).let { jsonArray ->
                0.until(jsonArray.length()).map { i -> jsonArray.optString(i) }
            }
        } catch (e: Exception) {
            return defaultValue
        }

        return data
    }

    override fun serialize(value: List<String>?): String? {
        if (value == null) { return null }
        return JSONArray(value).toString()
    }
}
