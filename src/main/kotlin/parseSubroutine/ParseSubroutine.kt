package parseSubroutine

import parseInstruction.BodyNode
import parseInstruction.InputWord
import parseInstruction.ParsedInstruction
import parseInstruction.Transition
import parseInstruction.parseBasicNode
import parseInstruction.parseCallNode
import parseInstruction.parseEntryNode
import parseInstruction.parseExitNode
import parseInstruction.parseSubroutineEnd
import parseInstruction.parseSubroutineStart
import parseInstruction.parseTransition
import result.Result
import result.getOrThrow
import result.isErr
import result.isOk
import result.toErr
import result.toOk

data class ParsedSubroutineContent(
    val parsedBodyNodes: List<BodyNode>,
    val parsedTransitions: List<Transition>,
    val remainingWords: List<InputWord>,
)

fun parseSubroutineContent(remainingWords: List<InputWord>): ParsedSubroutineContent {
    var currentlyRemainingWords = remainingWords
    val parsedInstructions = mutableListOf<ParsedInstruction>()
    while (true) {
        val instructions =
            listOf(
                parseTransition(currentlyRemainingWords),
                parseCallNode(currentlyRemainingWords),
                parseBasicNode(currentlyRemainingWords),
            )

        val parsedInstruction = instructions.singleOrNull { it.isOk() }
        if (parsedInstruction == null) {
            return ParsedSubroutineContent(
                parsedTransitions = parsedInstructions.filterIsInstance<Transition>(),
                parsedBodyNodes = parsedInstructions.filterIsInstance<BodyNode>(),
                remainingWords = currentlyRemainingWords,
            )
        }

        val successfullyParsedInstruction = parsedInstruction.getOrThrow()

        currentlyRemainingWords = successfullyParsedInstruction.remainingWords
        parsedInstructions.add(successfullyParsedInstruction.parsedInstruction)
    }
}

fun parseSubroutine(inputWords: List<InputWord>): Result<ParsedSubroutine.Success, ParsedSubroutine.Error> {
    val subroutineStart = parseSubroutineStart(inputWords)
    if (subroutineStart.isErr()) return ParsedSubroutine.Error.toErr()

    val entryNode = parseEntryNode(subroutineStart.v.remainingWords)
    if (entryNode.isErr()) return ParsedSubroutine.Error.toErr()

    val exitNode = parseExitNode(entryNode.v.remainingWords)
    if (exitNode.isErr()) return ParsedSubroutine.Error.toErr()

    val parsedSubroutineContent = parseSubroutineContent(exitNode.v.remainingWords)

    val subroutineEnd = parseSubroutineEnd(parsedSubroutineContent.remainingWords)
    if (subroutineEnd.isErr()) return ParsedSubroutine.Error.toErr()

    return ParsedSubroutine
        .Success(
            subroutineStart = subroutineStart.v.parsedInstruction,
            entryNode = entryNode.v.parsedInstruction,
            exitNode = exitNode.v.parsedInstruction,
            nodeDeclarations = parsedSubroutineContent.parsedBodyNodes,
            transitions = parsedSubroutineContent.parsedTransitions,
            remainingWords = exitNode.v.remainingWords,
        ).toOk()
}
