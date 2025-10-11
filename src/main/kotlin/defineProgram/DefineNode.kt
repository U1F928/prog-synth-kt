package defineProgram
import kotlin.reflect.KProperty

fun Subroutine.NODE() = NodeDelegate(nodes = this.nodes)

class NodeDelegate(
    private val nodes: MutableList<Node>,
) {
    operator fun provideDelegate(
        thisRef: Any?,
        property: KProperty<*>,
    ): NodeDelegate {
        val node = Node(name = property.name)
        nodes.add(node)
        return this
    }

    operator fun getValue(
        thisRef: Any?,
        property: KProperty<*>,
    ): Node = nodes.single { it.name == property.name }
}

data class Node(
    val name: String,
)
