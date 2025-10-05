package evaluateProgram

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import evaluateProgram.EntryNode
import parseInstruction.NodeName
import parseInstruction.SubroutineName
import evaluateProgram.Transition
import kotlin.contracts.contract

class EvaluateProgramTest {

    @Test
    fun `evaluate program with 2 nodes and 1 conditional transition - pushing to the output`(){
        // given
        val node1 = EntryNode(NodeName("node1"))
        val node2 = ExitNode(NodeName("node2"))

        val conditionalValue = 123.toUByte()
        val outputValue = 234.toUByte()
        val transition = Transition(
            fromNode = node1,
            toNode = node2,
            conditions = setOf(
                Transition.Condition.OnInputStack( conditionValue = conditionalValue )
            ),
            actions = setOf(
                Transition.Action.PushToOutputStack(outputValue = outputValue)
            ),
        )

        val mainSubroutineDefinition = SubroutineDefinition(
            name = SubroutineName("main"),
            nodes = setOf(node1, node2),
            transitions = setOf(transition),
        )

        val programDefinition = ProgramDefinition(
            mainSubroutine = mainSubroutineDefinition,
            otherSubroutines = emptySet()
        )

        val programState = ProgramState(
            subroutineStack = listOf(
                SubroutineState(
                    subroutineName = mainSubroutineDefinition.name,
                    currentNode = mainSubroutineDefinition.entryNode()
                )
            ),
            outputValues = emptyList(),
            inputValues = listOf(conditionalValue)
        )

        val evalConfig = EvaluationConfig(skipInputValueWithNoMatchingTransition = false)

        // when
        val result = getNextProgramState(
            programDefinition = programDefinition,
            programState = programState,
            evaluationConfig = evalConfig
        )
        // then
        assertThat(result).isEqualTo(
            ProgramState(
                subroutineStack = listOf(
                    SubroutineState(
                        subroutineName = mainSubroutineDefinition.name,
                        currentNode = node2
                    )
                ),
                outputValues = listOf(outputValue),
                inputValues = emptyList()
            )
        )
    }
}
