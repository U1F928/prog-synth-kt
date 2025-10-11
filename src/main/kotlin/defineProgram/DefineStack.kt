package defineProgram

import parseInstruction.StackName
import kotlin.reflect.KProperty

fun ProgramDefinition.STACK() =
    StackDelegate(
        stacks = this.stacks,
    )

class StackDelegate(
    private val stacks: MutableList<Stack>,
) {
    operator fun provideDelegate(
        thisRef: Any?,
        property: KProperty<*>,
    ): StackDelegate {
        val stack = CustomStack(name = StackName(property.name))
        stacks.add(stack)
        return this
    }

    operator fun getValue(
        thisRef: Any?,
        property: KProperty<*>,
    ): CustomStack =
        stacks
            .filterIsInstance<CustomStack>()
            .single { it.name == StackName(property.name) }
}

sealed interface Stack

sealed interface ReadableStack : Stack

sealed interface WritableStack : Stack

data object INPUT : ReadableStack

data object OUTPUT : WritableStack

data class CustomStack(
    val name: StackName,
) : ReadableStack,
    WritableStack
