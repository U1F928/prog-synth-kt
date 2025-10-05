package parseProgram

import parseInstruction.InputWord
import parseSubroutine.ParsedSubroutine
import parseSubroutine.parseSubroutine
import result.isErr
import result.toErr
import result.Result
import result.toOk


fun parseProgram(inputWords: List<InputWord>): Result<ParsedProgram.Success, ParsedProgram.Error>{
    val parsedSubroutines = parseSubroutines(inputWords)
    if(parsedSubroutines.isErr()) return ParsedProgram.Error(parsedSubroutines.v.remainingWords).toErr()

    val successfullyParsedSubroutines = parsedSubroutines.v
    val mainSubroutine = successfullyParsedSubroutines.single{it.subroutineName == ENTRY_SUBROUTINE_NAME}
    return ParsedProgram.Success(
        mainSubroutine = mainSubroutine,
        restOfSubroutines = successfullyParsedSubroutines.filter{it != mainSubroutine}
    ).toOk()
}

fun parseSubroutines(words: List<InputWord>): Result<List<ParsedSubroutine.Success>, ParsedProgram.Error>{
    if(words.isEmpty()) {
        return emptyList<ParsedSubroutine.Success>().toOk()
    }

    val parsedSubroutine = parseSubroutine(inputWords = words)

    if(parsedSubroutine.isErr()){
        return ParsedProgram.Error(parsedSubroutine.v.remainingWords).toErr()
    }

    val parsedSubroutines = parseSubroutines(parsedSubroutine.v.remainingWords)
    if(parsedSubroutines.isErr()){
        return ParsedProgram.Error(parsedSubroutine.v.remainingWords).toErr()
    }

    val successfullyParsedSubroutines = listOf(parsedSubroutine.v) + parsedSubroutines.v

    return successfullyParsedSubroutines.toOk()
}