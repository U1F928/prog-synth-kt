package parseInstruction

sealed interface InstructionParseResult {
    val remainingWords: List<InputWord>

    data class Success<T : ParsedInstruction>(
        override val remainingWords: List<InputWord>,
        val parsedInstruction: T,
    ) : InstructionParseResult

    data class Error(
        override val remainingWords: List<InputWord>,
    ) : InstructionParseResult
}
