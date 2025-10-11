package evaluateProgram

import parseInstruction.NodeName
import parseInstruction.SubroutineName

data class Transition(
    val fromNode: Node,
    val toNode: Node,
    val conditions: Set<Condition>,
    val actions: Set<Action>,
) {
    sealed interface Condition {
        data class OnInputStack(
            val conditionValue: UByte,
        ) : Condition
    }

    sealed interface Action {
        data class PushToOutputStack(
            val outputValue: UByte,
        ) : Action
    }
}

sealed interface Node

data class BasicNode(
    val name: NodeName,
) : Node

data object EntryNode : Node

data object ExitNode : Node

data class CallNode(
    val name: NodeName,
    val subroutineName: SubroutineName,
) : Node

data class ProgramDefinition(
    val mainSubroutine: SubroutineDefinition,
    val otherSubroutines: Set<SubroutineDefinition>,
) {
    fun allSubroutines() = otherSubroutines + mainSubroutine
}

data class SubroutineDefinition(
    val name: SubroutineName,
    val nodes: Set<Node>,
    val transitions: Set<Transition>,
) {
    fun entryNode() = nodes.single { it is EntryNode }

    fun exitNode() = nodes.single { it is ExitNode }
}

data class SubroutineState(
    val subroutineName: SubroutineName,
    val currentNode: Node,
)

sealed interface ProgramState {
    val subroutineStack: List<SubroutineState>
    val outputValues: List<UByte>
    val inputValues: List<UByte>
}

data class RunningProgramState(
    override val subroutineStack: List<SubroutineState>,
    override val outputValues: List<UByte>,
    override val inputValues: List<UByte>,
) : ProgramState

fun RunningProgramState.toFinishedState(): FinishedProgramState =
    FinishedProgramState(
        subroutineStack = this.subroutineStack,
        outputValues = this.outputValues,
        inputValues = this.inputValues,
    )

data class FinishedProgramState(
    override val subroutineStack: List<SubroutineState>,
    override val outputValues: List<UByte>,
    override val inputValues: List<UByte>,
) : ProgramState
