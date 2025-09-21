package parseProgram

import kotlin.text.first
import kotlin.text.last
import kotlin.text.replace

fun parseStartSubroutineDefinition(words: List<InputWord>): InstructionParseResult{
    if(words.size < 2)
        return InstructionParseResult.Error

    if(words[0].value != ParsedInstruction.StartSubroutine.STRING_NAME)
        return InstructionParseResult.Error

    val subroutineName = words[1].value
    if(!isValidObjectName(subroutineName)){
        return InstructionParseResult.Error
    }

    val startSubroutine = ParsedInstruction.StartSubroutine( SubroutineName(subroutineName) )
    val remainingWords = words.drop(2)
    return InstructionParseResult.Success(
        remainingWords = remainingWords,
        parsedInstruction =startSubroutine
    )
}

fun parseEndSubroutine(words: List<InputWord>): InstructionParseResult{
    if(words.size < 1)
        return InstructionParseResult.Error

    if(words[0].value != ParsedInstruction.EndSubroutine.STRING_NAME)
        return InstructionParseResult.Error

    return InstructionParseResult.Success(
        remainingWords = words.drop(1),
        parsedInstruction = ParsedInstruction.EndSubroutine
    )
}

fun parseFromNode(words: List<InputWord>): InstructionParseResult{
    if(words.size < 2)
        return InstructionParseResult.Error

    if(words[0].value != ParsedInstruction.FromNode.STRING_NAME)
        return InstructionParseResult.Error

    val nodeName = words[1].value
    if(!isValidObjectName(nodeName)){
        return InstructionParseResult.Error
    }

    val fromNode = ParsedInstruction.FromNode(NodeName(nodeName) )
    val remainingWords = words.drop(2)
    return InstructionParseResult.Success(
        remainingWords = remainingWords,
        parsedInstruction =fromNode
    )
}

fun parseGotoNode(words: List<InputWord>): InstructionParseResult{
    if(words.size < 2)
        return InstructionParseResult.Error

    if(words[0].value != ParsedInstruction.FromNode.STRING_NAME)
        return InstructionParseResult.Error

    val nodeName = words[1].value
    if(!isValidObjectName(nodeName)){
        return InstructionParseResult.Error
    }

    val fromNode = ParsedInstruction.GotoNode(NodeName(nodeName) )
    val remainingWords = words.drop(2)
    return InstructionParseResult.Success(
        remainingWords = remainingWords,
        parsedInstruction =fromNode
    )
}

fun parseNodeDeclaration(words: List<InputWord>): InstructionParseResult{
    if(words.size < 2)
        return InstructionParseResult.Error

    if(words[0].value != ParsedInstruction.NodeDeclaration.STRING_NAME)
        return InstructionParseResult.Error

    val nodeName = words[1].value
    if(!isValidObjectName(nodeName)){
        return InstructionParseResult.Error
    }

    val fromNode = ParsedInstruction.GotoNode(NodeName(nodeName) )
    val remainingWords = words.drop(2)
    return InstructionParseResult.Success(
        remainingWords = remainingWords,
        parsedInstruction =fromNode
    )
}

fun parseOnInputStack(words: List<InputWord>): InstructionParseResult{
    if(words.size < 3)
        return InstructionParseResult.Error

    if(words[0].value + " " + words[1].value != ParsedInstruction.OnInputStack.STRING_NAME)
        return InstructionParseResult.Error

    val conditionalByte = parseConditionalByte(words[2])

    if(conditionalByte == null) return InstructionParseResult.Error

    val remainingWords = words.drop(3)
    val onInputStack = ParsedInstruction.OnInputStack( conditionalValue = conditionalByte )
    return InstructionParseResult.Success(
        remainingWords = remainingWords,
        parsedInstruction = onInputStack
    )
}

private fun parseConditionalByte(conditionalValue: InputWord): Byte? {
    if(conditionalValue.value.length == 1){
        val byteValue = conditionalValue.value
            .single()
            .code
            .toByte()

        return byteValue
    }

    if(conditionalValue.value.toByteOrNull() != null){
        return conditionalValue.value.toByte()
    }

    if(getCharInsideQuotesAsByte(conditionalValue.value) != null){
        return getCharInsideQuotesAsByte(conditionalValue.value)
    }

    return null
}

private fun getCharInsideQuotesAsByte(string: String): Byte? {
    return getCharInsideQuotes(string)?.code?.toByte()
}
private fun getCharInsideQuotes(string: String): Char? {
    if(string.length != 3) return null

    if(string[0] == '\'' && string[2] == '\''){
        return string[1]
    }
    else return null
}