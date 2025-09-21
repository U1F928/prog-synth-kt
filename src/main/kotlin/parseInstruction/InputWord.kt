package parseInstruction

data class InputWord(
    val value: String,
) {
    init {
        require(value.isNotEmpty())
    }
}
