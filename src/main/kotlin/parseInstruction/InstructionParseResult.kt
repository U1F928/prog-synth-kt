package parseInstruction

sealed interface InstructionParseResult {
    data class Success(
        val remainingWords: List<InputWord>,
        val parsedInstruction: ParsedInstruction,
    ) : InstructionParseResult

    data object Error : InstructionParseResult
}

fun InstructionParseResult.onSuccess(transform: (remainingWords: List<InputWord>) -> InstructionParseResult): ChainedParsingResult =
    when (this) {
        is InstructionParseResult.Success -> {
            when (val newResult = transform(this.remainingWords)) {
                is InstructionParseResult.Success ->
                    ChainedParsingResult.AllSuccessful(
                        parsedInstructions = listOf(this, newResult),
                    )
                is InstructionParseResult.Error -> ChainedParsingResult.FailedToParse(newResult)
            }
        }
        is InstructionParseResult.Error -> ChainedParsingResult.FailedToParse(this)
    }

fun ChainedParsingResult.onSuccess(transform: (remainingWords: List<InputWord>) -> InstructionParseResult): ChainedParsingResult =
    when (this) {
        is ChainedParsingResult.AllSuccessful -> {
            val lastResult = this.parsedInstructions.last()
            val newResult = transform(lastResult.remainingWords)

            when (newResult) {
                is InstructionParseResult.Success ->
                    ChainedParsingResult.AllSuccessful(
                        parsedInstructions = this.parsedInstructions + newResult,
                    )
                is InstructionParseResult.Error -> ChainedParsingResult.FailedToParse(newResult)
            }
        }
        is ChainedParsingResult.FailedToParse -> this
    }

sealed interface ChainedParsingResult {
    data class AllSuccessful(
        val parsedInstructions: List<InstructionParseResult.Success>,
    ) : ChainedParsingResult

    data class FailedToParse(
        val firstParseFailure: InstructionParseResult.Error,
    ) : ChainedParsingResult
}
