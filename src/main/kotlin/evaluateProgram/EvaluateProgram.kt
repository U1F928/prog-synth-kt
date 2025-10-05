package evaluateProgram

fun getNextProgramState(
    programDefinition: ProgramDefinition,
    programState: ProgramState,
    evaluationConfig: EvaluationConfig
): ProgramState {
    val inputValue = programState.inputValues.first()
    val currentSubroutineState = programState.subroutineStack.last()
    val currentSubroutine = programDefinition.allSubroutines().single{it.name == currentSubroutineState.subroutineName}
    val currentNode = currentSubroutineState.currentNode
    val transitions = currentSubroutine.transitions

    val matchingTransitions = transitions
        .filter { it.fromNode == currentNode }
        .filter {
            val condition = it.conditions.single() as Transition.Condition.OnInputStack
            condition.conditionValue == inputValue
        }


    if(currentNode is ExitNode){
        return programState.copy(
            subroutineStack = programState.subroutineStack.dropLast(1)
        )
    }

    if (matchingTransitions.size == 1) {
        val matchingTransition = matchingTransitions.single()
        val nextNode = matchingTransition.toNode
        val newSubroutineStack = getNewSubroutineStack(
            subroutineStack = programState.subroutineStack,
            nextNode = nextNode
        )

        val newOutputValues = getNewOutputValues(
            matchingTransition = matchingTransition,
            currentOutputValues = programState.outputValues
        )

        return programState.copy(
            inputValues = programState.inputValues.drop(1),
            subroutineStack = newSubroutineStack,
            outputValues = newOutputValues
        )
    }

    if (matchingTransitions.isEmpty() && evaluationConfig.skipInputValueWithNoMatchingTransition) {
        return programState.copy(
            inputValues = programState.inputValues.drop(1)
        )
    }

    error("This should not be reachable!")
}

fun getNewOutputValues(
    matchingTransition: Transition,
    currentOutputValues: List<UByte>
): List<UByte> {
    val actions = matchingTransition.actions
    if(actions.isEmpty()){
        return currentOutputValues
    }

    val action = actions.single() as Transition.Action.PushToOutputStack
    return currentOutputValues + action.outputValue
}

fun getNewSubroutineStack(
    subroutineStack: List<SubroutineState>,
    nextNode: Node
): List<SubroutineState> {
    return when(nextNode){
        is CallNode -> {
            val newSubroutineState = SubroutineState(
                subroutineName = nextNode.subroutineName,
                currentNode = nextNode

            )
            subroutineStack + newSubroutineState
        }
        else -> {
            val currentSubroutineState = subroutineStack.last()
            val newSubroutineState = currentSubroutineState.copy(
                currentNode = nextNode
            )

            subroutineStack.dropLast(1) + newSubroutineState
        }
    }
}

