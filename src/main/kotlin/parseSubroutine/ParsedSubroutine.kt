package parseSubroutine

import parseInstruction.BodyNode
import parseInstruction.EntryNode
import parseInstruction.ExitNode
import parseInstruction.InputWord
import parseInstruction.SubroutineName
import parseInstruction.SubroutineStart
import parseInstruction.Transition

sealed interface ParsedSubroutine {
    val remainingWords: List<InputWord>

    data class Success(
        val subroutineName: SubroutineName,
        val entryNode: EntryNode,
        val exitNode: ExitNode,
        val bodyNodes: List<BodyNode>,
        val transitions: List<Transition>,
        override val remainingWords: List<InputWord>
    ) : ParsedSubroutine

    data class Error(
        override val remainingWords: List<InputWord>
    ): ParsedSubroutine
}

data class ParsedSubroutineContent(
    val parsedBodyNodes: List<BodyNode>,
    val parsedTransitions: List<Transition>,
    val remainingWords: List<InputWord>,
)
