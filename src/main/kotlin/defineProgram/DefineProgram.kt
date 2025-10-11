package defineProgram
import kotlin.reflect.KProperty

data class ProgramDefinition(
    val subroutines: MutableList<Subroutine> = mutableListOf(),
)

fun program(initializeProgramDefinition: ProgramDefinition.() -> Unit): ProgramDefinition =
    ProgramDefinition().apply(initializeProgramDefinition)
