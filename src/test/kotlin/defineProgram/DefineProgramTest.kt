@file:Suppress("ktlint")
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
                    val q3 by node()

                    from(q1).to(q2)
                        .pushOutput(12)

                    from(q2).to(q3)
                        .on(23).at(INPUT)
                        .push(23).to(OUTPUT)
                }
            }
        println(x)
    }
}
