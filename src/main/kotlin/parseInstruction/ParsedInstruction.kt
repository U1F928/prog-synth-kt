package parseInstruction

sealed interface ParsedInstruction

data class SubroutineStart(
    val subroutineName: SubroutineName,
) : ParsedInstruction {
    companion object {
        const val STRING_NAME = "SUBROUTINE"
    }
}

data object EndSubroutine : ParsedInstruction {
    const val STRING_NAME = "END"
}

sealed interface NodeDeclaration : ParsedInstruction

data class EntryNode(
    val nodeName: NodeName,
) : NodeDeclaration {
    companion object {
        const val STRING_NAME = "ENTRY NODE"
    }
}

data class ExitNode(
    val nodeName: NodeName,
) : NodeDeclaration {
    companion object {
        const val STRING_NAME = "EXIT NODE"
    }
}

sealed interface BodyNode : NodeDeclaration

data class CallNode(
    val nodeName: NodeName,
    val subroutineName: SubroutineName,
) : BodyNode {
    companion object {
        const val STRING_NAME = "CALL NODE"
    }
}

data class BasicNode(
    val nodeName: NodeName,
) : BodyNode {
    companion object {
        const val STRING_NAME = "NODE"
    }
}

data class Transition(
    val fromNode: NodeName,
    val toNode: NodeName,
    val conditions: List<OnInputStack>,
) : ParsedInstruction {
    data class FromNode(
        val nodeName: NodeName,
    ) : ParsedInstruction {
        companion object {
            const val STRING_NAME = "FROM"
        }
    }

    data class GotoNode(
        val nodeName: NodeName,
    ) : ParsedInstruction {
        companion object {
            const val STRING_NAME = "GOTO"
        }
    }

    sealed interface TransitionCondition : ParsedInstruction

    data class OnInputStack(
        val conditionalValue: Byte,
    ) : TransitionCondition {
        companion object {
            const val STRING_NAME = "ON INPUT"
        }
    }
}

fun isValidObjectName(name: String): Boolean {
    val firstChar = name.first()
    return firstChar.isLetter() && firstChar.isLowerCase()
}

@JvmInline
value class SubroutineName(
    val name: String,
) {
    init {
        require(isValidObjectName(name))
    }
}

@JvmInline
value class NodeName(
    val name: String,
) {
    init {
        require(isValidObjectName(name))
    }
}
