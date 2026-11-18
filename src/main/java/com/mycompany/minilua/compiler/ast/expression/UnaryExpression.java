package com.mycompany.minilua.compiler.ast.expression;

import com.mycompany.minilua.compiler.lexer.TokenType;

public class UnaryExpression extends Expression {

    private final TokenType operator;
    private final Expression operand;

    public UnaryExpression(TokenType operator, Expression operand) {
        this.operator = operator;
        this.operand = operand;
    }

    public TokenType getOperator() {
        return operator;
    }

    public Expression getOperand() {
        return operand;
    }
}
