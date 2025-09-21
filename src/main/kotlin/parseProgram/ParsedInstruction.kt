package parseProgram

sealed interface ParsedInstruction{
    data class StartSubroutine(val subroutineName: SubroutineName ): ParsedInstruction {
        companion object { const val STRING_NAME = "SUBROUTINE" }
    }

    data object EndSubroutine: ParsedInstruction{
        const val STRING_NAME = "END"
    }

    data class EntryNodeDeclaration(val nodeName: String): ParsedInstruction{
        companion object { const val STRING_NAME = "ENTRY NODE" }
    }

    data class ExitNodeDeclaration(val nodeName: String): ParsedInstruction{
        companion object { const val STRING_NAME = "EXIT NODE" }
    }

    data class NodeDeclaration(val nodeName: String): ParsedInstruction{
        companion object { const val STRING_NAME = "NODE" }
    }

    data class FromNode( val nodeName: NodeName ): ParsedInstruction{
        companion object { const val STRING_NAME = "FROM" }
    }

    data class OnInputStack( val conditionalValue: Byte ): ParsedInstruction{
        companion object { const val STRING_NAME = "ON INPUT" }
    }

    data class GotoNode(val nodeName: NodeName): ParsedInstruction{
        companion object { const val STRING_NAME = "GOTO" }
    }
}


fun isValidObjectName(name: String): Boolean {
    val firstChar = name.first()
    return firstChar.isLetter() && firstChar.isLowerCase()
}

@JvmInline
value class SubroutineName(val name: String) {
    init {
        require(isValidObjectName(name))
    }
}

@JvmInline
value class NodeName(val name: String) {
    init {
        require(isValidObjectName(name))
    }
}