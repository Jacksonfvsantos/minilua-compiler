
package com.mycompany.minilua.compiler.bytecode;

import java.util.List;
import java.util.Scanner;
import java.util.Objects;
import java.io.PrintStream;
import java.io.ByteArrayOutputStream;

public class VirtualMachine {

    private final List<BytecodeInstruction> instructions;
    private final Object[] memory;
    private final Scanner scanner;
    private final PrintStream output;

    private int programCounter;
    private boolean halted;

    public VirtualMachine(BytecodeProgram program) {
        this(program, new Scanner(System.in), System.out);
}

    public VirtualMachine(
            BytecodeProgram program,
            Scanner scanner,
            PrintStream output) {

        this.instructions = program.getInstructions();
        this.memory = new Object[program.getSlots().size()];

        this.scanner = Objects.requireNonNull(scanner);
        this.output = Objects.requireNonNull(output);

        this.programCounter = 0;
        this.halted = false;
    }

    public void execute() {

        programCounter = 0;
        halted = false;

        while (!halted) {

            if (programCounter < 0
                    || programCounter >= instructions.size()) {

                throw new IllegalStateException(
                        "Contador de programa fora dos limites: "
                                + programCounter
                );
            }

            BytecodeInstruction instruction =
                    instructions.get(programCounter);

            executeInstruction(instruction);
        }
    }

    // ==========================================
    // VALIDAÇÃO E ACESSO À MEMÓRIA
    // ==========================================

    private void validateSlot(int slot) {

        if (slot < 0 || slot >= memory.length) {

            throw new IllegalStateException(
                    "Slot de memória inválido: " + slot
            );
        }
    }

    private Object readSlot(int slot) {

        validateSlot(slot);

        return memory[slot];
    }

    private void writeSlot(int slot, Object value) {

        validateSlot(slot);

        memory[slot] = value;
    }

    // ==========================================
    // LEITURA DE TIPOS
    // ==========================================

    private int getInteger(int slot) {

        Object value = readSlot(slot);

        if (!(value instanceof Integer)) {

            throw new IllegalStateException(
                    "O slot " + slot
                            + " não contém um valor inteiro."
            );
        }

        return (Integer) value;
    }

    private boolean getBoolean(int slot) {

        Object value = readSlot(slot);

        if (!(value instanceof Boolean)) {

            throw new IllegalStateException(
                    "O slot " + slot
                            + " não contém um valor booleano."
            );
        }

        return (Boolean) value;
    }

    // ==========================================
    // EXECUÇÃO DAS INSTRUÇÕES
    // ==========================================

    private void executeInstruction(
            BytecodeInstruction instruction) {

        switch (instruction.getOpcode()) {

            case LOAD_CONST:

                writeSlot(
                        instruction.getDestination(),
                        instruction.getConstant()
                );

                programCounter++;
                break;

            case MOVE:

                writeSlot(
                        instruction.getDestination(),
                        readSlot(instruction.getOperand1())
                );

                programCounter++;
                break;

            case PRINT:
                output.println(readSlot(instruction.getOperand1()));
                programCounter++;
                break;

            case HALT:

                halted = true;
                break;

            // ==================================
            // OPERAÇÕES ARITMÉTICAS
            // ==================================

            case ADD:

                writeSlot(
                        instruction.getDestination(),
                        getInteger(instruction.getOperand1())
                        + getInteger(instruction.getOperand2())
                );

                programCounter++;
                break;

            case SUBTRACT:

                writeSlot(
                        instruction.getDestination(),
                        getInteger(instruction.getOperand1())
                        - getInteger(instruction.getOperand2())
                );

                programCounter++;
                break;

            case MULTIPLY:

                writeSlot(
                        instruction.getDestination(),
                        getInteger(instruction.getOperand1())
                        * getInteger(instruction.getOperand2())
                );

                programCounter++;
                break;

            case DIVIDE:

                int divisor =
                        getInteger(instruction.getOperand2());

                if (divisor == 0) {

                    throw new ArithmeticException(
                            "Divisão por zero."
                    );
                }

                writeSlot(
                        instruction.getDestination(),
                        getInteger(instruction.getOperand1()) / divisor
                );

                programCounter++;
                break;

            case NEGATE:

                writeSlot(
                        instruction.getDestination(),
                        -getInteger(instruction.getOperand1())
                );

                programCounter++;
                break;

            // ==================================
            // OPERAÇÕES DE COMPARAÇÃO
            // ==================================

            case EQUAL:

                writeSlot(
                        instruction.getDestination(),
                        Objects.equals(
                                readSlot(instruction.getOperand1()),
                                readSlot(instruction.getOperand2())
                        )
                );

                programCounter++;
                break;

            case NOT_EQUAL:

                writeSlot(
                        instruction.getDestination(),
                        !Objects.equals(
                                readSlot(instruction.getOperand1()),
                                readSlot(instruction.getOperand2())
                        )
                );

                programCounter++;
                break;

            case LESS:

                writeSlot(
                        instruction.getDestination(),
                        getInteger(instruction.getOperand1())
                        < getInteger(instruction.getOperand2())
                );

                programCounter++;
                break;

            case GREATER:

                writeSlot(
                        instruction.getDestination(),
                        getInteger(instruction.getOperand1())
                        > getInteger(instruction.getOperand2())
                );

                programCounter++;
                break;

            case LESS_EQUAL:

                writeSlot(
                        instruction.getDestination(),
                        getInteger(instruction.getOperand1())
                        <= getInteger(instruction.getOperand2())
                );

                programCounter++;
                break;

            case GREATER_EQUAL:

                writeSlot(
                        instruction.getDestination(),
                        getInteger(instruction.getOperand1())
                        >= getInteger(instruction.getOperand2())
                );

                programCounter++;
                break;

            // ==================================
            // CONTROLE DE FLUXO
            // ==================================

            case JUMP:

                programCounter = instruction.getJumpTarget();
                break;

            case JUMP_IF_FALSE:

                if (!getBoolean(instruction.getOperand1())) {

                    programCounter = instruction.getJumpTarget();

                } else {

                    programCounter++;
                }

                break;

            // ==================================
            // ENTRADA DE DADOS
            // ==================================

            case READ:

                output.print("Digite um valor inteiro: ");
                output.flush();

                if (!scanner.hasNext()) {
                    throw new IllegalArgumentException(
                            "Nenhuma entrada foi fornecida."
                    );
                }

                if (!scanner.hasNextInt()) {
                    scanner.next();

                    throw new IllegalArgumentException(
                            "Entrada inválida. Era esperado um número inteiro."
                    );
                }

                writeSlot(
                        instruction.getDestination(),
                        scanner.nextInt()
                );

                programCounter++;
                break;

            // ==================================
            // INSTRUÇÃO NÃO IMPLEMENTADA
            // ==================================

            default:

                throw new UnsupportedOperationException(
                        "Opcode ainda não implementado: "
                                + instruction.getOpcode()
                );
        }
    }
}
