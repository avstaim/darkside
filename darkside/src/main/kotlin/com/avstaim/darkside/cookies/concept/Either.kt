@file:Suppress("unused")

package com.avstaim.darkside.cookies.concept

sealed class Either<L, R> {

    class Left<L, R>(val left: L) : Either<L, R>() {
        override fun toString(): String = "Left $left"
    }

    class Right<L, R>(val right: R) : Either<L, R>() {
        override fun toString(): String = "Right $right"
    }

    inline infix fun <Rp> mapRight(transform: (R) -> (Either<L, Rp>)): Either<L, Rp> {
        return when (this) {
            is Left<L, R> -> Left(left)
            is Right<L, R> -> transform(right)
        }
    }

    inline infix fun <Lp> mapLeft(transform: (L) -> (Either<Lp, R>)): Either<Lp, R> {
        return when (this) {
            is Left<L, R> -> transform(left)
            is Right<L, R> -> Right(right)
        }
    }

    inline fun <Rp, Lp> map(
        transformRight: (R) -> (Either<Lp, Rp>),
        transformLeft: (L) -> (Either<Lp, Rp>),
    ): Either<Lp, Rp> {
        return when (this) {
            is Left<L, R> -> transformLeft(left)
            is Right<L, R> -> transformRight(right)
        }
    }

    inline fun <T> fold(
        onLeft: (left: L) -> T,
        onRight: (right: R) -> T,
    ): T = when (this) {
            is Left -> onLeft(left)
            is Right -> onRight(right)
        }

    companion object {
        inline fun <T, reified L, reified R> T.either(): Either<L, R> =
            when(this) {
                is L -> Left(this)
                is R -> Right(this)
                else -> error("$this is not ${R::class} of ${L::class}")
            }
    }
}
