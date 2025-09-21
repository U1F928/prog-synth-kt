package parseProgram

data class InputWord(val value: String){
    init{
        require(value.isNotEmpty())
    }
}