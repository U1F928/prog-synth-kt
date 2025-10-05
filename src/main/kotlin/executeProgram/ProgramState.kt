package executeProgram

import parseInstruction.NodeName
import parseInstruction.SubroutineName
import java.util.Stack

data class Transition(
    val fromNode: Node,
    val toNode: Node,
    val conditions: Set<Condition>
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


data class SubroutineState(
    val name: SubroutineName,
    val currentNode: Node,
    val nodes: Set<Node>,
    val transitions: Set<Transition>
)


data class ProgramState(
    val subroutineStack: Stack<SubroutineState>,
    val outputValues: List<UByte>,
    val inputValues: List<UByte>
)