package evaluateProgram

import parseInstruction.NodeName
import parseInstruction.SubroutineName
import java.util.Stack

data class Transition(
    val fromNode: Node,
    val toNode: Node,
    val conditions: Set<Condition>,
    val actions: Set<Action>
){
    sealed interface Condition{
        data class OnInputStack(
            val conditionValue: UByte
        ): Condition
    }

    sealed interface Action{
        data class PushToOutputStack(
            val outputValue: UByte
        ): Action
    }

}

sealed interface Node

data class BasicNode(val name: NodeName): Node
data class EntryNode(val name: NodeName): Node
data class ExitNode(val name: NodeName): Node
data class CallNode(val name: NodeName, val subroutineName: SubroutineName): Node

data class ProgramDefinition(
    val mainSubroutine: SubroutineDefinition,
    val otherSubroutines: Set<SubroutineDefinition>
){
    fun allSubroutines() = otherSubroutines + mainSubroutine
}

data class SubroutineDefinition(
    val name: SubroutineName,
    val nodes: Set<Node>,
    val transitions: Set<Transition>,
){
    fun entryNode() = nodes.single{it is EntryNode}
    fun exitNode() = nodes.single{it is ExitNode}
}


data class SubroutineState(
    val subroutineName: SubroutineName,
    val currentNode: Node,
)

data class ProgramState(
    val subroutineStack: List<SubroutineState>,
    val outputValues: List<UByte>,
    val inputValues: List<UByte>
)