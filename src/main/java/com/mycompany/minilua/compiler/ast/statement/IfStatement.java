package com.mycompany.minilua.compiler.ast.statement;

import com.mycompany.minilua.compiler.ast.expression.Expression;

public class IfStatement extends Statement {

    private final Expression condition;
    private final BlockStatement thenBranch;
    private final BlockStatement elseBranch;

    public IfStatement(
            Expression condition,
            BlockStatement thenBranch,
            BlockStatement elseBranch) {

        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }

    public Expression getCondition() {
        return condition;
    }

    public BlockStatement getThenBranch() {
        return thenBranch;
    }

    public BlockStatement getElseBranch() {
        return elseBranch;
    }
}
