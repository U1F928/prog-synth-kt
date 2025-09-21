package parseSubroutine

import parseInstruction.ParsedInstruction

sealed interface ParsedSubroutine {
    data class Success(
        val startSubroutine: ParsedInstruction.StartSubroutine,
        val entryNodeDeclaration: ParsedInstruction.EntryNodeDeclaration,
        val exitNodeDeclaration: ParsedInstruction.ExitNodeDeclaration,
        val nodeDeclarations: List<ParsedInstruction.BasicNodeDeclaration>,
    )
}
