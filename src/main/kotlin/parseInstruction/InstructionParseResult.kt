package parseInstruction

sealed interface InstructionParseResult {
    data class Success<T : ParsedInstruction>(
        val remainingWords: List<InputWord>,
        val parsedInstruction: T,
    ) : InstructionParseResult

    data object Error : InstructionParseResult
}
