@file:Suppress("ktlint")
package defineProgram
import org.junit.jupiter.api.Test

class DefineProgramTest {
    @Test
    fun `test`() {
        val x =
            PROGRAM {
                val main by SUBROUTINE {
                    val q1 by NODE()
                    val q2 by NODE()
                    val q3 by NODE()

                    FROM(q3)
                        .ON(INPUT, 43)
                        .GOTO(q1)

                    FROM(q1)
                        .ON(INPUT, 23)
                        .PUSH(OUTPUT, 443)
                        .GOTO(q3)
                }
            }
        println(x)
    }
}
