package defineProgram

infix fun Subroutine.from(fromNode: Node): Transition {
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

fun Transition.to(toNode: Node): Transition {
    this.toNode = toNode
    return this
}

data class DefinedNewConditionalValue(
    val transition: Transition,
    val conditionalValue: UByte,
)

fun Transition.on(conditionalValue: Int) =
    DefinedNewConditionalValue(
        transition = this,
        conditionalValue = conditionalValue.toUByte(),
    )

fun Transition.onInput(conditionalValue: Int): Transition = this.on(conditionalValue).at(INPUT)

fun DefinedNewConditionalValue.at(stack: ReadableStack): Transition {
    val newCondition =
        when (stack) {
            INPUT -> Transition.OnInputStack(this.conditionalValue)
            is CustomStack -> TODO()
        }
    this.transition.conditions.add(newCondition)

    return this.transition
}

fun Transition.on(
    stack: ReadableStack,
    conditionalValue: Int,
) {
}

data class DefinedNewOutputValue(
    val transition: Transition,
    val outputValue: UByte,
)

fun Transition.push(outputValue: Int): DefinedNewOutputValue =
    DefinedNewOutputValue(
        transition = this,
        outputValue = outputValue.toUByte(),
    )

fun Transition.pushOutput(outputValue: Int): Transition = this.push(outputValue).to(OUTPUT)

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
