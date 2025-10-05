package parseSubroutine

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import parseInstruction.BasicNode
import parseInstruction.CallNode
import parseInstruction.EntryNode
import parseInstruction.ExitNode
import parseInstruction.InputWord
import parseInstruction.NodeName
import parseInstruction.SubroutineName
import parseInstruction.SubroutineStart
import parseInstruction.Transition
import result.getErrOrThrow
import result.getOrThrow

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
            assertThat(result.remainingWords).containsExactly(
                InputWord("END"),
            )

            assertThat(result.parsedBodyNodes).containsExactly(
                BasicNode(
                    nodeName = NodeName("myNode1"),
                ),
                CallNode(
                    nodeName = NodeName("myNode2"),
                    subroutineName = SubroutineName("mySubroutine1"),
                ),
            )

            assertThat(result.parsedTransitions).containsExactly(
                Transition(
                    fromNode = NodeName("myNode1"),
                    toNode = NodeName("myNode2"),
                    conditions =
                        listOf(
                            Transition.OnInputStack(
                                conditionalValue = 123.toUByte(),
                            ),
                        ),
                    emptyList(),
                ),
            )
        }
    }

    @Nested
    inner class `test parsing of whole subroutine` {
        @Test
        fun `parse correctly formatted subroutine content`() {
            // given
            val input =
                listOf(
                    //
                    "SUBROUTINE",
                    "mySubroutine1",
                    //
                    "ENTRY",
                    "NODE",
                    "myNode1",
                    //
                    "EXIT",
                    "NODE",
                    "myNode2",
                    //
                    "NODE",
                    "myNode3",
                    //
                    "CALL",
                    "NODE",
                    "myNode4",
                    "OF",
                    "mySubroutine2",
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
                    //
                    "SUBROUTINE",
                    "mySubroutine2",
                    "...",
                ).map { InputWord(it) }

            // when
            val result = parseSubroutine(inputWords = input).getOrThrow()

            // then
            assertThat(result.subroutineName).isEqualTo(
                 SubroutineName("mySubroutine1"),
            )

            assertThat(result.entryNode).isEqualTo(
                EntryNode(
                    nodeName = NodeName("myNode1"),
                ),
            )

            assertThat(result.exitNode).isEqualTo(
                ExitNode(
                    nodeName = NodeName("myNode2"),
                ),
            )

            assertThat(result.bodyNodes).containsExactly(
                BasicNode(
                    nodeName = NodeName("myNode3"),
                ),
                CallNode(
                    nodeName = NodeName("myNode4"),
                    subroutineName = SubroutineName("mySubroutine2"),
                ),
            )

            assertThat(result.transitions).containsExactly(
                Transition(
                    fromNode = NodeName("myNode1"),
                    toNode = NodeName("myNode2"),
                    conditions =
                        listOf(
                            Transition.OnInputStack(
                                conditionalValue = 123.toUByte(),
                            ),
                        ),
                    emptyList(),
                ),
            )

            assertThat(result.remainingWords).containsExactly(
                InputWord("SUBROUTINE"),
                InputWord("mySubroutine2"),
                InputWord("..."),
            )
        }
    }

    @Test
    fun `return error on invalid subroutine definition`() {
        // given
        val input =
            listOf(
                //
                "SUBROUTINE",
                "mySubroutine1",
                //
                "ENTRY",
                "NODE",
                "myNode1",
                //
                "EXIT",
                "NODE",
                "myNode2",
                //
                "nODE",
                "myNode3",
                //
                "END",
            ).map { InputWord(it) }

        // when
        val result = parseSubroutine(inputWords = input).getErrOrThrow()

        // then
        assertThat(result).isEqualTo(
            ParsedSubroutine.Error(
                remainingWords = listOf(
                    InputWord("nODE"),
                    InputWord("myNode3"),
                    InputWord("END")
                )
            )
        )
    }
}
