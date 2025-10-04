package parseInstruction

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import result.getOrThrow
import result.toErr

class ParseInstructionTest {
    @Nested
    inner class `test parsing of StartSubroutine instruction` {
        @Test
        fun `parse correctly formatted instruction`() {
            // given
            val input =
                listOf(
                    "SUBROUTINE",
                    "mySubroutineName",
                    "remaining",
                    "words",
                ).map { InputWord(it) }

            // when
            val result = parseSubroutineStart(words = input).getOrThrow()

            // then
            assertThat(result).isEqualTo(
                InstructionParseResult.Success<SubroutineStart>(
                    remainingWords =
                        listOf(
                            InputWord("remaining"),
                            InputWord("words"),
                        ),
                    parsedInstruction =
                        SubroutineStart(
                            subroutineName = SubroutineName("mySubroutineName"),
                        ),
                ),
            )
        }

        @Test
        fun `return error when not enough words for a subroutine instruction`() {
            // given
            val input =
                listOf(
                    InputWord("SUBROUTINE"),
                )

            // when
            val result = parseSubroutineStart(words = input)

            // then
            assertThat(result).isEqualTo(InstructionParseResult.Error.toErr())
        }

        @Test
        fun `return error when subroutine names does not match the format`() {
            // given
            val input =
                listOf(
                    InputWord("SUBROUTINE"),
                    InputWord("MySubroutine"),
                )

            // when
            val result = parseSubroutineStart(words = input)

            // then
            assertThat(result).isEqualTo(InstructionParseResult.Error.toErr())
        }
    }

    @Nested
    inner class `test parsing of EndSubroutine instruction` {
        @Test
        fun `parse correctly formatted instruction`() {
            // given
            val input =
                listOf(
                    "END",
                    "remaining",
                    "words",
                ).map { InputWord(it) }

            // when
            val result = parseSubroutineEnd(words = input).getOrThrow()

            // then
            assertThat(result).isEqualTo(
                InstructionParseResult.Success<EndSubroutine>(
                    remainingWords =
                        listOf(
                            InputWord("remaining"),
                            InputWord("words"),
                        ),
                    parsedInstruction = EndSubroutine,
                ),
            )
        }

        @Test
        fun `return error when not enough words for a subroutine instruction`() {
            // given
            val input =
                listOf(
                    InputWord("SUBROUTINE"),
                )

            // when
            val result = parseSubroutineStart(words = input)

            // then
            assertThat(result).isEqualTo(InstructionParseResult.Error.toErr())
        }

        @Test
        fun `return error when subroutine names does not match the format`() {
            // given
            val input =
                listOf(
                    InputWord("SUBROUTINE"),
                    InputWord("MySubroutine"),
                )

            // when
            val result = parseSubroutineStart(words = input)

            // then
            assertThat(result).isEqualTo(InstructionParseResult.Error.toErr())
        }
    }

    @Nested
    inner class `test parsing of FromNode instruction` {
        @Test
        fun `parse correctly formatted instruction`() {
            // given
            val input =
                listOf(
                    "FROM",
                    "myNodeName",
                    "remaining",
                    "words",
                ).map { InputWord(it) }

            // when
            val result = parseFromNode(words = input).getOrThrow()

            // then
            assertThat(result).isEqualTo(
                InstructionParseResult.Success(
                    remainingWords =
                        listOf(
                            InputWord("remaining"),
                            InputWord("words"),
                        ),
                    parsedInstruction =
                        Transition.FromNode(
                            nodeName = NodeName("myNodeName"),
                        ),
                ),
            )
        }

        @Test
        fun `return error when not enough words for a FromNode instruction`() {
            // given
            val input =
                listOf(
                    InputWord("FROM"),
                )

            // when
            val result = parseFromNode(words = input)

            // then
            assertThat(result).isEqualTo(InstructionParseResult.Error.toErr())
        }

        @Test
        fun `return error when node names does not match the format`() {
            // given
            val input =
                listOf(
                    InputWord("FROM"),
                    InputWord("MyNode"),
                )

            // when
            val result = parseFromNode(words = input)

            // then
            assertThat(result).isEqualTo(InstructionParseResult.Error.toErr())
        }
    }

