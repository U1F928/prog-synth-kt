package parseSubroutine

import parseInstruction.InputWord
import parseInstruction.parseBasicNode
import parseInstruction.parseCallNode
import parseInstruction.parseEntryNode
import parseInstruction.parseExitNode
import parseInstruction.parseSubroutineStart
import parseInstruction.parseTransition
import result.isErr

fun parseSubroutine(inputWords: List<InputWord>): ParsedSubroutine {
    val subroutineStart = parseSubroutineStart(inputWords)
    if (subroutineStart.isErr()) return ParsedSubroutine.Error

    val entryNode = parseEntryNode(subroutineStart.v.remainingWords)
    if (entryNode.isErr()) return ParsedSubroutine.Error

    val exitNode = parseExitNode(entryNode.v.remainingWords)
    if (exitNode.isErr()) return ParsedSubroutine.Error

    var remainingWords = exitNode.v.remainingWords
    while (true) {
        val transition = parseTransition(remainingWords)
        val callNode = parseCallNode(remainingWords)
        val basicNode = parseBasicNode(remainingWords)
    }

    ParsedSubroutine.Success(
        subroutineStart = subroutineStart.v.parsedInstruction,
        entryNode = entryNode.v.parsedInstruction,
        exitNode = exitNode.v.parsedInstruction,
        nodeDeclarations = TODO(),
        transitions = TODO(),
        remainingWords = exitNode.v.remainingWords,
    )
}
