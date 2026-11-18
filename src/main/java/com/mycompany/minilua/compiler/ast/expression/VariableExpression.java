package com.mycompany.minilua.compiler.ast.expression;

public class VariableExpression extends Expression {

    private final String name;

    public VariableExpression(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