    @Nested
    inner class `test parsing of GotoNode instruction` {
        @Test
        fun `parse correctly formatted instruction`() {
            // given
            val input =
                listOf(
                    "GOTO",
                    "myNodeName",
                    "remaining",
                    "words",
                ).map { InputWord(it) }

            // when
            val result = parseGotoNode(words = input).getOrThrow()

            // then
            assertThat(result).isEqualTo(
                InstructionParseResult.Success(
                    remainingWords =
                        listOf(
                            InputWord("remaining"),
                            InputWord("words"),
                        ),
                    parsedInstruction =
                        Transition.GotoNode(
                            nodeName = NodeName("myNodeName"),
                        ),
                ),
            )
        }

        @Test
        fun `return error when not enough words for a Goto instruction`() {
            // given
            val input =
                listOf(
                    InputWord("GOTO"),
                )

            // when
            val result = parseGotoNode(words = input)

            // then
            assertThat(result).isEqualTo(InstructionParseResult.Error.toErr())
        }

        @Test
        fun `return error when node names does not match the format`() {
            // given
            val input =
                listOf(
                    InputWord("GOTO"),
                    InputWord("MyNode"),
                )

            // when
            val result = parseGotoNode(words = input)

            // then
            assertThat(result).isEqualTo(InstructionParseResult.Error.toErr())
        }
    }

    @Nested
    inner class `test parsing of NodeDeclaration instruction` {
        @Test
        fun `parse correctly formatted instruction`() {
            // given
            val input =
                listOf(
                    "NODE",
                    "myNodeName",
                    "remaining",
                    "words",
                ).map { InputWord(it) }

            // when
            val result = parseBasicNode(words = input).getOrThrow()

            // then
            assertThat(result).isEqualTo(
                InstructionParseResult.Success(
                    remainingWords =
                        listOf(
                            InputWord("remaining"),
                            InputWord("words"),
                        ),
                    parsedInstruction =
                        BasicNode(
                            nodeName = NodeName("myNodeName"),
                        ),
                ),
            )
        }

        @Test
        fun `return error when not enough words for a NodeDeclaration instruction`() {
            // given
            val input =
                listOf(
                    InputWord("GOTO"),
                )

            // when
            val result = parseBasicNode(words = input)

            // then
            assertThat(result).isEqualTo(InstructionParseResult.Error.toErr())
        }

        @Test
        fun `return error when node names does not match the format`() {
            // given
            val input =
                listOf(
                    InputWord("NODE"),
                    InputWord("MyNode"),
                )

            // when
            val result = parseBasicNode(words = input)

            // then
            assertThat(result).isEqualTo(InstructionParseResult.Error.toErr())
        }
    }

    @Nested
    inner class `test parsing of EntryNodeDeclaration instruction` {
        @Test
        fun `parse correctly formatted instruction`() {
            // given
            val input =
                listOf(
                    "ENTRY",
                    "NODE",
                    "myNodeName",
                    "remaining",
                    "words",
                ).map { InputWord(it) }

            // when
            val result = parseEntryNode(words = input)

            // then
            assertThat(result).isEqualTo(
                InstructionParseResult.Success(
                    remainingWords =
                        listOf(
                            InputWord("remaining"),
                            InputWord("words"),
                        ),
                    parsedInstruction =
                        EntryNode(
                            nodeName = NodeName("myNodeName"),
                        ),
                ),
            )
        }

        @Test
        fun `return error when not enough words for a EntryNode instruction`() {
            // given
            val input =
                listOf(
                    InputWord("ENTRY"),
                    InputWord("NODE"),
                )

            // when
            val result = parseBasicNode(words = input)

            // then
            assertThat(result).isEqualTo(InstructionParseResult.Error.toErr())
        }

        @Test
        fun `return error when subroutine names does not match the format`() {
            // given
            val input =
                listOf(
                    InputWord("NODE"),
                    InputWord("MyNode"),
                )

            // when
            val result = parseEntryNode(words = input)

            // then
            assertThat(result).isEqualTo(InstructionParseResult.Error)
        }
    }

