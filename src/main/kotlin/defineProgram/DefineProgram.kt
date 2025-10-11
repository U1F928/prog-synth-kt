package defineProgram

data class ProgramDefinition(
    val subroutines: MutableList<Subroutine> = mutableListOf(),
)

fun PROGRAM(initializeProgramDefinition: ProgramDefinition.() -> Unit): ProgramDefinition =
    ProgramDefinition().apply(initializeProgramDefinition)
