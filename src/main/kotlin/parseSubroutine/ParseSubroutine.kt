package parseSubroutine

import parseInstruction.InputWord
import parseInstruction.parseEntryNodeDeclaration
import parseInstruction.parseSubroutineStartDeclaration
import result.isErr

fun parseSubroutine(inputWords: List<InputWord>): ParsedSubroutine {
    val subroutineStart = parseSubroutineStartDeclaration(inputWords)
    if (subroutineStart.isErr()) return ParsedSubroutine.Error
    val entryNode = parseEntryNodeDeclaration()
    ParsedSubroutine.Success(
        subroutineStart = TODO(),
        entryNode = TODO(),
        exitNode = TODO(),
        nodeDeclarations = TODO(),
        transitions = TODO(),
    )
}
