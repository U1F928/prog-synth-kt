package defineProgram
import parseInstruction.NodeName
import parseInstruction.SubroutineName
import kotlin.reflect.KProperty

sealed interface NodeDefinitionToHandle

data object BasicNodeDefinition : NodeDefinitionToHandle

data class CallNodeDefinition(
    val subroutineName: String,
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

fun Subroutine.CALL_NODE() =
    NodeDelegate(
        nodeDefinitionToHandle = CallNodeDefinition(subroutineName = this.name),
        nodes = this.nodes,
    )

class NodeDelegate(
    private val nodeDefinitionToHandle: NodeDefinitionToHandle,
    private val nodes: MutableList<Node>,
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
                        subroutineName = SubroutineName(nodeDefinitionToHandle.subroutineName),
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
            .filterIsInstance<NodeWithCustomName>()
            .single { it.name == NodeName(property.name) }
}

sealed interface Node

sealed interface NodeWithCustomName : Node {
    val name: NodeName
}

data object EntryNode : Node

data object ExitNode : Node

data class BasicNode(
    override val name: NodeName,
) : NodeWithCustomName

data class CallNode(
    override val name: NodeName,
    val subroutineName: SubroutineName,
) : NodeWithCustomName
