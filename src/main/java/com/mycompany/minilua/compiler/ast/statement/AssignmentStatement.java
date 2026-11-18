package com.mycompany.minilua.compiler.ast.statement;

import com.mycompany.minilua.compiler.ast.expression.Expression;

public class AssignmentStatement extends Statement {

    private final String name;
    private final Expression value;

    public AssignmentStatement(String name, Expression value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Expression getValue() {
        return value;
    }
}
