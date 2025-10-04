package result

import result.Ok
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

sealed interface Result<out S, out E>

@JvmInline
value class Ok<out S>(
    val value: S,
) : Result<S, Nothing>

@JvmInline
value class Err<out E>(
    val value: E,
) : Result<Nothing, E>

fun <T> T.toOk() = Ok(this)

fun <T> T.toErr() = Err(this)

fun <S1, E, S2> Result<S1, E>.mapOk(transform: (S1) -> S2): Result<S2, E> =
    when (this) {
        is Ok -> Ok(transform(this.value))
        is Err -> this
    }

fun <S1, E, S2> Result<S1, E>.flatMapOk(transform: (S1) -> Result<S2, E>): Result<S2, E> =
    when (this) {
        is Ok -> {
            val newResult = transform(this.value)
            when (newResult) {
                is Ok -> Ok(newResult.value)
                is Err -> Err(newResult.value)
            }
        }
        is Err -> this
    }

fun <S, E1, E2> Result<S, E1>.mapErr(transform: (E1) -> E2): Result<S, E2> =
    when (this) {
        is Ok -> this
        is Err -> Err(transform(this.value))
    }

fun <S, E1, E2> Result<S, E1>.flatMapErr(transform: (E1) -> Result<S, E2>): Result<S, E2> =
    when (this) {
        is Ok -> this
        is Err -> {
            val newResult = transform(this.value)
            when (newResult) {
                is Ok -> Ok(newResult.value)
                is Err -> Err(newResult.value)
            }
        }
    }
