package convertProgramToEvaluationFormat

import defineProgram.BasicNode
import defineProgram.CallNode
import defineProgram.EntryNode
import defineProgram.ExitNode
import defineProgram.INPUT
import defineProgram.Node
import defineProgram.OUTPUT
import defineProgram.ProgramDefinition
import defineProgram.Subroutine
import defineProgram.Transition
import evaluateProgram.SubroutineDefinition
import parseInstruction.SubroutineName
import kotlin.collections.minus

fun ProgramDefinition.toEvaluationFormat(): evaluateProgram.ProgramDefinition {
    val subroutines = this.subroutines.map { it.toEvaluationFormat() }
    val mainSubroutine = subroutines.single { it.name == SubroutineName("main") }
    val otherSubroutines = (subroutines - mainSubroutine).toSet()
    return evaluateProgram.ProgramDefinition(
        mainSubroutine = mainSubroutine,
        otherSubroutines = otherSubroutines,
    )
}

fun Subroutine.toEvaluationFormat(): SubroutineDefinition {
    val entryNode = this.ENTRY_NODE.toEvaluationFormat()
    val exitNode = this.EXIT_NODE.toEvaluationFormat()
    val nodes = this.nodes.map { it.toEvaluationFormat() }.toSet() + entryNode + exitNode

    val transitions = this.transitions.map { it.toEvaluationFormat() }.toSet()
    return SubroutineDefinition(
        name = this.name,
        nodes = nodes,
        transitions = transitions,
    )
}

fun Transition.toEvaluationFormat(): evaluateProgram.Transition {
    val fromNode = this.fromNode.toEvaluationFormat()
    val toNode = this.toNode!!.toEvaluationFormat()
    val conditions = this.conditions.map { it.toEvaluationFormat() }.toSet()
    val actions = this.actions.map { it.toEvaluationFormat() }.toSet()
    return evaluateProgram.Transition(
        fromNode = fromNode,
        toNode = toNode,
        conditions = conditions,
        actions = actions,
    )
}

fun Node.toEvaluationFormat(): evaluateProgram.Node =
    when (this) {
        is BasicNode ->
            evaluateProgram.BasicNode(
                name = this.name,
            )
        is CallNode ->
            evaluateProgram.CallNode(
                name = this.name,
                subroutineName = this.subroutine.name,
            )
        is EntryNode -> evaluateProgram.EntryNode
        is ExitNode -> evaluateProgram.ExitNode
    }

fun Transition.Condition.toEvaluationFormat() =
    when (this) {
        is Transition.OnStack -> {
            when (this.stack) {
                INPUT ->
                    evaluateProgram.Transition.Condition.OnInputStack(
                        conditionValue = this.conditionalValue,
                    )
                else -> TODO()
            }
        }
    }

fun Transition.Action.toEvaluationFormat() =
    when (this) {
        is Transition.PushToStack -> {
            when (this.stack) {
                OUTPUT ->
                    evaluateProgram.Transition.Action.PushToOutputStack(
                        outputValue = this.outputValue,
                    )
                else -> TODO()
            }
        }
    }
