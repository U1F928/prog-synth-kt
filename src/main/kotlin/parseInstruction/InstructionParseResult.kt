package parseInstruction

sealed interface InstructionParseResult {
    data class Success<T : ParsedInstruction>(
        val parsedInstruction: T,
        val remainingWords: List<InputWord>,
    ) : InstructionParseResult

    data object Error : InstructionParseResult
}
