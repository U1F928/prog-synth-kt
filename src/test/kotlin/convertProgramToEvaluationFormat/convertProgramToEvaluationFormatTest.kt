@file:Suppress("ktlint")
package convertProgramToEvaluationFormat
import defineProgram.FROM
import defineProgram.GOTO
import defineProgram.INPUT
import defineProgram.ON
import defineProgram.OUTPUT
import defineProgram.PROGRAM
import defineProgram.PUSH
import defineProgram.SUBROUTINE
import evaluateProgram.EvaluationConfig
import evaluateProgram.evaluateProgram
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class ConvertProgramToEvaluationFormatTest {

    @Nested
    inner class `test execution of the converted program`{
        @Test
        fun `test conversion of 1 to 111 and of 2 to 222`(){
            // given
            val program = PROGRAM{
                val main by SUBROUTINE {
                    FROM(ENTRY_NODE)
                        .ON(INPUT, 1)
                        .PUSH(OUTPUT, 111)
                        .GOTO(ENTRY_NODE)

                    FROM(ENTRY_NODE)
                        .ON(INPUT, 2)
                        .PUSH(OUTPUT, 222)
                        .GOTO(ENTRY_NODE)
                }
            }

            // when
            val evaluableProgram = program.toEvaluationFormat()

            val inputValues = listOf(
                1, 1, 1,
                2, 2, 2
            ).map{it.toUByte()}

            val result = evaluateProgram(
                programDefinition = evaluableProgram,
                inputValues = inputValues,
                evaluationConfig = EvaluationConfig( skipInputValueWithNoMatchingTransition = false )
            )

            // then
            val expectedOutputValues = listOf(
                111, 111, 111,
                222, 222, 222
            ).map{it.toUByte()}

            assertThat(result.outputValues).isEqualTo(expectedOutputValues)
        }
    }

}
