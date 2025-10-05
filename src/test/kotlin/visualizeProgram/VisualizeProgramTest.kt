package visualizeProgram

import org.junit.jupiter.api.Test
import javax.swing.SwingUtilities

class VisualizeProgramTest {
    @Test
    fun `test visualization`() {
        SwingUtilities.invokeLater {
            testGraphVis() // runs on EDT
        }
        readLine()
    }
}
