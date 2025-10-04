package parseSubroutine

import parseInstruction.BasicNode
import parseInstruction.BodyNode
import parseInstruction.EntryNode
import parseInstruction.ExitNode
import parseInstruction.InputWord
import parseInstruction.SubroutineStart
import parseInstruction.Transition

// TODO
sealed interface ParsedSubroutine {
    data class Success(
        val subroutineStart: SubroutineStart,
        val entryNode: EntryNode,
        val exitNode: ExitNode,
        val nodeDeclarations: List<BodyNode>,
        val transitions: List<Transition>,
        val remainingWords: List<InputWord>,
    )

    data object Error : ParsedSubroutine
}
