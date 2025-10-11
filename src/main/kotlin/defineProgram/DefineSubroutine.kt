package defineProgram

import kotlin.reflect.KProperty

fun ProgramDefinition.SUBROUTINE(initializeSubroutine: Subroutine.() -> Unit) =
    SubroutineDelegate(
        initializeSubroutine = initializeSubroutine,
        subroutines = this.subroutines,
    )

class SubroutineDelegate(
    private val initializeSubroutine: Subroutine.() -> Unit,
    private val subroutines: MutableList<Subroutine>,
) {
    operator fun provideDelegate(
        thisRef: Any?,
        property: KProperty<*>,
    ): SubroutineDelegate {
        val subroutine = Subroutine(property.name).apply(initializeSubroutine)
        subroutines.add(subroutine)
        return this
    }

    operator fun getValue(
        thisRef: Any?,
        property: KProperty<*>,
    ): Subroutine = subroutines.single { it.name == property.name }
}

data class Subroutine(
    val name: String,
    val nodes: MutableList<Node> = mutableListOf(),
    val transitions: MutableList<Transition> = mutableListOf(),
)
