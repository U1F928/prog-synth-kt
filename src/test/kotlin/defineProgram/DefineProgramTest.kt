package defineProgram

import org.junit.jupiter.api.Test

class DefineProgramTest {
    @Test
    fun `test`() {
        val x =
            program {
                val main by subroutine {
                    val q1 by node()
                    val q2 by node()
                    q1 to q2
                }
            }
        println(x)
    }
}
