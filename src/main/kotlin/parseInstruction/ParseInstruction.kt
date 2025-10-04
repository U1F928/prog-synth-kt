package parseInstruction

import result.Result
import result.err
import result.isErr
import result.map
import result.toErr
import result.toOk

fun parseStartSubroutineDefinition(
    words: List<InputWord>,
): Result<InstructionParseResult.Success<SubroutineStart>, InstructionParseResult.Error> {
    if (words.size < 2) {
        return InstructionParseResult.Error.toErr()
    }

    if (words[0].value != SubroutineStart.STRING_NAME) {
        return InstructionParseResult.Error.toErr()
    }

    val subroutineName = words[1].value
    if (!isValidObjectName(subroutineName)) {
        return InstructionParseResult.Error.toErr()
    }

    val subroutineStart = SubroutineStart(SubroutineName(subroutineName))
    val remainingWords = words.drop(2)
    return InstructionParseResult
        .Success(
            remainingWords = remainingWords,
            parsedInstruction = subroutineStart,
        ).toOk()
}

fun parseEndSubroutine(words: List<InputWord>): Result<InstructionParseResult.Success<EndSubroutine>, InstructionParseResult.Error> {
    if (words.size < 1) {
        return InstructionParseResult.Error.toErr()
    }

    if (words[0].value != EndSubroutine.STRING_NAME) {
        return InstructionParseResult.Error.toErr()
    }

    return InstructionParseResult
        .Success(
            remainingWords = words.drop(1),
            parsedInstruction = EndSubroutine,
        ).toOk()
}

fun parseFromNode(words: List<InputWord>): Result<InstructionParseResult.Success<Transition.FromNode>, InstructionParseResult.Error> {
    if (words.size < 2) {
        return InstructionParseResult.Error.toErr()
    }

    if (words[0].value != Transition.FromNode.STRING_NAME) {
        return InstructionParseResult.Error.toErr()
    }

    val nodeName = words[1].value
    if (!isValidObjectName(nodeName)) {
        return InstructionParseResult.Error.toErr()
    }

    val fromNode = Transition.FromNode(NodeName(nodeName))
    val remainingWords = words.drop(2)
    return InstructionParseResult
        .Success(
            remainingWords = remainingWords,
            parsedInstruction = fromNode,
        ).toOk()
}

fun parseGotoNode(words: List<InputWord>): Result<InstructionParseResult.Success<Transition.GotoNode>, InstructionParseResult.Error> {
    if (words.size < 2) {
        return InstructionParseResult.Error.toErr()
    }

    if (words[0].value != Transition.GotoNode.STRING_NAME) {
        return InstructionParseResult.Error.toErr()
    }

    val nodeName = words[1].value
    if (!isValidObjectName(nodeName)) {
        return InstructionParseResult.Error.toErr()
    }

    val fromNode = Transition.GotoNode(NodeName(nodeName))
    val remainingWords = words.drop(2)
    return InstructionParseResult
        .Success(
            remainingWords = remainingWords,
            parsedInstruction = fromNode,
        ).toOk()
}

fun parseBasicNodeDeclaration(
    words: List<InputWord>,
): Result<InstructionParseResult.Success<BasicNodeDeclaration>, InstructionParseResult.Error> {
    if (words.size < 2) {
        return InstructionParseResult.Error.toErr()
    }

    if (words[0].value != BasicNodeDeclaration.STRING_NAME) {
        return InstructionParseResult.Error.toErr()
    }

    val nodeName = words[1].value
    if (!isValidObjectName(nodeName)) {
        return InstructionParseResult.Error.toErr()
    }

    val fromNode = BasicNodeDeclaration(NodeName(nodeName))
    val remainingWords = words.drop(2)
    return InstructionParseResult
        .Success(
            remainingWords = remainingWords,
            parsedInstruction = fromNode,
        ).toOk()
}

fun parseEntryNodeDeclaration(words: List<InputWord>): InstructionParseResult {
    if (words.size < 3) {
        return InstructionParseResult.Error
    }

    if (words[0].value + " " + words[1].value != EntryNodeDeclaration.STRING_NAME) {
        return InstructionParseResult.Error
    }

    val nodeName = words[2].value
    if (!isValidObjectName(nodeName)) {
        return InstructionParseResult.Error
    }

    val fromNode = EntryNodeDeclaration(NodeName(nodeName))
    val remainingWords = words.drop(3)
    return InstructionParseResult.Success(
        remainingWords = remainingWords,
        parsedInstruction = fromNode,
    )
}

