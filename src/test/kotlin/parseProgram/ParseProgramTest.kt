package parseProgram

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import parseInstruction.BasicNode
import parseInstruction.EntryNode
import parseInstruction.ExitNode
import parseInstruction.InputWord
import parseInstruction.NodeName
import parseInstruction.SubroutineName
import parseInstruction.SubroutineStart
import parseInstruction.Transition
import parseSubroutine.ParsedSubroutine
import result.getErrOrThrow
import result.getOrThrow
import result.v

class ParseProgramTest {

        @Test
        fun `parse correctly formatted subroutine content`() {
            // given
            val mainSubroutineDefinition = listOf(
                //
                "SUBROUTINE",
                "main",
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
                "FROM",
                "myNode1",
                "GOTO",
                "myNode2",
                //
                "END",
            ).map { InputWord(it) }

            val otherSubroutineDefinition = listOf(
                    "SUBROUTINE",
                "mySubroutineB",
                //
                "ENTRY",
                "NODE",
                "myNodeB1",
                //
                "EXIT",
                "NODE",
                "myNodeB2",
                //
                "NODE",
                "myNodeB3",
                //
                "END",
           ).map { InputWord(it) }

            val input = (mainSubroutineDefinition + otherSubroutineDefinition)

            // when
            val result = parseProgram(inputWords = input).getOrThrow()

            // then
            assertThat(result.mainSubroutine).isEqualTo(
                ParsedSubroutine.Success(
                    subroutineName = SubroutineName("main"),
                    entryNode = EntryNode(NodeName("myNode1")),
                    exitNode = ExitNode(NodeName("myNode2")),
                    bodyNodes = listOf(
                        BasicNode(NodeName("myNode3"))
                    ),
                    transitions = listOf(
                        Transition(
                            fromNode = NodeName("myNode1"),
                            toNode = NodeName("myNode2"),
                            conditions = emptyList(),
                            actions = emptyList()
                        )
                    ),
                    remainingWords = otherSubroutineDefinition
                )
            )


            assertThat(result.restOfSubroutines).hasSize(1)
            assertThat(result.restOfSubroutines.single()).isEqualTo(
                ParsedSubroutine.Success(
                    subroutineName = SubroutineName("mySubroutineB"),
                    entryNode = EntryNode(NodeName("myNodeB1")),
                    exitNode = ExitNode(NodeName("myNodeB2")),
                    bodyNodes = listOf(
                        BasicNode(NodeName("myNodeB3"))
                    ),
                    transitions = emptyList(),
                    remainingWords = emptyList()
                )
            )
        }

    @Test
    fun `return error on an invalid main subroutine definition`() {
        // given
        val mainSubroutineDefinition = listOf(
            //
            "SUBROUTINE",
            "main",
            //
            "END",
        ).map { InputWord(it) }

        // when
        val result = parseProgram(inputWords = mainSubroutineDefinition).getErrOrThrow()

        // then
        assertThat(result).isEqualTo(
            ParsedProgram.Error(
                remainingWords = listOf(
                    InputWord("END")
                )
            )
        )
    }
}
