package com.mycompany.minilua.compiler.intermediate;

public class TACInstruction {

    private final String operation;
    private final String arg1;
    private final String arg2;
    private final String result;

    public TACInstruction(
            String operation,
            String arg1,
            String arg2,
            String result) {

        this.operation = operation;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.result = result;
    }

    public String getOperation() {
        return operation;
    }

    public String getArg1() {
        return arg1;
    }

    public String getArg2() {
        return arg2;
    }

    public String getResult() {
        return result;
    }
}
