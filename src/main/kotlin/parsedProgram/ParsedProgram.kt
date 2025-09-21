package parsedProgram

import parseInstruction.ParsedInstruction
import parseSubroutine.ParsedSubroutine

// TODO
interface ParsedProgram {
    data class Success(
        val mainSubroutine: ParsedSubroutine,
        val restOfSubroutines: List<ParsedSubroutine>,
    ) {
        init {
            require(mainSubroutine !in restOfSubroutines)
        }
    }

    data object Error
}
