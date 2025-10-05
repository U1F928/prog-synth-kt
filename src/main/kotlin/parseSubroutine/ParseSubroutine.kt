package parseSubroutine

import parseInstruction.BodyNode
import parseInstruction.InputWord
import parseInstruction.InstructionParseResult
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
            bodyNodes = parsedSubroutineContent.parsedBodyNodes,
            transitions = parsedSubroutineContent.parsedTransitions,
            remainingWords = exitNode.v.remainingWords,
        ).toOk()
}

fun parseSubroutineContent(remainingWords: List<InputWord>): ParsedSubroutineContent {
    val parsedInstructionResults = parseSubroutineBodyInstructions(remainingWords)
    val newRemainingWords =
        if (parsedInstructionResults.isNotEmpty()) {
            parsedInstructionResults.last().remainingWords
        } else {
            emptyList()
        }
    val parsedInstructions = parsedInstructionResults.map { it.parsedInstruction }
    val parsedTransitions = parsedInstructions.filterIsInstance<Transition>()
    val parsedBodyNodes = parsedInstructions.filterIsInstance<BodyNode>()

    return ParsedSubroutineContent(
        parsedTransitions = parsedTransitions,
        parsedBodyNodes = parsedBodyNodes,
        remainingWords = newRemainingWords,
    )
}

fun parseSubroutineBodyInstructions(remainingWords: List<InputWord>): List<InstructionParseResult.Success<*>> {
    val instructions =
        listOf(
            parseTransition(remainingWords),
            parseCallNode(remainingWords),
            parseBasicNode(remainingWords),
        )

    val parsedInstruction = instructions.singleOrNull { it.isOk() }

    if (parsedInstruction == null) {
        return emptyList()
    }

    val successfullyParsedInstruction = parsedInstruction.getOrThrow()
    return listOf(successfullyParsedInstruction) + parseSubroutineBodyInstructions(successfullyParsedInstruction.remainingWords)
}
