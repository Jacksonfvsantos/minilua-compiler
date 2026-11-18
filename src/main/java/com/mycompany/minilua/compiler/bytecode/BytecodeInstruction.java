
package com.mycompany.minilua.compiler.bytecode;

public class BytecodeInstruction {

    private final BytecodeOpcode opcode;

    private final int destination;
    private final int operand1;
    private final int operand2;

    private final Object constant;
    private final int jumpTarget;

    public BytecodeInstruction(
            BytecodeOpcode opcode,
            int destination,
            int operand1,
            int operand2,
            Object constant,
            int jumpTarget) {

        this.opcode = opcode;
        this.destination = destination;
        this.operand1 = operand1;
        this.operand2 = operand2;
        this.constant = constant;
        this.jumpTarget = jumpTarget;
    }

    public BytecodeOpcode getOpcode() {
        return opcode;
    }

    public int getDestination() {
        return destination;
    }

    public int getOperand1() {
        return operand1;
    }

    public int getOperand2() {
        return operand2;
    }

    public Object getConstant() {
        return constant;
    }

    public int getJumpTarget() {
        return jumpTarget;
    }
}
