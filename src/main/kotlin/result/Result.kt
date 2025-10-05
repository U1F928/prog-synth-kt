package result

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

sealed interface Result<out S, out E>

@JvmInline
value class Ok<out S>(
    val v: S,
) : Result<S, Nothing>

@JvmInline
value class Err<out E>(
    val v: E,
) : Result<Nothing, E>

fun <T> ok(value: T) = Ok(value)

fun ok() = Ok(Unit)

fun <T> err(value: T) = Err(value)

fun err() = Err(Unit)

fun <T> T.toOk() = Ok(this)

fun <T> T.toErr() = Err(this)

fun <S : U, E : U, U> Result<S, E>.v(): U =
    when (this) {
        is Ok -> this.v
        is Err -> this.v
    }

@OptIn(ExperimentalContracts::class)
fun <S, E> Result<S, E>.isOk(): Boolean {
    contract {
        returns(true) implies (this@isOk is Ok)
        returns(false) implies (this@isOk is Err)
    }
    return when (this) {
        is Ok -> true
        is Err -> false
    }
}

@OptIn(ExperimentalContracts::class)
fun <S, E> Result<S, E>.isErr(): Boolean {
    contract {
        returns(true) implies (this@isErr is Err)
        returns(false) implies (this@isErr is Ok)
    }
    return when (this) {
        is Ok -> false
        is Err -> true
    }
}

inline fun <S, reified E> Result<S, E>.getErrOrThrow(): E {
    check(this is Err<E>) {
        """
        $this is not an Err<${E::class.qualifiedName}> result!   
        """.trimIndent()
    }
    return this.v
}

inline fun <reified S, E> Result<S, E>.getOrThrow(): S {
    check(this is Ok<S>) {
        """
        $this is not an Ok<${S::class.qualifiedName}> result!   
        """.trimIndent()
    }
    return this.v
}

fun <S1, E, S2> Result<S1, E>.map(transform: (S1) -> S2): Result<S2, E> =
    when (this) {
        is Ok -> Ok(transform(this.v))
        is Err -> this
    }

fun <S1, E, S2> Result<S1, E>.flatMap(transform: (S1) -> Result<S2, E>): Result<S2, E> =
    when (this) {
        is Ok -> {
            val newResult = transform(this.v)
            when (newResult) {
                is Ok -> Ok(newResult.v)
                is Err -> Err(newResult.v)
            }
        }
        is Err -> this
    }

fun <S, E1, E2> Result<S, E1>.mapErr(transform: (E1) -> E2): Result<S, E2> =
    when (this) {
        is Ok -> this
        is Err -> Err(transform(this.v))
    }

fun <S, E1, E2> Result<S, E1>.flatMapErr(transform: (E1) -> Result<S, E2>): Result<S, E2> =
    when (this) {
        is Ok -> this
        is Err -> {
            val newResult = transform(this.v)
            when (newResult) {
                is Ok -> Ok(newResult.v)
                is Err -> Err(newResult.v)
            }
        }
    }

fun <S, E> List<Result<S, E>>.filterIsOk(): List<S> =
    this.mapNotNull { element ->
        when (element) {
            is Ok -> element.v
            is Err -> null
        }
    }
