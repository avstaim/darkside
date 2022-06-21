@file:Suppress("unused")

package com.avstaim.darkside.cookies.concept

/**
 * A container object which may or may not contain an object instance of class `<T>`.
 *
 * @param T Type of the stored object.
 */
class Optional<T> {

    private val value: T?

    private constructor() {
        value = null
    }

    private constructor(value: T) {
        this.value = value
    }

    /**
     * If a value is present in this [Optional], returns the value,
     * otherwise throws [NoSuchElementException].
     *
     * @return the non-null value held by this [Optional]
     * @see Optional.isPresent
     */
    fun get(): T = value ?: throw NoSuchElementException("No value present")

    /**
     * If a value is present in this [Optional], returns the value,
     * otherwise returns `null`.
     *
     * @return the nullable value held by this [Optional]
     * @see Optional.isPresent
     */
    fun getOrNull(): T? = value

    /**
     * If a value is present in this [Optional], returns the value,
     * otherwise returns [default].
     *
     * @param default value to use when optional is missing
     * @return the non-null value held by this [Optional] or [default]
     * @see Optional.isPresent
     */
    fun getOrDefault(default: T): T = value ?: default

    val isPresent: Boolean
        get() = value != null

    /**
     * If a value is present, invoke the specified consumer with the value,
     * otherwise do nothing.
     *
     * @param consumer block to be executed if a value is present
     */
    fun ifPresent(consumer: (T) -> Unit) {
        value?.let(consumer)
    }

    /**
     * If a value is present, performs the given action with the value,
     * otherwise performs the given empty-based action.
     *
     * @param action the action to be performed, if a value is present
     * @param emptyAction the empty-based action to be performed, if no value is present empty-based action is `null`.
     */
    fun fold(ifPresent: (T) -> Unit, orElse: () -> Unit) =
        value?.let(ifPresent) ?: orElse()

    /**
     * If a value is present, apply the provided mapping function to it,
     * and if the result is non-null, return an `Optional` describing the
     * result.  Otherwise return an empty `Optional`.
     *
     * @param U The type of the result of the mapping function
     * @param mapper a mapping function to apply to the value, if present
     * @return an `Optional` describing the result of applying a mapping
     * function to the value of this `Optional`, if a value is present,
     * otherwise an empty `Optional`
     * @apiNote This method supports post-processing on optional values, without
     * the need to explicitly check for a return status.  For example, the
     * following code traverses a stream of file names, selects one that has
     * not yet been processed, and then opens that file, returning an
     * `Optional<FileInputStream>`:
     *
     * <pre>`Optional<FileInputStream> fis =
     * names.stream().filter(name -> !isProcessedYet(name))
     * .findFirst()
     * .map(name -> new FileInputStream(name));
    `</pre> *
     *
     *
     * Here, `findFirst` returns an `Optional<String>`, and then
     * `map` returns an `Optional<FileInputStream>` for the desired
     * file if one exists.
     */
    fun <U> map(mapper: (T) -> U): Optional<U> =
        value?.let {
            of(mapper(it))
        } ?: empty()


    fun <U> flatMap(mapper: (T) -> Optional<U>): Optional<U> =
        value?.let {
            mapper(it)
        } ?: empty()

    /**
     * Return the value if present, otherwise return `other`.
     *
     * @param other the value to be returned if there is no value present, may
     * be null
     * @return the value, if present, otherwise `other`
     */
    fun orElse(other: T): T = value ?: other

    /**
     * If a value is present, and the value matches the given predicate,
     * return an `Optional` describing the value, otherwise return an
     * empty `Optional`.
     *
     * @param predicate a predicate to apply to the value, if present
     * @return an `Optional` describing the value of this `Optional`
     * if a value is present and the value matches the given predicate,
     * otherwise an empty `Optional`
     * @throws NullPointerException if the predicate is null
     */
    fun filter(predicate: (T) -> Boolean): Optional<T> =
        value?.let {
            if (predicate(it)) this else empty()
        } ?: this

    /**
     * Return the value if present, otherwise return null.
     *
     * @return the value, if present, otherwise null
     */
    fun orNull(): T? = value

    companion object {

        private val EMPTY: Optional<*> = Optional<Any>()

        /**
         * Returns an `FeatureOptional` with the specified present non-null value.
         *
         * @param T the class of the value
         * @param value the value to be present, which must be non-null
         * @return an `FeatureOptional` with the value present
         */
        fun <T> of(value: T): Optional<T> = Optional(value)

        /**
         * Returns an `FeatureOptional` describing the specified value, if non-null,
         * otherwise returns an empty `FeatureOptional`.
         *
         * @param T the class of the value
         * @param value the possibly-null value to describe
         * @return an `FeatureOptional` with a present value if the specified value
         * is non-null, otherwise an empty `FeatureOptional`
         */
        fun <T> ofNullable(value: T?): Optional<T> {
            return if (value == null) empty() else of(value)
        }

        /**
         * Returns an empty `FeatureOptional` instance.  No value is present for this
         * Optional.
         *
         * @param T Type of the non-existent value
         * @return an empty `FeatureOptional`
         * @apiNote Though it may be tempting to do so, avoid testing if an object
         * is empty by comparing with `==` against instances returned by
         * `Option.empty()`. There is no guarantee that it is a singleton.
         * Instead, use [isPresent].
         */
        fun <T> empty(): Optional<T> {
            return EMPTY as Optional<T>
        }
    }
}
