package evaluateProgram

tailrec fun getProgramStateInGivenNumberOfSteps(
    programDefinition: ProgramDefinition,
    programState: ProgramState,
    evaluationConfig: EvaluationConfig,
    numberOfSteps: UInt,
): ProgramState {
    if (numberOfSteps == 0u) return programState

    val nextProgramState =
        getNextProgramState(
            programDefinition = programDefinition,
            programState = programState,
            evaluationConfig = evaluationConfig,
        )

    return getProgramStateInGivenNumberOfSteps(
        programDefinition = programDefinition,
        programState = nextProgramState,
        evaluationConfig = evaluationConfig,
        numberOfSteps = numberOfSteps - 1u,
    )
}

fun getNextProgramState(
    programDefinition: ProgramDefinition,
    programState: ProgramState,
    evaluationConfig: EvaluationConfig,
): ProgramState {
    val inputValue = programState.inputValues.firstOrNull()
    val currentSubroutineState = programState.subroutineStack.last()
    val currentSubroutine = programDefinition.allSubroutines().single { it.name == currentSubroutineState.subroutineName }
    val currentNode = currentSubroutineState.currentNode
    val transitions = currentSubroutine.transitions

    val matchingTransitions =
        getMatchingTransitions(
            currentNode = currentNode,
            inputValue = inputValue,
            transitions = transitions,
        )

    if (currentNode is ExitNode) {
        return programState.copy(
            subroutineStack = programState.subroutineStack.dropLast(1),
        )
    }

    if (matchingTransitions.size == 1) {
        val matchingTransition = matchingTransitions.single()
        val nextNode = matchingTransition.toNode
        val newSubroutineStack =
            getNewSubroutineStack(
                subroutineStack = programState.subroutineStack,
                nextNode = nextNode,
            )

        val newOutputValues =
            getNewOutputValues(
                matchingTransition = matchingTransition,
                currentOutputValues = programState.outputValues,
            )

        return programState.copy(
            inputValues = programState.inputValues.drop(1),
            subroutineStack = newSubroutineStack,
            outputValues = newOutputValues,
        )
    }

    if (matchingTransitions.isEmpty() && evaluationConfig.skipInputValueWithNoMatchingTransition) {
        return programState.copy(
            inputValues = programState.inputValues.drop(1),
        )
    }

    error("This should not be reachable!")
}

fun getMatchingTransitions(
    currentNode: Node,
    inputValue: UByte?,
    transitions: Set<Transition>,
): List<Transition> {
    val transitionsFromCurrentNode = transitions.filter { it.fromNode == currentNode }

    val unconditionalTransitions = transitionsFromCurrentNode.filter { it.conditions.isEmpty() }

    val onInputStackTransitions =
        transitionsFromCurrentNode
            .filter { it.conditions.isNotEmpty() }
            .filter {
                val condition = it.conditions.single() as Transition.Condition.OnInputStack
                condition.conditionValue == inputValue
            }

    return unconditionalTransitions + onInputStackTransitions
}

fun getNewOutputValues(
    matchingTransition: Transition,
    currentOutputValues: List<UByte>,
): List<UByte> {
    val actions = matchingTransition.actions
    if (actions.isEmpty()) {
        return currentOutputValues
    }

    val action = actions.single() as Transition.Action.PushToOutputStack
    return currentOutputValues + action.outputValue
}

fun getNewSubroutineStack(
    subroutineStack: List<SubroutineState>,
    nextNode: Node,
): List<SubroutineState> =
    when (nextNode) {
        is CallNode -> {
            val newSubroutineState =
                SubroutineState(
                    subroutineName = nextNode.subroutineName,
                    currentNode = nextNode,
                )
            subroutineStack + newSubroutineState
        }
        else -> {
            val currentSubroutineState = subroutineStack.last()
            val newSubroutineState =
                currentSubroutineState.copy(
                    currentNode = nextNode,
                )

            subroutineStack.dropLast(1) + newSubroutineState
        }
    }