    @Nested
    inner class `test parsing of ExitNodeDeclaration instruction` {
        @Test
        fun `parse correctly formatted instruction`() {
            // given
            val input =
                listOf(
                    "EXIT",
                    "NODE",
                    "myNodeName",
                    "remaining",
                    "words",
                ).map { InputWord(it) }

            // when
            val result = parseExitNode(words = input).getOrThrow()

            // then
            assertThat(result).isEqualTo(
                InstructionParseResult.Success(
                    remainingWords =
                        listOf(
                            InputWord("remaining"),
                            InputWord("words"),
                        ),
                    parsedInstruction =
                        ExitNode(
                            nodeName = NodeName("myNodeName"),
                        ),
                ),
            )
        }

        @Test
        fun `return error when not enough words for a EntryNode instruction`() {
            // given
            val input =
                listOf(
                    InputWord("EXIT"),
                    InputWord("NODE"),
                )

            // when
            val result = parseExitNode(words = input)

            // then
            assertThat(result).isEqualTo(InstructionParseResult.Error.toErr())
        }

        @Test
        fun `return error when node name does not match the format`() {
            // given
            val input =
                listOf(
                    InputWord("EXIT"),
                    InputWord("MyNode"),
                )

            // when
            val result = parseEntryNode(words = input)

            // then
            assertThat(result).isEqualTo(InstructionParseResult.Error)
        }
    }

    @Nested
    inner class `test parsing of CallNodeDeclaration instruction` {
        @Test
        fun `parse correctly formatted instruction`() {
            // given
            val input =
                listOf(
                    "CALL",
                    "NODE",
                    "myNodeName",
                    "mySubroutineName",
                    "remaining",
                    "words",
                ).map { InputWord(it) }

            // when
            val result = parseCallNode(words = input).getOrThrow()

            // then
            assertThat(result).isEqualTo(
                InstructionParseResult.Success(
                    remainingWords =
                        listOf(
                            InputWord("remaining"),
                            InputWord("words"),
                        ),
                    parsedInstruction =
                        CallNodeDeclaration(
                            nodeName = NodeName("myNodeName"),
                            subroutineName = SubroutineName("mySubroutineName"),
                        ),
                ),
            )
        }

        @Test
        fun `return error when not enough words for a EntryNode instruction`() {
            // given
            val input =
                listOf(
                    InputWord("SUBROUTINE"),
                    InputWord("NODE"),
                    InputWord("myNodeName"),
                )

            // when
            val result = parseCallNode(words = input)

            // then
            assertThat(result).isEqualTo(InstructionParseResult.Error.toErr())
        }

        @Test
        fun `return error when node name does not match the format`() {
            // given
            val input =
                listOf(
                    InputWord("CALL"),
                    InputWord("NODE"),
                    InputWord("MyNode"),
                    InputWord("mySubroutine"),
                )

            // when
            val result = parseCallNode(words = input)

            // then
            assertThat(result).isEqualTo(InstructionParseResult.Error.toErr())
        }

        @Test
        fun `return error when subroutine name does not match the format`() {
            // given
            val input =
                listOf(
                    InputWord("SUBROUTINE"),
                    InputWord("NODE"),
                    InputWord("myNode"),
                    InputWord("MySubroutine"),
                )

            // when
            val result = parseCallNode(words = input)

            // then
            assertThat(result).isEqualTo(InstructionParseResult.Error.toErr())
        }
    }

