package defineProgram
import parseInstruction.NodeName
import kotlin.reflect.KProperty

sealed interface NodeDefinitionToHandle

data object BasicNodeDefinition : NodeDefinitionToHandle

data class CallNodeDefinition(
    val subroutine: Subroutine,
) : NodeDefinitionToHandle

enum class NodeType {
    BASIC_NODE,
    CALL_NODE,
}

fun Subroutine.NODE() =
    NodeDelegate(
        nodeDefinitionToHandle = BasicNodeDefinition,
        nodes = this.nodes,
    )

fun Subroutine.CALL_NODE(subroutine: Subroutine) =
    NodeDelegate(
        nodeDefinitionToHandle = CallNodeDefinition(subroutine = subroutine),
        nodes = this.nodes,
    )

class NodeDelegate(
    private val nodeDefinitionToHandle: NodeDefinitionToHandle,
    private val nodes: MutableList<CustomNode>,
) {
    operator fun provideDelegate(
        thisRef: Any?,
        property: KProperty<*>,
    ): NodeDelegate {
        val node =
            when (nodeDefinitionToHandle) {
                is BasicNodeDefinition -> BasicNode(name = NodeName(property.name))
                is CallNodeDefinition ->
                    CallNode(
                        name = NodeName(property.name),
                        subroutine = nodeDefinitionToHandle.subroutine,
                    )
            }

        nodes.add(node)
        return this
    }

    operator fun getValue(
        thisRef: Any?,
        property: KProperty<*>,
    ): Node =
        nodes
            .filterIsInstance<CustomNode>()
            .single { it.name == NodeName(property.name) }
}

sealed interface Node

sealed interface CustomNode : Node {
    val name: NodeName
}

data object EntryNode : Node

data object ExitNode : Node

data class BasicNode(
    override val name: NodeName,
) : CustomNode

data class CallNode(
    override val name: NodeName,
    val subroutine: Subroutine,
) : CustomNode
