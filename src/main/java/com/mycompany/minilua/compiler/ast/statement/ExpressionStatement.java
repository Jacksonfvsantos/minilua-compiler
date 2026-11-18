package com.mycompany.minilua.compiler.ast.statement;

import com.mycompany.minilua.compiler.ast.expression.Expression;

public class ExpressionStatement extends Statement {

    private final Expression expression;

    public ExpressionStatement(Expression expression) {
        this.expression = expression;
    }

    public Expression getExpression() {
        return expression;
    }
}
