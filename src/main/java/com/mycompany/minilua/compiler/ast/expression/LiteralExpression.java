package com.mycompany.minilua.compiler.ast.expression;

public class LiteralExpression extends Expression {

    private final Object value;

    public LiteralExpression(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }
}
