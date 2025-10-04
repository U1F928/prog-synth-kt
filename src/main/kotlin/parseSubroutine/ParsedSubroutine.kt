package parseSubroutine

import parseInstruction.BasicNodeDeclaration
import parseInstruction.EntryNodeDeclaration
import parseInstruction.ExitNodeDeclaration
import parseInstruction.SubroutineStart
import parseInstruction.Transition

// TODO
sealed interface ParsedSubroutine {
    data class Success(
        val subroutineStart: SubroutineStart,
        val entryNodeDeclaration: EntryNodeDeclaration,
        val exitNodeDeclaration: ExitNodeDeclaration,
        val nodeDeclarations: List<BasicNodeDeclaration>,
        val transitions: List<Transition>,
    )

    data object Error : ParsedSubroutine
}
