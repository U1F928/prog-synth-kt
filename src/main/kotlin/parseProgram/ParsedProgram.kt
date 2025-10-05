package parseProgram

import parseInstruction.InputWord
import parseInstruction.SubroutineName
import parseSubroutine.ParsedSubroutine

val ENTRY_SUBROUTINE_NAME = SubroutineName("main")

interface ParsedProgram {
    data class Success(
        val mainSubroutine: ParsedSubroutine,
        val restOfSubroutines: List<ParsedSubroutine>,
    ) {
        init {
            require(mainSubroutine !in restOfSubroutines)
        }
    }

    data class Error(
        val remainingWords: List<InputWord>
    ): ParsedProgram

}