    @Nested
    inner class `test parsing of OnInputStack instruction` {
        @Test
        fun `parse correctly formatted instruction using number to represent byte`() {
            // given
            val input =
                listOf(
                    "ON",
                    "INPUT",
                    "123",
                    "remaining",
                    "words",
                ).map { InputWord(it) }

            // when
            val result = parseOnInputStack(words = input).getOrThrow()

            // then
            assertThat(result).isEqualTo(
                InstructionParseResult.Success(
                    remainingWords =
                        listOf(
                            InputWord("remaining"),
                            InputWord("words"),
                        ),
                    parsedInstruction =
                        Transition.OnInputStack(
                            conditionalValue = 123.toByte(),
                        ),
                ),
            )
        }

        @Test
        fun `parse correctly formatted instruction using char to represent byte`() {
            // given
            val input =
                listOf(
                    "ON",
                    "INPUT",
                    "a",
                    "remaining",
                    "words",
                ).map { InputWord(it) }

            // when
            val result = parseOnInputStack(words = input).getOrThrow()

            // then
            assertThat(result).isEqualTo(
                InstructionParseResult.Success(
                    remainingWords =
                        listOf(
                            InputWord("remaining"),
                            InputWord("words"),
                        ),
                    parsedInstruction =
                        Transition.OnInputStack(
                            conditionalValue = 'a'.code.toByte(),
                        ),
                ),
            )
        }

        @Test
        fun `return error when not enough words for a OnInputStack instruction`() {
            // given
            val input =
                listOf(
                    InputWord("ON"),
                    InputWord("INPUT"),
                )

            // when
            val result = parseOnInputStack(words = input)

            // then
            assertThat(result).isEqualTo(InstructionParseResult.Error.toErr())
        }

        @Test
        fun `return error when instruction does not match the format`() {
            // given
            val input =
                listOf(
                    InputWord("ON"),
                    InputWord("iNPUT"),
                )

            // when
            val result = parseOnInputStack(words = input)

            // then
            assertThat(result).isEqualTo(InstructionParseResult.Error.toErr())
        }
    }

    @Nested
    inner class `test parsing of Transition instruction` {
        @Test
        fun `parse correctly formatted instruction`() {
            // given
            val input =
                listOf(
                    "FROM",
                    "myNodeA",
                    "ON",
                    "INPUT",
                    "123",
                    "GOTO",
                    "myNodeB",
                    "remaining",
                    "words",
                ).map { InputWord(it) }

            // when
            val result = parseTransition(words = input).getOrThrow()

            // then
            assertThat(result).isEqualTo(
                InstructionParseResult.Success<Transition>(
                    remainingWords =
                        listOf(
                            InputWord("remaining"),
                            InputWord("words"),
                        ),
                    parsedInstruction =
                        Transition(
                            fromNode = NodeName("myNodeA"),
                            toNode = NodeName("myNodeB"),
                            conditions =
                                listOf(
                                    Transition.OnInputStack(
                                        conditionalValue = 123.toByte(),
                                    ),
                                ),
                        ),
                ),
            )
        }

        @Test
        fun `return error when not enough words for a EntryNode instruction`() {
            // given
            val input =
                listOf(
                    InputWord("SUBROUTINE"),
                    InputWord("NODE"),
                    InputWord("myNodeName"),
                )

            // when
            val result = parseCallNode(words = input)

            // then
            assertThat(result).isEqualTo(InstructionParseResult.Error.toErr())
        }

        @Test
        fun `return error when node name does not match the format`() {
            // given
            val input =
                listOf(
                    InputWord("SUBROUTINE"),
                    InputWord("NODE"),
                    InputWord("MyNode"),
                    InputWord("mySubroutine"),
                )

            // when
            val result = parseCallNode(words = input)

            // then
            assertThat(result).isEqualTo(InstructionParseResult.Error.toErr())
        }

        @Test
        fun `return error when subroutine name does not match the format`() {
            // given
            val input =
                listOf(
                    InputWord("SUBROUTINE"),
                    InputWord("NODE"),
                    InputWord("myNode"),
                    InputWord("MySubroutine"),
                )

            // when
            val result = parseCallNode(words = input)

            // then
            assertThat(result).isEqualTo(InstructionParseResult.Error.toErr())
        }
    }
}
