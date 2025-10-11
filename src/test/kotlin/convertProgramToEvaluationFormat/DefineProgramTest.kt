@file:Suppress("ktlint")
package convertProgramToEvaluationFormat
import defineProgram.BasicNode
import defineProgram.CALL_NODE
import defineProgram.CallNode
import defineProgram.CustomStack
import defineProgram.FROM
import defineProgram.GOTO
import defineProgram.INPUT
import defineProgram.NODE
import defineProgram.ON
import defineProgram.OUTPUT
import defineProgram.PROGRAM
import defineProgram.PUSH
import defineProgram.STACK
import defineProgram.SUBROUTINE
import defineProgram.Transition
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class ConvertProgramToEvaluationFormatTest {
    @Test
    fun `test a`(){
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
        // TODO
        val evaluableProgram = program.toEvaluationFormat()
    }
}
