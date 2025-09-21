//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {

    for (i in 1..5) {
        printValuesFirst(i)
        //TIP Press <shortcut actionId="Debug"/> to start debugging your code. We have set one <icon src="AllIcons.Debugger.Db_set_breakpoint"/> breakpoint
        // for you, but you can always add more by pressing <shortcut actionId="ToggleLineBreakpoint"/>.
    }
}

fun printValuesFirst(i: Int){
    printValues2(i + 1)
    check(i < 10)
}

fun printValues2(i: Int){
    println(i + 2)
}