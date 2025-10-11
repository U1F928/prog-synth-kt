package defineProgram

data class ProgramDefinition(
    val subroutines: MutableList<Subroutine> = mutableListOf(),
    val stacks: MutableList<Stack> = mutableListOf(),
)

fun PROGRAM(initializeProgramDefinition: ProgramDefinition.() -> Unit): ProgramDefinition =
    ProgramDefinition().apply(initializeProgramDefinition)
