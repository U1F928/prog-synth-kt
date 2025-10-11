package defineProgram

import evaluateProgram.SubroutineDefinition

data class ProgramDefinition(
    val subroutines: MutableList<Subroutine> = mutableListOf(),
    val stacks: MutableList<CustomStack> = mutableListOf(),
)

fun PROGRAM(initializeProgramDefinition: ProgramDefinition.() -> Unit): ProgramDefinition =
    ProgramDefinition().apply(initializeProgramDefinition)
