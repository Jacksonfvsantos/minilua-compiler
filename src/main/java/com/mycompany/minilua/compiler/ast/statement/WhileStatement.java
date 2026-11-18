package com.mycompany.minilua.compiler.ast.statement;

import com.mycompany.minilua.compiler.ast.expression.Expression;

public class WhileStatement extends Statement {

    private final Expression condition;
    private final BlockStatement body;

    public WhileStatement(Expression condition, BlockStatement body) {
        this.condition = condition;
        this.body = body;
    }

    public Expression getCondition() {
        return condition;
    }

    public BlockStatement getBody() {
        return body;
    }
}
