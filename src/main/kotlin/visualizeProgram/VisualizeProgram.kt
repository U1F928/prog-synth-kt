package visualizeProgram

import org.graphstream.graph.implementations.SingleGraph
import java.util.UUID
import kotlin.concurrent.thread

fun testGraphVis() {
    System.setProperty("org.graphstream.ui", "swing")
    System.setProperty("gs.gui.renderer", "swing")
    val graph = SingleGraph("My Graph-${UUID.randomUUID()}")

    val node1 = graph.addNode("node1-${UUID.randomUUID()}")
    val node2 = graph.addNode("node2-${UUID.randomUUID()}")
    val edge1 =
        graph.addEdge(
            "edge1-${UUID.randomUUID()}",
            node1,
            node2,
            true,
        )
    graph.display()
}
