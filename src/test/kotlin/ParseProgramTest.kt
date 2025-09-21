import org.junit.jupiter.api.Test
import parseProgram.parseStartSubroutineDefinition
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Nested
import parseProgram.InputWord
import parseProgram.NodeName
import parseProgram.InstructionParseResult
import parseProgram.ParsedInstruction
import parseProgram.SubroutineName
import parseProgram.parseFromNode
import parseProgram.parseEndSubroutine
import parseProgram.parseGotoNode
import parseProgram.parseOnInputStack

class ParseProgramTest {

    @Nested
    inner class `test parsing of StartSubroutine instruction`{
        @Test
        fun  `parse correctly formatted instruction`(){
            // given
            val input = listOf(
                "SUBROUTINE",
                "mySubroutineName",
                "remaining",
                "text"
            )
                .map{ InputWord(it) }

            // when
            val result = parseStartSubroutineDefinition(words = input)

            // then
            assertThat(result).isEqualTo(
                InstructionParseResult.Success(
                    remainingWords = listOf(
                        InputWord("remaining"),
                        InputWord("text")
                    ),
                    parsedInstruction = ParsedInstruction.StartSubroutine(
                        subroutineName = SubroutineName("mySubroutineName")
                    )
                )
            )
        }

        @Test
        fun  `return error when not enough words for a subroutine instruction`(){
            // given
            val input = listOf(
                InputWord("SUBROUTINE")
            )

            // when
            val result = parseStartSubroutineDefinition(words = input)

            // then
            assertThat(result).isEqualTo( InstructionParseResult.Error )
        }

        @Test
        fun  `return error when subroutine names does not match the format`(){
            // given
            val input = listOf(
                InputWord("SUBROUTINE"),
                InputWord("MySubroutine"),
            )

            // when
            val result = parseStartSubroutineDefinition(words = input)

            // then
            assertThat(result).isEqualTo( InstructionParseResult.Error )
        }
    }

    @Nested
    inner class `test parsing of EndSubroutine instruction`{
        @Test
        fun  `parse correctly formatted instruction`(){
            // given
            val input = listOf(
                "END",
                "remaining",
                "text"
            )
                .map{ InputWord(it) }

            // when
            val result = parseEndSubroutine(words = input)

            // then
            assertThat(result).isEqualTo(
                InstructionParseResult.Success(
                    remainingWords = listOf(
                        InputWord("remaining"),
                        InputWord("text")
                    ),
                    parsedInstruction = ParsedInstruction.EndSubroutine
                )
            )
        }

        @Test
        fun  `return error when not enough words for a subroutine instruction`(){
            // given
            val input = listOf(
                InputWord("SUBROUTINE")
            )

            // when
            val result = parseStartSubroutineDefinition(words = input)

            // then
            assertThat(result).isEqualTo( InstructionParseResult.Error )
        }

        @Test
        fun  `return error when subroutine names does not match the format`(){
            // given
            val input = listOf(
                InputWord("SUBROUTINE"),
                InputWord("MySubroutine"),
            )

            // when
            val result = parseStartSubroutineDefinition(words = input)

            // then
            assertThat(result).isEqualTo( InstructionParseResult.Error )
        }
    }

    @Nested
    inner class `test parsing of FromNode instruction`{
        @Test
        fun  `parse correctly formatted instruction`(){
            // given
            val input = listOf(
                "FROM",
                "myNodeName",
                "remaining",
                "text"
            )
                .map{ InputWord(it) }

            // when
            val result = parseFromNode(words = input)

            // then
            assertThat(result).isEqualTo(
                InstructionParseResult.Success(
                    remainingWords = listOf(
                        InputWord("remaining"),
                        InputWord("text")
                    ),
                    parsedInstruction = ParsedInstruction.FromNode(
                        nodeName = NodeName("myNodeName")
                    )
                )
            )
        }

        @Test
        fun  `return error when not enough words for a FromNode instruction`(){
            // given
            val input = listOf(
                InputWord("FROM")
            )

            // when
            val result = parseFromNode(words = input)

            // then
            assertThat(result).isEqualTo( InstructionParseResult.Error )
        }

        @Test
        fun  `return error when subroutine names does not match the format`(){
            // given
            val input = listOf(
                InputWord("FROM"),
                InputWord("MyNode"),
            )

            // when
            val result = parseFromNode(words = input)

            // then
            assertThat(result).isEqualTo( InstructionParseResult.Error )
        }
    }

    @Nested
    inner class `test parsing of GotoNode instruction`{
        @Test
        fun  `parse correctly formatted instruction`(){
            // given
            val input = listOf(
                "FROM",
                "myNodeName",
                "remaining",
                "text"
            )
                .map{ InputWord(it) }

            // when
            val result = parseGotoNode(words = input)

            // then
            assertThat(result).isEqualTo(
                InstructionParseResult.Success(
                    remainingWords = listOf(
                        InputWord("remaining"),
                        InputWord("text")
                    ),
                    parsedInstruction = ParsedInstruction.GotoNode(
                        nodeName = NodeName("myNodeName")
                    )
                )
            )
        }

        @Test
        fun  `return error when not enough words for a FromNode instruction`(){
            // given
            val input = listOf(
                InputWord("GOTO")
            )

            // when
            val result = parseGotoNode(words = input)

            // then
            assertThat(result).isEqualTo( InstructionParseResult.Error )
        }

        @Test
        fun  `return error when subroutine names does not match the format`(){
            // given
            val input = listOf(
                InputWord("GOTO"),
                InputWord("MyNode"),
            )

            // when
            val result = parseGotoNode(words = input)

            // then
            assertThat(result).isEqualTo( InstructionParseResult.Error )
        }
    }

    @Nested
    inner class `test parsing of OnInputStack instruction`{
        @Test
        fun  `parse correctly formatted instruction using number to represent byte`(){
            // given
            val input = listOf(
                "ON",
                "INPUT",
                "123",
                "remaining",
                "text"
            )
                .map{ InputWord(it) }

            // when
            val result = parseOnInputStack(words = input)

            // then
            assertThat(result).isEqualTo(
                InstructionParseResult.Success(
                    remainingWords = listOf(
                        InputWord("remaining"),
                        InputWord("text")
                    ),
                    parsedInstruction = ParsedInstruction.OnInputStack(
                        conditionalValue = 123.toByte()
                    )
                )
            )
        }

        @Test
        fun  `parse correctly formatted instruction using char to represent byte`(){
            // given
            val input = listOf(
                "ON",
                "INPUT",
                "a",
                "remaining",
                "text"
            )
                .map{ InputWord(it) }

            // when
            val result = parseOnInputStack(words = input)

            // then
            assertThat(result).isEqualTo(
                InstructionParseResult.Success(
                    remainingWords = listOf(
                        InputWord("remaining"),
                        InputWord("text")
                    ),
                    parsedInstruction = ParsedInstruction.OnInputStack(
                        conditionalValue = 'a'.code.toByte()
                    )
                )
            )
        }

        @Test
        fun  `return error when not enough words for a OnInputStack instruction`(){
            // given
            val input = listOf(
                InputWord("ON"),
                InputWord("INPUT")
            )

            // when
            val result = parseOnInputStack(words = input)

            // then
            assertThat(result).isEqualTo( InstructionParseResult.Error )
        }

        @Test
        fun  `return error when instruction does not match the format`(){
            // given
            val input = listOf(
                InputWord("ON"),
                InputWord("iNPUT"),
            )

            // when
            val result = parseOnInputStack(words = input)

            // then
            assertThat(result).isEqualTo( InstructionParseResult.Error )
        }
    }
}