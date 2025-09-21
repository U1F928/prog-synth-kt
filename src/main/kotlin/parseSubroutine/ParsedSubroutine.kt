package parseSubroutine

import parseInstruction.ParsedInstruction

// TODO
sealed interface ParsedSubroutine {
    data class Success(
        val startSubroutine: ParsedInstruction.StartSubroutine,
        val entryNodeDeclaration: ParsedInstruction.EntryNodeDeclaration,
        val exitNodeDeclaration: ParsedInstruction.ExitNodeDeclaration,
        val nodeDeclarations: List<ParsedInstruction.BasicNodeDeclaration>,
        val transitions: List<ParsedInstruction.Transition>,
    )

    data object Error: ParsedSubroutine
}
