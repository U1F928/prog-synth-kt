package evaluateProgram

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import parseInstruction.NodeName
import parseInstruction.SubroutineName
import kotlin.collections.emptyList

class EvaluateProgramTest {
    @Nested
    inner class `test getNextProgramState` {
        @Test
        fun `2 nodes and 1 conditional transition`() {
            // given
            val node1 = EntryNode(NodeName("node1"))
            val node2 = ExitNode(NodeName("node2"))

            val conditionalValue = 123.toUByte()
            val transition =
                Transition(
                    fromNode = node1,
                    toNode = node2,
                    conditions =
                        setOf(
                            Transition.Condition.OnInputStack(conditionValue = conditionalValue),
                        ),
                    actions = emptySet(),
                )

            val mainSubroutineDefinition =
                SubroutineDefinition(
                    name = SubroutineName("main"),
                    nodes = setOf(node1, node2),
                    transitions = setOf(transition),
                )

            val programDefinition =
                ProgramDefinition(
                    mainSubroutine = mainSubroutineDefinition,
                    otherSubroutines = emptySet(),
                )

            val programState =
                ProgramState(
                    subroutineStack =
                        listOf(
                            SubroutineState(
                                subroutineName = mainSubroutineDefinition.name,
                                currentNode = mainSubroutineDefinition.entryNode(),
                            ),
                        ),
                    outputValues = emptyList(),
                    inputValues = listOf(conditionalValue),
                )

            val evalConfig = EvaluationConfig(skipInputValueWithNoMatchingTransition = false)

            // when
            val result =
                getNextProgramState(
                    programDefinition = programDefinition,
                    programState = programState,
                    evaluationConfig = evalConfig,
                )
            // then
            assertThat(result).isEqualTo(
                ProgramState(
                    subroutineStack =
                        listOf(
                            SubroutineState(
                                subroutineName = mainSubroutineDefinition.name,
                                currentNode = node2,
                            ),
                        ),
                    outputValues = emptyList(),
                    inputValues = emptyList(),
                ),
            )
        }

        @Test
        fun `2 nodes and 1 unconditional transition`() {
            // given
            val node1 = EntryNode(NodeName("node1"))
            val node2 = ExitNode(NodeName("node2"))

            val transition =
                Transition(
                    fromNode = node1,
                    toNode = node2,
                    conditions = emptySet(),
                    actions = emptySet(),
                )

            val mainSubroutineDefinition =
                SubroutineDefinition(
                    name = SubroutineName("main"),
                    nodes = setOf(node1, node2),
                    transitions = setOf(transition),
                )

            val programDefinition =
                ProgramDefinition(
                    mainSubroutine = mainSubroutineDefinition,
                    otherSubroutines = emptySet(),
                )

            val programState =
                ProgramState(
                    subroutineStack =
                        listOf(
                            SubroutineState(
                                subroutineName = mainSubroutineDefinition.name,
                                currentNode = mainSubroutineDefinition.entryNode(),
                            ),
                        ),
                    outputValues = emptyList(),
                    inputValues = emptyList(),
                )

            val evalConfig = EvaluationConfig(skipInputValueWithNoMatchingTransition = false)

            // when
            val result =
                getNextProgramState(
                    programDefinition = programDefinition,
                    programState = programState,
                    evaluationConfig = evalConfig,
                )
            // then
            assertThat(result).isEqualTo(
                ProgramState(
                    subroutineStack =
                        listOf(
                            SubroutineState(
                                subroutineName = mainSubroutineDefinition.name,
                                currentNode = node2,
                            ),
                        ),
                    outputValues = emptyList(),
                    inputValues = emptyList(),
                ),
            )
        }

        @Test
        fun `2 nodes and 1 unconditional transition which pushes to the output`() {
            // given
            val node1 = EntryNode(NodeName("node1"))
            val node2 = ExitNode(NodeName("node2"))

            val outputValue = 234.toUByte()
            val transition =
                Transition(
                    fromNode = node1,
                    toNode = node2,
                    conditions = emptySet(),
                    actions =
                        setOf(
                            Transition.Action.PushToOutputStack(outputValue = outputValue),
                        ),
                )

            val mainSubroutineDefinition =
                SubroutineDefinition(
                    name = SubroutineName("main"),
                    nodes = setOf(node1, node2),
                    transitions = setOf(transition),
                )

            val programDefinition =
                ProgramDefinition(
                    mainSubroutine = mainSubroutineDefinition,
                    otherSubroutines = emptySet(),
                )

            val programState =
                ProgramState(
                    subroutineStack =
                        listOf(
                            SubroutineState(
                                subroutineName = mainSubroutineDefinition.name,
                                currentNode = mainSubroutineDefinition.entryNode(),
                            ),
                        ),
                    outputValues = emptyList(),
                    inputValues = emptyList(),
                )

            val evalConfig = EvaluationConfig(skipInputValueWithNoMatchingTransition = false)

            // when
            val result =
                getNextProgramState(
                    programDefinition = programDefinition,
                    programState = programState,
                    evaluationConfig = evalConfig,
                )
            // then
            assertThat(result).isEqualTo(
                ProgramState(
                    subroutineStack =
                        listOf(
                            SubroutineState(
                                subroutineName = mainSubroutineDefinition.name,
                                currentNode = node2,
                            ),
                        ),
                    outputValues = listOf(outputValue),
                    inputValues = emptyList(),
                ),
            )
        }

        @Test
        fun `2 nodes and 1 conditional transition which pushes to the output`() {
            // given
            val node1 = EntryNode(NodeName("node1"))
            val node2 = ExitNode(NodeName("node2"))

            val conditionalValue = 123.toUByte()
            val outputValue = 234.toUByte()
            val transition =
                Transition(
                    fromNode = node1,
                    toNode = node2,
                    conditions =
                        setOf(
                            Transition.Condition.OnInputStack(conditionValue = conditionalValue),
                        ),
                    actions =
                        setOf(
                            Transition.Action.PushToOutputStack(outputValue = outputValue),
                        ),
                )

            val mainSubroutineDefinition =
                SubroutineDefinition(
                    name = SubroutineName("main"),
                    nodes = setOf(node1, node2),
                    transitions = setOf(transition),
                )

            val programDefinition =
                ProgramDefinition(
                    mainSubroutine = mainSubroutineDefinition,
                    otherSubroutines = emptySet(),
                )

            val programState =
                ProgramState(
                    subroutineStack =
                        listOf(
                            SubroutineState(
                                subroutineName = mainSubroutineDefinition.name,
                                currentNode = mainSubroutineDefinition.entryNode(),
                            ),
                        ),
                    outputValues = emptyList(),
                    inputValues = listOf(conditionalValue),
                )

            val evalConfig = EvaluationConfig(skipInputValueWithNoMatchingTransition = false)

            // when
            val result =
                getNextProgramState(
                    programDefinition = programDefinition,
                    programState = programState,
                    evaluationConfig = evalConfig,
                )
            // then
            assertThat(result).isEqualTo(
                ProgramState(
                    subroutineStack =
                        listOf(
                            SubroutineState(
                                subroutineName = mainSubroutineDefinition.name,
                                currentNode = node2,
                            ),
                        ),
                    outputValues = listOf(outputValue),
                    inputValues = emptyList(),
                ),
            )
        }
    }

