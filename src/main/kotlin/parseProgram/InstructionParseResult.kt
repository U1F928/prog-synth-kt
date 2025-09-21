package parseProgram

sealed interface InstructionParseResult {
    data class Success(
        val remainingWords: List<InputWord>,
        val parsedInstruction: ParsedInstruction
    ): InstructionParseResult

    data object Error: InstructionParseResult
}

sealed interface ConditionalValueParseResult{
    data class Success(val value: Byte)
    data object Error
}


