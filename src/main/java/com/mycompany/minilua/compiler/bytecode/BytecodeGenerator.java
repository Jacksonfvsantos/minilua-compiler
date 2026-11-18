
package com.mycompany.minilua.compiler.bytecode;

import com.mycompany.minilua.compiler.intermediate.TACInstruction;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BytecodeGenerator {

    private final List<BytecodeInstruction> instructions =
            new ArrayList<>();

    private final Map<String, Integer> slots =
            new LinkedHashMap<>();

    private final Map<String, Integer> labels =
            new LinkedHashMap<>();

    private int constantCounter;

    public BytecodeProgram generate(List<TACInstruction> tac) {

        instructions.clear();
        slots.clear();
        labels.clear();
        constantCounter = 0;

        resolveLabels(tac);

        for (TACInstruction instruction : tac) {
            generateInstruction(instruction);
        }

        emit(BytecodeOpcode.HALT, -1, -1, -1, null, -1);

        return new BytecodeProgram(
                new ArrayList<>(instructions),
                new LinkedHashMap<>(slots)
        );
    }

    private void resolveLabels(List<TACInstruction> tac) {

        int position = 0;

        for (TACInstruction instruction : tac) {

            String operation = instruction.getOperation();

            if (operation.equals("LABEL")) {
                labels.put(instruction.getResult(), position);
            } else {
                position += 1 + countExtraInstructions(instruction);
            }
        }
    }

    private int countExtraInstructions(TACInstruction tac) {

        String operation = tac.getOperation();

        switch (operation) {

            case "NEG":
            case "PRINT":
            case "IF_FALSE":
                return isConstant(tac.getArg1()) ? 1 : 0;

            case "PLUS":
            case "MINUS":
            case "MULTIPLY":
            case "DIVIDE":
            case "EQUAL_EQUAL":
            case "NOT_EQUAL":
            case "LESS":
            case "GREATER":
            case "LESS_EQUAL":
            case "GREATER_EQUAL":

                int extra = 0;

                if (isConstant(tac.getArg1())) {
                    extra++;
                }

                if (isConstant(tac.getArg2())) {
                    extra++;
                }

                return extra;

            default:
                return 0;
        }
    }

    private void generateInstruction(TACInstruction tac) {

        String operation = tac.getOperation();

        String arg1 = tac.getArg1();
        String arg2 = tac.getArg2();
        String result = tac.getResult();

        switch (operation) {

            case "ASSIGN":

                int destination = getSlot(result);

                if (isConstant(arg1)) {

                    emit(
                            BytecodeOpcode.LOAD_CONST,
                            destination,
                            -1,
                            -1,
                            parseConstant(arg1),
                            -1
                    );

                } else {

                    emit(
                            BytecodeOpcode.MOVE,
                            destination,
                            getSlot(arg1),
                            -1,
                            null,
                            -1
                    );
                }

                break;

            case "NEG":

                int negDestination = getSlot(result);
                int negOperand = getOperandSlot(arg1);

                emit(
                        BytecodeOpcode.NEGATE,
                        negDestination,
                        negOperand,
                        -1,
                        null,
                        -1
                );

                break;

            case "PLUS":
            case "MINUS":
            case "MULTIPLY":
            case "DIVIDE":
            case "EQUAL_EQUAL":
            case "NOT_EQUAL":
            case "LESS":
            case "GREATER":
            case "LESS_EQUAL":
            case "GREATER_EQUAL":

                int operationDestination = getSlot(result);
                int operand1 = getOperandSlot(arg1);
                int operand2 = getOperandSlot(arg2);

                emit(
                        getOpcode(operation),
                        operationDestination,
                        operand1,
                        operand2,
                        null,
                        -1
                );

                break;

            case "READ":

                emit(
                        BytecodeOpcode.READ,
                        getSlot(result),
                        -1,
                        -1,
                        null,
                        -1
                );

                break;

            case "PRINT":

                emit(
                        BytecodeOpcode.PRINT,
                        -1,
                        getOperandSlot(arg1),
                        -1,
                        null,
                        -1
                );

                break;

            case "IF_FALSE":

                emit(
                        BytecodeOpcode.JUMP_IF_FALSE,
                        -1,
                        getOperandSlot(arg1),
                        -1,
                        null,
                        getLabel(result)
                );

                break;

            case "GOTO":

                emit(
                        BytecodeOpcode.JUMP,
                        -1,
                        -1,
                        -1,
                        null,
                        getLabel(result)
                );

                break;

            case "LABEL":
                break;

            default:

                throw new IllegalArgumentException(
                        "Operação TAC desconhecida: " + operation
                );
        }
    }

    private int getOperandSlot(String operand) {

        if (!isConstant(operand)) {
            return getSlot(operand);
        }

        int slot = getSlot("#const" + (++constantCounter));

        emit(
                BytecodeOpcode.LOAD_CONST,
                slot,
                -1,
                -1,
                parseConstant(operand),
                -1
        );

        return slot;
    }

    private BytecodeOpcode getOpcode(String operation) {

        switch (operation) {

            case "PLUS":
                return BytecodeOpcode.ADD;

            case "MINUS":
                return BytecodeOpcode.SUBTRACT;

            case "MULTIPLY":
                return BytecodeOpcode.MULTIPLY;

            case "DIVIDE":
                return BytecodeOpcode.DIVIDE;

            case "EQUAL_EQUAL":
                return BytecodeOpcode.EQUAL;

            case "NOT_EQUAL":
                return BytecodeOpcode.NOT_EQUAL;

            case "LESS":
                return BytecodeOpcode.LESS;

            case "GREATER":
                return BytecodeOpcode.GREATER;

            case "LESS_EQUAL":
                return BytecodeOpcode.LESS_EQUAL;

            case "GREATER_EQUAL":
                return BytecodeOpcode.GREATER_EQUAL;

            default:
                throw new IllegalArgumentException(
                        "Operador desconhecido: " + operation
                );
        }
    }

    private boolean isConstant(String value) {

        if (value == null) {
            return false;
        }

        return value.matches("-?\\d+")
                || value.equals("true")
                || value.equals("false");
    }

    private Object parseConstant(String value) {

        if (value.equals("true")) {
            return true;
        }

        if (value.equals("false")) {
            return false;
        }

        return Integer.parseInt(value);
    }

    private int getSlot(String name) {

        if (name == null) {
            throw new IllegalArgumentException(
                    "Identificador de slot inválido."
            );
        }

        return slots.computeIfAbsent(
                name,
                key -> slots.size()
        );
    }

    private int getLabel(String name) {

        Integer position = labels.get(name);

        if (position == null) {
            throw new IllegalArgumentException(
                    "Rótulo não encontrado: " + name
            );
        }

        return position;
    }

    private void emit(
            BytecodeOpcode opcode,
            int destination,
            int operand1,
            int operand2,
            Object constant,
            int jumpTarget) {

        instructions.add(
                new BytecodeInstruction(
                        opcode,
                        destination,
                        operand1,
                        operand2,
                        constant,
                        jumpTarget
                )
        );
    }
}
