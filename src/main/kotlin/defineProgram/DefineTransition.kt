package defineProgram

infix fun Subroutine.FROM(fromNode: Node): Transition {
    val transition =
        Transition(
            fromNode = fromNode,
            toNode = null,
            conditions = mutableListOf(),
            actions = mutableListOf(),
        )

    this.transitions.add(transition)

    return transition
}

infix fun Transition.GOTO(toNode: Node): Transition {
    this.toNode = toNode
    return this
}

data class DefinedNewConditionalValue(
    val transition: Transition,
    val conditionalValue: UByte,
)

fun Transition.ON(
    stack: ReadableStack,
    conditionalValue: Int,
): Transition {
    val newCondition =
        Transition.OnStack(
            stack = stack,
            conditionalValue = conditionalValue.toUByte(),
        )

    this.conditions.add(newCondition)

    return this
}

fun Transition.PUSH(
    stack: WritableStack,
    outputValue: Int,
): Transition {
    val newCondition =
        Transition.PushToStack(
            stack = stack,
            outputValue = outputValue.toUByte(),
        )

    this.actions.add(newCondition)

    return this
}

data class DefinedNewOutputValue(
    val transition: Transition,
    val outputValue: UByte,
)

fun DefinedNewOutputValue.to(stack: WritableStack): Transition {
    val newAction =
        Transition.PushToStack(
            stack = stack,
            outputValue = outputValue,
        )

    this.transition.actions.add(newAction)
    return this.transition
}

data class Transition(
    val fromNode: Node,
    var toNode: Node?,
    val conditions: MutableList<Condition>,
    val actions: MutableList<Action>,
) {
    sealed interface Condition

    data class OnStack(
        val stack: ReadableStack,
        val conditionalValue: UByte,
    ) : Condition

    sealed interface Action

    data class PushToStack(
        val stack: WritableStack,
        val outputValue: UByte,
    ) : Action
}
