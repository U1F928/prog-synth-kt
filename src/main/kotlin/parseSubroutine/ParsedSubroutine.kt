package parseSubroutine

import parseInstruction.BasicNodeDeclaration
import parseInstruction.EntryNodeDeclaration
import parseInstruction.ExitNodeDeclaration
import parseInstruction.ParsedInstruction
import parseInstruction.StartSubroutine
import parseInstruction.Transition

// TODO
sealed interface ParsedSubroutine {
    data class Success(
        val startSubroutine: StartSubroutine,
        val entryNodeDeclaration: EntryNodeDeclaration,
        val exitNodeDeclaration: ExitNodeDeclaration,
        val nodeDeclarations: List<BasicNodeDeclaration>,
        val transitions: List<Transition>,
    )

    data object Error : ParsedSubroutine
}
