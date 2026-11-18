
package com.mycompany.minilua.compiler.bytecode;

import java.util.List;
import java.util.Map;

public class BytecodePrinter {

    public void print(BytecodeProgram program) {

        List<BytecodeInstruction> instructions =
                program.getInstructions();

        Map<String, Integer> slots = program.getSlots();

        String[] names = new String[slots.size()];

        for (Map.Entry<String, Integer> entry : slots.entrySet()) {
            names[entry.getValue()] = entry.getKey();
        }

        for (int i = 0; i < instructions.size(); i++) {

            BytecodeInstruction instruction = instructions.get(i);

            System.out.print(i + ": ");

            switch (instruction.getOpcode()) {

                case LOAD_CONST:

                    System.out.println(
                            "LOAD_CONST "
                            + names[instruction.getDestination()]
                            + ", "
                            + instruction.getConstant()
                    );

                    break;

                case MOVE:

                    System.out.println(
                            "MOVE "
                            + names[instruction.getDestination()]
                            + ", "
                            + names[instruction.getOperand1()]
                    );

                    break;

                case NEGATE:

                    System.out.println(
                            "NEGATE "
                            + names[instruction.getDestination()]
                            + ", "
                            + names[instruction.getOperand1()]
                    );

                    break;

                case ADD:
                case SUBTRACT:
                case MULTIPLY:
                case DIVIDE:
                case EQUAL:
                case NOT_EQUAL:
                case LESS:
                case GREATER:
                case LESS_EQUAL:
                case GREATER_EQUAL:

                    System.out.println(
                            instruction.getOpcode()
                            + " "
                            + names[instruction.getDestination()]
                            + ", "
                            + names[instruction.getOperand1()]
                            + ", "
                            + names[instruction.getOperand2()]
                    );

                    break;

                case READ:

                    System.out.println(
                            "READ "
                            + names[instruction.getDestination()]
                    );

                    break;

                case PRINT:

                    System.out.println(
                            "PRINT "
                            + names[instruction.getOperand1()]
                    );

                    break;

                case JUMP:

                    System.out.println(
                            "JUMP "
                            + instruction.getJumpTarget()
                    );

                    break;

                case JUMP_IF_FALSE:

                    System.out.println(
                            "JUMP_IF_FALSE "
                            + names[instruction.getOperand1()]
                            + ", "
                            + instruction.getJumpTarget()
                    );

                    break;

                case HALT:

                    System.out.println("HALT");

                    break;
            }
        }
    }
}
