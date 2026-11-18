package com.mycompany.minilua.compiler.ast.statement;

import com.mycompany.minilua.compiler.ast.expression.Expression;

public class VariableDeclarationStatement extends Statement {

    private final String name;
    private final Expression initializer;

    public VariableDeclarationStatement(String name, Expression initializer) {
        this.name = name;
        this.initializer = initializer;
    }

    public String getName() {
        return name;
    }

    public Expression getInitializer() {
        return initializer;
    }
}
