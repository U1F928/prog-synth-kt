package parseInstruction

fun parseStartSubroutineDefinition(words: List<InputWord>): InstructionParseResult {
    if (words.size < 2) {
        return InstructionParseResult.Error
    }

    if (words[0].value != ParsedInstruction.StartSubroutine.STRING_NAME) {
        return InstructionParseResult.Error
    }

    val subroutineName = words[1].value
    if (!isValidObjectName(subroutineName)) {
        return InstructionParseResult.Error
    }

    val startSubroutine = ParsedInstruction.StartSubroutine(SubroutineName(subroutineName))
    val remainingWords = words.drop(2)
    return InstructionParseResult.Success(
        remainingWords = remainingWords,
        parsedInstruction = startSubroutine,
    )
}

fun parseEndSubroutine(words: List<InputWord>): InstructionParseResult {
    if (words.size < 1) {
        return InstructionParseResult.Error
    }

    if (words[0].value != ParsedInstruction.EndSubroutine.STRING_NAME) {
        return InstructionParseResult.Error
    }

    return InstructionParseResult.Success(
        remainingWords = words.drop(1),
        parsedInstruction = ParsedInstruction.EndSubroutine,
    )
}

fun parseFromNode(words: List<InputWord>): InstructionParseResult {
    if (words.size < 2) {
        return InstructionParseResult.Error
    }

    if (words[0].value != ParsedInstruction.Transition.FromNode.STRING_NAME) {
        return InstructionParseResult.Error
    }

    val nodeName = words[1].value
    if (!isValidObjectName(nodeName)) {
        return InstructionParseResult.Error
    }

    val fromNode = ParsedInstruction.Transition.FromNode(NodeName(nodeName))
    val remainingWords = words.drop(2)
    return InstructionParseResult.Success(
        remainingWords = remainingWords,
        parsedInstruction = fromNode,
    )
}

fun parseGotoNode(words: List<InputWord>): InstructionParseResult {
    if (words.size < 2) {
        return InstructionParseResult.Error
    }

    if (words[0].value != ParsedInstruction.Transition.GotoNode.STRING_NAME) {
        return InstructionParseResult.Error
    }

    val nodeName = words[1].value
    if (!isValidObjectName(nodeName)) {
        return InstructionParseResult.Error
    }

    val fromNode = ParsedInstruction.Transition.GotoNode(NodeName(nodeName))
    val remainingWords = words.drop(2)
    return InstructionParseResult.Success(
        remainingWords = remainingWords,
        parsedInstruction = fromNode,
    )
}

fun parseNodeDeclaration(words: List<InputWord>): InstructionParseResult {
    if (words.size < 2) {
        return InstructionParseResult.Error
    }

    if (words[0].value != ParsedInstruction.BasicNodeDeclaration.STRING_NAME) {
        return InstructionParseResult.Error
    }

    val nodeName = words[1].value
    if (!isValidObjectName(nodeName)) {
        return InstructionParseResult.Error
    }

    val fromNode = ParsedInstruction.BasicNodeDeclaration(NodeName(nodeName))
    val remainingWords = words.drop(2)
    return InstructionParseResult.Success(
        remainingWords = remainingWords,
        parsedInstruction = fromNode,
    )
}

fun parseEntryNodeDeclaration(words: List<InputWord>): InstructionParseResult {
    if (words.size < 3) {
        return InstructionParseResult.Error
    }

    if (words[0].value + " " + words[1].value != ParsedInstruction.EntryNodeDeclaration.STRING_NAME) {
        return InstructionParseResult.Error
    }

    val nodeName = words[2].value
    if (!isValidObjectName(nodeName)) {
        return InstructionParseResult.Error
    }

    val fromNode = ParsedInstruction.EntryNodeDeclaration(NodeName(nodeName))
    val remainingWords = words.drop(3)
    return InstructionParseResult.Success(
        remainingWords = remainingWords,
        parsedInstruction = fromNode,
    )
}

