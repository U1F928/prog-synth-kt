package parseSubroutine

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import parseInstruction.BasicNode
import parseInstruction.CallNode
import parseInstruction.InputWord
import parseInstruction.NodeName
import parseInstruction.SubroutineName
import parseInstruction.Transition

class ParseSubroutineTest {
    @Nested
    inner class `test parsing of Subroutine Content` {
        @Test
        fun `parse correctly formatted subroutine content`() {
            // given
            val input =
                listOf(
                    //
                    "NODE",
                    "myNode1",
                    //
                    "CALL",
                    "NODE",
                    "myNode2",
                    "OF",
                    "mySubroutine1",
                    //
                    "FROM",
                    "myNode1",
                    "ON",
                    "INPUT",
                    "123",
                    "GOTO",
                    "myNode2",
                    //
                    "END",
                ).map { InputWord(it) }

            // when
            val result = parseSubroutineContent(remainingWords = input)

            // then
            assertThat(result).isEqualTo(
                ParsedSubroutineContent(
                    remainingWords =
                        listOf(
                            InputWord("END"),
                        ),
                    parsedBodyNodes =
                        listOf(
                            BasicNode(
                                nodeName = NodeName("myNode1"),
                            ),
                            CallNode(
                                nodeName = NodeName("myNode2"),
                                subroutineName = SubroutineName("mySubroutine1"),
                            ),
                        ),
                    parsedTransitions =
                        listOf(
                            Transition(
                                fromNode = NodeName("myNode1"),
                                toNode = NodeName("myNode2"),
                                conditions =
                                    listOf(
                                        Transition.OnInputStack(
                                            conditionalValue = 123.toByte(),
                                        ),
                                    ),
                            ),
                        ),
                ),
            )
        }
    }
}
