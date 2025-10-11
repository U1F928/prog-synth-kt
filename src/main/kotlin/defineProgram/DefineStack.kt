package defineProgram

import kotlin.reflect.KProperty0

sealed interface Stack

sealed interface ReadableStack : Stack

sealed interface WritableStack : Stack

data object INPUT : ReadableStack

data object OUTPUT : WritableStack

data class CustomStack(
    val name: String,
) : ReadableStack,
    WritableStack