    @Nested
    inner class `test getNextProgramStateInGivenNumberOfSteps` {
        @Test
        fun `3 nodes and 2 unconditional transitions which push to the ouput`() {
            // given
            val node1 = EntryNode(NodeName("node1"))
            val node2 = BasicNode(NodeName("node2"))
            val node3 = ExitNode(NodeName("node3"))

            val outputValue1 = 123.toUByte()
            val outputValue2 = 234.toUByte()
            val transition1 =
                Transition(
                    fromNode = node1,
                    toNode = node2,
                    conditions = emptySet(),
                    actions =
                        setOf(
                            Transition.Action.PushToOutputStack(outputValue = outputValue1),
                        ),
                )

            val transition2 =
                Transition(
                    fromNode = node2,
                    toNode = node3,
                    conditions = emptySet(),
                    actions =
                        setOf(
                            Transition.Action.PushToOutputStack(outputValue = outputValue2),
                        ),
                )

            val mainSubroutineDefinition =
                SubroutineDefinition(
                    name = SubroutineName("main"),
                    nodes = setOf(node1, node2, node3),
                    transitions = setOf(transition1, transition2),
                )

            val programDefinition =
                ProgramDefinition(
                    mainSubroutine = mainSubroutineDefinition,
                    otherSubroutines = emptySet(),
                )

            val programState =
                ProgramState(
                    subroutineStack =
                        listOf(
                            SubroutineState(
                                subroutineName = mainSubroutineDefinition.name,
                                currentNode = mainSubroutineDefinition.entryNode(),
                            ),
                        ),
                    outputValues = emptyList(),
                    inputValues = emptyList(),
                )

            val evalConfig = EvaluationConfig(skipInputValueWithNoMatchingTransition = false)

            // when
            val result =
                getProgramStateInGivenNumberOfSteps(
                    programDefinition = programDefinition,
                    programState = programState,
                    evaluationConfig = evalConfig,
                    numberOfSteps = 2u,
                )
            // then
            assertThat(result).isEqualTo(
                ProgramState(
                    subroutineStack =
                        listOf(
                            SubroutineState(
                                subroutineName = mainSubroutineDefinition.name,
                                currentNode = node3,
                            ),
                        ),
                    outputValues = listOf(outputValue1, outputValue2),
                    inputValues = emptyList(),
                ),
            )
        }
    }

    @Test
    fun `2 nodes and 1 unconditional self-loop transition which push to the ouput`() {
        // given
        val node1 = EntryNode(NodeName("node1"))
        val node2 = BasicNode(NodeName("node2"))

        val outputValue = 123.toUByte()
        val transition =
            Transition(
                fromNode = node1,
                toNode = node1,
                conditions = emptySet(),
                actions =
                    setOf(
                        Transition.Action.PushToOutputStack(outputValue = outputValue),
                    ),
            )

        val mainSubroutineDefinition =
            SubroutineDefinition(
                name = SubroutineName("main"),
                nodes = setOf(node1, node2),
                transitions = setOf(transition),
            )

        val programDefinition =
            ProgramDefinition(
                mainSubroutine = mainSubroutineDefinition,
                otherSubroutines = emptySet(),
            )

        val programState =
            ProgramState(
                subroutineStack =
                    listOf(
                        SubroutineState(
                            subroutineName = mainSubroutineDefinition.name,
                            currentNode = mainSubroutineDefinition.entryNode(),
                        ),
                    ),
                outputValues = emptyList(),
                inputValues = emptyList(),
            )

        val evalConfig = EvaluationConfig(skipInputValueWithNoMatchingTransition = false)
        val numberOfSteps = 10u

        // when
        val result =
            getProgramStateInGivenNumberOfSteps(
                programDefinition = programDefinition,
                programState = programState,
                evaluationConfig = evalConfig,
                numberOfSteps = numberOfSteps,
            )
        // then
        assertThat(result).isEqualTo(
            ProgramState(
                subroutineStack =
                    listOf(
                        SubroutineState(
                            subroutineName = mainSubroutineDefinition.name,
                            currentNode = node1,
                        ),
                    ),
                outputValues = (1u..numberOfSteps).map { outputValue },
                inputValues = emptyList(),
            ),
        )
    }
}