fun parseExitNodeDeclaration(
    words: List<InputWord>,
): Result<InstructionParseResult.Success<ExitNodeDeclaration>, InstructionParseResult.Error> {
    if (words.size < 3) {
        return InstructionParseResult.Error.toErr()
    }

    if (words[0].value + " " + words[1].value != ExitNodeDeclaration.STRING_NAME) {
        return InstructionParseResult.Error.toErr()
    }

    val nodeName = words[2].value
    if (!isValidObjectName(nodeName)) {
        return InstructionParseResult.Error.toErr()
    }

    val fromNode = ExitNodeDeclaration(NodeName(nodeName))
    val remainingWords = words.drop(3)
    return InstructionParseResult
        .Success(
            remainingWords = remainingWords,
            parsedInstruction = fromNode,
        ).toOk()
}

fun parseCallNodeDeclaration(
    words: List<InputWord>,
): Result<InstructionParseResult.Success<CallNodeDeclaration>, InstructionParseResult.Error> {
    if (words.size < 4) {
        return InstructionParseResult.Error.toErr()
    }

    if (words[0].value + " " + words[1].value != CallNodeDeclaration.STRING_NAME) {
        return InstructionParseResult.Error.toErr()
    }

    val nodeName = words[2].value
    if (!isValidObjectName(nodeName)) {
        return InstructionParseResult.Error.toErr()
    }

    val subroutineName = words[3].value
    if (!isValidObjectName(subroutineName)) {
        return InstructionParseResult.Error.toErr()
    }

    val callNodeDeclaration =
        CallNodeDeclaration(
            nodeName = NodeName(nodeName),
            subroutineName = SubroutineName(subroutineName),
        )

    val remainingWords = words.drop(4)
    return InstructionParseResult
        .Success(
            remainingWords = remainingWords,
            parsedInstruction = callNodeDeclaration,
        ).toOk()
}

fun parseTransition(words: List<InputWord>): Result<InstructionParseResult.Success<Transition>, InstructionParseResult.Error> {
    val fromNode = parseFromNode(words)
    if (fromNode.isErr()) return InstructionParseResult.Error.toErr()

    val onInput = parseOnInputStack(fromNode.value.remainingWords)
    if (onInput.isErr()) return InstructionParseResult.Error.toErr()

    val gotoNode = parseGotoNode(onInput.value.remainingWords)
    if (gotoNode.isErr()) return InstructionParseResult.Error.toErr()

    val transition =
        Transition(
            fromNode = fromNode.value.parsedInstruction.nodeName,
            conditions = listOf(onInput.value.parsedInstruction),
            toNode = gotoNode.value.parsedInstruction.nodeName,
        )

    return InstructionParseResult
        .Success(
            remainingWords = words.drop(3 + 2 * 2),
            parsedInstruction = transition,
        ).toOk()
}

fun parseOnInputStack(
    words: List<InputWord>,
): Result<InstructionParseResult.Success<Transition.OnInputStack>, InstructionParseResult.Error> {
    if (words.size < 3) {
        return InstructionParseResult.Error.toErr()
    }

    if (words[0].value + " " + words[1].value != Transition.OnInputStack.STRING_NAME) {
        return InstructionParseResult.Error.toErr()
    }

    val conditionalByte = parseConditionalByte(words[2])

    if (conditionalByte.isErr()) return InstructionParseResult.Error.toErr()

    val remainingWords = words.drop(3)
    val onInputStack = Transition.OnInputStack(conditionalValue = conditionalByte.value)
    return InstructionParseResult
        .Success(
            remainingWords = remainingWords,
            parsedInstruction = onInputStack,
        ).toOk()
}

private fun parseConditionalByte(conditionalValue: InputWord): Result<Byte, Unit> {
    if (conditionalValue.value.length == 1) {
        val byteValue =
            conditionalValue.value
                .single()
                .code
                .toByte()

        return byteValue.toOk()
    }

    if (conditionalValue.value.toByteOrNull() != null) {
        return conditionalValue.value.toByte().toOk()
    }

    return getCharInsideQuotesAsByte(conditionalValue.value)
}

private fun getCharInsideQuotesAsByte(string: String) = getCharInsideQuotes(string).map { it.code.toByte() }

private fun getCharInsideQuotes(string: String): Result<Char, Unit> {
    if (string.length != 3) return err()

    if (string[0] == '\'' && string[2] == '\'') {
        return string[1].toOk()
    } else {
        return err()
    }
}
