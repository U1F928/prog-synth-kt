@file:Suppress("ktlint")
package defineProgram
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class DefineProgramTest {

    @Nested
    inner class `test subroutine definition`{
        @Test
        fun `define program with a single empty subroutine`() {
            // given & when
            val program = PROGRAM {
                val main by SUBROUTINE { }
            }

            // then
            val subroutine = program.subroutines.single()
            assertThat(subroutine.name.value).isEqualTo("main")
            assertThat(subroutine.nodes).isEmpty()
            assertThat(subroutine.transitions).isEmpty()
        }

        @Test
        fun `define program with multiple empty subroutines`() {
            // given & when
            val program = PROGRAM {
                val main by SUBROUTINE { }
                val subroutineA by SUBROUTINE { }
                val subroutineB by SUBROUTINE { }
            }

            // then
            assertThat(program.subroutines).hasSize(3)
            val subroutineNames = program.subroutines.map{it.name.value}
            assertThat(subroutineNames).containsExactly(
                "main",
                "subroutineA",
                "subroutineB"
            )
        }
    }

    @Nested
    inner class `test node definition`{

        @Test
        fun `define program with a single BasicNode`() {
            // given & when
            val program = PROGRAM {
                val main by SUBROUTINE {
                    val myNode by NODE()
                }
            }

            // then
            val subroutine = program.subroutines.single()
            assertThat(subroutine.transitions).isEmpty()

            val node = subroutine.nodes.single() as BasicNode
            assertThat(node.name.value).isEqualTo("myNode")
        }

        @Test
        fun `define program with multiple BasicNodes`() {
            // given & when
            val program = PROGRAM {
                val main by SUBROUTINE {
                    val myNode1 by NODE()
                    val myNode2 by NODE()
                    val myNode3 by NODE()
                }
            }

            // then
            val subroutine = program.subroutines.single()
            assertThat(subroutine.nodes).hasSize(3)
            val nodeNames = subroutine.nodes.map{it.name.value}
            assertThat(nodeNames).containsExactly(
                "myNode1",
                "myNode2",
                "myNode3"
            )
        }

        @Test
        fun `define program with a single CallNode`() {
            // given & when
            val program = PROGRAM {
                val main by SUBROUTINE { }
                val secondSubroutine by SUBROUTINE {
                    val myNode by CALL_NODE(main)
                }
            }

            // then
            val subroutine = program.subroutines.last()
            assertThat(subroutine.transitions).isEmpty()

            val node = subroutine.nodes.single() as CallNode
            assertThat(node.subroutine.name.value).isEqualTo("main")
        }
    }


    @Nested
    inner class `test custom stack definition`{

        @Test
        fun `define program with a single custom stack`(){
            // given & when
            val program = PROGRAM {
                val myStack by STACK()
                val main by SUBROUTINE { }
            }

            // then
            val stack = program.stacks.single()
            assertThat(stack.name.value).isEqualTo("myStack")
        }

        @Test
        fun `define program with multiple custom stacks`(){
            // given & when
            val program = PROGRAM {
                val myStack1 by STACK()
                val myStack2 by STACK()
                val myStack3 by STACK()
                val main by SUBROUTINE { }
            }

            // then
            assertThat(program.stacks).hasSize(3)
            val stackNames = program.stacks.map{it.name.value }
            assertThat(stackNames).containsExactly(
                "myStack1",
                "myStack2",
                "myStack3"
            )
        }
    }
    @Nested
    inner class `test transition definition`{
        @Test
        fun `define program with a single unconditional transition`() {
            // given & when
            val program = PROGRAM {
                val main by SUBROUTINE {
                    FROM(ENTRY_NODE).GOTO(EXIT_NODE)
                }
            }

            // then
            assertThat(program.stacks).isEmpty()

            // verify there is only single subroutine
            assertThat(program.subroutines).hasSize(1)
            val subroutine = program.subroutines.single()

            // verify subroutine properties
            assertThat(subroutine.name.value).isEqualTo("main")
            assertThat(subroutine.nodes).isEmpty()
            assertThat(subroutine.transitions).hasSize(1)

            // verify transition properties
            val transition = subroutine.transitions.single()
            assertThat(transition.fromNode).isEqualTo(subroutine.ENTRY_NODE)
            assertThat(transition.toNode).isEqualTo(subroutine.EXIT_NODE)
            assertThat(transition.conditions).isEmpty()
            assertThat(transition.actions).isEmpty()
        }

        @Test
        fun `define program with a single INPUT conditional transition`() {
            // given & when
            val program = PROGRAM {
                val main by SUBROUTINE {
                    FROM(ENTRY_NODE)
                        .ON(INPUT, 34)
                        .GOTO(EXIT_NODE)
                }
            }

            // then
            val subroutine = program.subroutines.single()
            val transition = subroutine.transitions.single()

            assertThat(transition.fromNode).isEqualTo(subroutine.ENTRY_NODE)
            assertThat(transition.toNode).isEqualTo(subroutine.EXIT_NODE)

            // verify the condition
            val condition = transition.conditions.single() as Transition.OnStack
            assertThat(condition.stack).isEqualTo(INPUT)
            assertThat(condition.conditionalValue).isEqualTo(34.toUByte())

        }

        @Test
        fun `define program with an unconditional OUTPUT push transition`() {
            // given & when
            val program = PROGRAM {
                val main by SUBROUTINE {
                    FROM(ENTRY_NODE)
                        .PUSH(OUTPUT, 123)
                        .GOTO(EXIT_NODE)
                }
            }

            // then
            val subroutine = program.subroutines.single()
            val transition = subroutine.transitions.single()

            assertThat(transition.fromNode).isEqualTo(subroutine.ENTRY_NODE)
            assertThat(transition.toNode).isEqualTo(subroutine.EXIT_NODE)

            // verify the action
            val action = transition.actions.single() as Transition.PushToStack
            assertThat(action.stack).isEqualTo(OUTPUT)
            assertThat(action.outputValue).isEqualTo(123.toUByte())
        }

        @Test
        fun `define program with an INPUT conditional and an OUTPUT push transition`() {
            // given & when
            val program = PROGRAM {
                val main by SUBROUTINE {
                    FROM(ENTRY_NODE)
                        .ON(INPUT, 123)
                        .PUSH(OUTPUT, 234)
                        .GOTO(EXIT_NODE)
                }
            }

            // then
            val subroutine = program.subroutines.single()
            val transition = subroutine.transitions.single()

            assertThat(transition.fromNode).isEqualTo(subroutine.ENTRY_NODE)
            assertThat(transition.toNode).isEqualTo(subroutine.EXIT_NODE)

            // verify the condition
            val condition = transition.conditions.single() as Transition.OnStack
            assertThat(condition.stack).isEqualTo(INPUT)
            assertThat(condition.conditionalValue).isEqualTo(123.toUByte())

            // verify the action
            val action = transition.actions.single() as Transition.PushToStack
            assertThat(action.stack).isEqualTo(OUTPUT)
            assertThat(action.outputValue).isEqualTo(234.toUByte())
        }
        @Test
        fun `define program with a custom Stack conditional`() {
            // given & when
            val program = PROGRAM {
                val myStack by STACK()
                val main by SUBROUTINE {
                    FROM(ENTRY_NODE)
                        .ON(myStack, 123)
                        .GOTO(EXIT_NODE)
                }
            }

            // then
            val subroutine = program.subroutines.single()
            val transition = subroutine.transitions.single()

            assertThat(transition.fromNode).isEqualTo(subroutine.ENTRY_NODE)
            assertThat(transition.toNode).isEqualTo(subroutine.EXIT_NODE)

            // verify the condition
            val condition = transition.conditions.single() as Transition.OnStack
            val stackFromCondition = condition.stack as CustomStack
            assertThat(stackFromCondition.name.value).isEqualTo("myStack")
            assertThat(condition.conditionalValue).isEqualTo(123.toUByte())
        }

        @Test
        fun `define program with a custom Stack push`() {
            // given & when
            val program = PROGRAM {
                val myStack by STACK()
                val main by SUBROUTINE {
                    FROM(ENTRY_NODE)
                        .PUSH(myStack, 123)
                        .GOTO(EXIT_NODE)
                }
            }

            // then
            val subroutine = program.subroutines.single()
            val transition = subroutine.transitions.single()

            assertThat(transition.fromNode).isEqualTo(subroutine.ENTRY_NODE)
            assertThat(transition.toNode).isEqualTo(subroutine.EXIT_NODE)

            // verify the action
            val action = transition.actions.single() as Transition.PushToStack
            val stackFromAction = action.stack as CustomStack
            assertThat(stackFromAction.name.value).isEqualTo("myStack")
            assertThat(action.outputValue).isEqualTo(123.toUByte())
        }

        @Test
        fun `define program with a custom Stack conditional and custom Stack push`() {
            // given & when
            val program = PROGRAM {
                val myStack by STACK()
                val main by SUBROUTINE {
                    FROM(ENTRY_NODE)
                        .ON(myStack, 123)
                        .PUSH(myStack, 234)
                        .GOTO(EXIT_NODE)
                }
            }

            // then
            val subroutine = program.subroutines.single()
            val transition = subroutine.transitions.single()

            assertThat(transition.fromNode).isEqualTo(subroutine.ENTRY_NODE)
            assertThat(transition.toNode).isEqualTo(subroutine.EXIT_NODE)

            // verify the condition
            val condition = transition.conditions.single() as Transition.OnStack
            val stackFromCondition = condition.stack as CustomStack
            assertThat(stackFromCondition.name.value).isEqualTo("myStack")
            assertThat(condition.conditionalValue).isEqualTo(123.toUByte())

            // verify the action
            val action = transition.actions.single() as Transition.PushToStack
            val stackFromAction = action.stack as CustomStack
            assertThat(stackFromAction.name.value).isEqualTo("myStack")
            assertThat(action.outputValue).isEqualTo(234.toUByte())
        }

        @Test
        fun `define a program with a conditional Char value`() {
            // given & when
            val program = PROGRAM {
                val main by SUBROUTINE {
                    FROM(ENTRY_NODE).ON(INPUT, 'a').GOTO(EXIT_NODE)
                }
            }

            // then
            val subroutine = program.subroutines.single()
            val transition = subroutine.transitions.single()

            // verify the condition
            val condition = transition.conditions.single() as Transition.OnStack
            assertThat(condition.conditionalValue).isEqualTo(97.toUByte())
        }

        @Test
        fun `define a program with a push Char output value`() {
            // given & when
            val program = PROGRAM {
                val main by SUBROUTINE {
                    FROM(ENTRY_NODE).PUSH(OUTPUT, 'a').GOTO(EXIT_NODE)
                }
            }

            // then
            val subroutine = program.subroutines.single()
            val transition = subroutine.transitions.single()

            // verify the action
            val action = transition.actions.single() as Transition.PushToStack
            assertThat(action.stack).isEqualTo(OUTPUT)
            assertThat(action.outputValue).isEqualTo(97.toUByte())
        }
    }

    @Nested
    inner class `test more complex program definitions`{
        @Test
        fun `test program definition with multiple outgoing transitions from single node`(){
            // given & when
            val program = PROGRAM {
                val main by SUBROUTINE {
                    FROM(ENTRY_NODE)
                        .ON(INPUT, 123)
                        .PUSH(OUTPUT, 'a')
                        .GOTO(EXIT_NODE)

                    FROM(ENTRY_NODE)
                        .ON(INPUT, 'b')
                        .PUSH(OUTPUT, 234)
                        .GOTO(EXIT_NODE)

                }
            }

            // then
            val subroutine = program.subroutines.single()
            assertThat(subroutine.transitions).hasSize(2)

            // verify the first transition
            val transition1 = subroutine.transitions.first()
            assertThat(transition1.fromNode).isEqualTo(subroutine.ENTRY_NODE)
            assertThat(transition1.toNode).isEqualTo(subroutine.EXIT_NODE)
            assertThat(transition1.conditions.single()).isEqualTo(
                Transition.OnStack(
                    stack = INPUT,
                    conditionalValue = 123.toUByte()
                )
            )
            assertThat(transition1.actions.single()).isEqualTo(
                Transition.PushToStack(
                    stack = OUTPUT,
                    outputValue = 97.toUByte()
                )
            )

            // verify the second transition
            val transition2 = subroutine.transitions.last()
            assertThat(transition2.fromNode).isEqualTo(subroutine.ENTRY_NODE)
            assertThat(transition2.toNode).isEqualTo(subroutine.EXIT_NODE)
            assertThat(transition2.conditions.single()).isEqualTo(
                Transition.OnStack(
                    stack = INPUT,
                    conditionalValue = 98.toUByte()
                )
            )
            assertThat(transition2.actions.single()).isEqualTo(
                Transition.PushToStack(
                    stack = OUTPUT,
                    outputValue = 234.toUByte()
                )
            )
        }

        // TODO
    }

    // TODO check that there must be a main subroutine
    // TODO check that the transitions from the same node must have different conditionals

}