fun parseExitNodeDeclaration(words: List<InputWord>): InstructionParseResult {
    if (words.size < 3) {
        return InstructionParseResult.Error
    }

    if (words[0].value + " " + words[1].value != ParsedInstruction.ExitNodeDeclaration.STRING_NAME) {
        return InstructionParseResult.Error
    }

    val nodeName = words[2].value
    if (!isValidObjectName(nodeName)) {
        return InstructionParseResult.Error
    }

    val fromNode = ParsedInstruction.ExitNodeDeclaration(NodeName(nodeName))
    val remainingWords = words.drop(3)
    return InstructionParseResult.Success(
        remainingWords = remainingWords,
        parsedInstruction = fromNode,
    )
}

fun parseSubroutineNodeDeclaration(words: List<InputWord>): InstructionParseResult {
    if (words.size < 4) {
        return InstructionParseResult.Error
    }

    if (words[0].value + " " + words[1].value != ParsedInstruction.CallNodeDeclaration.STRING_NAME) {
        return InstructionParseResult.Error
    }

    val nodeName = words[2].value
    if (!isValidObjectName(nodeName)) {
        return InstructionParseResult.Error
    }

    val subroutineName = words[3].value
    if (!isValidObjectName(subroutineName)) {
        return InstructionParseResult.Error
    }

    val callNodeDeclaration =
        ParsedInstruction.CallNodeDeclaration(
            nodeName = NodeName(nodeName),
            subroutineName = SubroutineName(subroutineName),
        )

    val remainingWords = words.drop(4)
    return InstructionParseResult.Success(
        remainingWords = remainingWords,
        parsedInstruction = callNodeDeclaration,
    )
}

fun parseTransition(words: List<InputWord>): InstructionParseResult? {
    val fromNode = parseFromNode(words)

    // TODO load multiple conditionals
    val conditions =
        when (fromNode) {
            is InstructionParseResult.Success -> {
                listOf(parseOnInputStack(fromNode.remainingWords))
            }
            is InstructionParseResult.Error -> return null
        }

    val lastCondition = conditions.last()
    val toNode =
        when (lastCondition) {
            is InstructionParseResult.Success -> {
                parseGotoNode(lastCondition.remainingWords)
            }
            is InstructionParseResult.Error -> return null
        }

    return when (toNode) {
        is InstructionParseResult.Success -> {
            InstructionParseResult.Success(
                remainingWords = words.drop(conditions.size * 3 + 2 * 2),
                parsedInstruction =
                    ParsedInstruction.Transition(
                        fromNode = (fromNode.parsedInstruction as ParsedInstruction.Transition.FromNode).nodeName,
                        toNode = (toNode.parsedInstruction as ParsedInstruction.Transition.GotoNode).nodeName,
                        conditions =
                            conditions
                                .map { (it as InstructionParseResult.Success).parsedInstruction }
                                .map { it as ParsedInstruction.Transition.TransitionCondition },
                    ),
            )
        }
        is InstructionParseResult.Error -> InstructionParseResult.Error
    }
}

fun parseOnInputStack(words: List<InputWord>): InstructionParseResult {
    if (words.size < 3) {
        return InstructionParseResult.Error
    }

    if (words[0].value + " " + words[1].value != ParsedInstruction.Transition.OnInputStack.STRING_NAME) {
        return InstructionParseResult.Error
    }

    val conditionalByte = parseConditionalByte(words[2])

    if (conditionalByte == null) return InstructionParseResult.Error

    val remainingWords = words.drop(3)
    val onInputStack = ParsedInstruction.Transition.OnInputStack(conditionalValue = conditionalByte)
    return InstructionParseResult.Success(
        remainingWords = remainingWords,
        parsedInstruction = onInputStack,
    )
}

private fun parseConditionalByte(conditionalValue: InputWord): Byte? {
    if (conditionalValue.value.length == 1) {
        val byteValue =
            conditionalValue.value
                .single()
                .code
                .toByte()

        return byteValue
    }

    if (conditionalValue.value.toByteOrNull() != null) {
        return conditionalValue.value.toByte()
    }

    if (getCharInsideQuotesAsByte(conditionalValue.value) != null) {
        return getCharInsideQuotesAsByte(conditionalValue.value)
    }

    return null
}

private fun getCharInsideQuotesAsByte(string: String): Byte? = getCharInsideQuotes(string)?.code?.toByte()

private fun getCharInsideQuotes(string: String): Char? {
    if (string.length != 3) return null

    if (string[0] == '\'' && string[2] == '\'') {
        return string[1]
    } else {
        return null
    }
}
