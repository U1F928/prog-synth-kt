package defineProgram
import kotlin.reflect.KProperty

data class ProgramDefinition(
    val subroutines: MutableList<Subroutine> = mutableListOf(),
)

fun program(initializeProgramDefinition: ProgramDefinition.() -> Unit): ProgramDefinition =
    ProgramDefinition().apply(initializeProgramDefinition)

fun ProgramDefinition.subroutine(initializeSubroutine: Subroutine.() -> Unit) =
    SubroutineDelegate(
        initializeSubroutine = initializeSubroutine,
        registerSubroutine = { this.subroutines += it },
    )

class SubroutineDelegate(
    private val initializeSubroutine: Subroutine.() -> Unit,
    private val registerSubroutine: (Subroutine) -> Unit,
) {
    operator fun provideDelegate(
        thisRef: Any?,
        property: KProperty<*>,
    ): SubroutineDelegate {
        Subroutine(property.name)
            .apply(initializeSubroutine)
            .apply(registerSubroutine)
        return this
    }

    operator fun getValue(
        thisRef: Any?,
        property: KProperty<*>,
    ): Subroutine = error("Accessing subroutine value directly is not supported")
}

data class Subroutine(
    val name: String,
    val nodes: MutableList<Node> = mutableListOf(),
)

fun Subroutine.node() =
    NodeDelegate { node ->
        nodes += node
    }

class NodeDelegate(
    private val registerNode: (Node) -> Unit,
) {
    operator fun provideDelegate(
        thisRef: Any?,
        property: KProperty<*>,
    ): NodeDelegate {
        Node(property.name)
            .apply(registerNode)
        return this
    }

    operator fun getValue(
        thisRef: Any?,
        property: KProperty<*>,
    ): Node = Node(property.name)
}

data class Node(
    val name: String,
)

data class NodePair(
    val fromNode: Node,
    val toNode: Node,
)

infix fun Node.to(toNode: Node) = NodePair(this, toNode)
