package com.mycompany.minilua.compiler.parser;

import com.mycompany.minilua.compiler.ast.expression.*;
import com.mycompany.minilua.compiler.ast.statement.*;
import com.mycompany.minilua.compiler.lexer.Token;
import com.mycompany.minilua.compiler.lexer.TokenType;

import java.util.ArrayList;
import java.util.List;

public class Parser {

    private final List<Token> tokens;
    private int current = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public List<Statement> parse() {

        List<Statement> statements = new ArrayList<>();

        while (!isAtEnd()) {
            statements.add(statement());
        }

        return statements;
    }



    private Statement statement() {

        if (match(TokenType.LOCAL)) {
            return variableDeclaration();
        }

        if (match(TokenType.IF)) {
            return ifStatement();
        }

        if (match(TokenType.WHILE)) {
            return whileStatement();
        }

        if (check(TokenType.IDENTIFIER)) {
            if (checkNext(TokenType.ASSIGN)) {
                return assignment();
            }

            return expressionStatement();
        }

        throw error(peek(), "Instrução inválida.");
    }


    private Statement whileStatement() {

        Expression condition = expression();

        consume(
                TokenType.DO,
                "Esperado 'do' após a condição do while."
        );

        BlockStatement body = block();

        consume(
                TokenType.END,
                "Esperado 'end' para finalizar o while."
        );

        return new WhileStatement(condition, body);
    }


    private boolean checkNext(TokenType type) {

        if (current + 1 >= tokens.size()) {
            return false;
        }

        return tokens.get(current + 1).getType() == type;
    }


    private Statement expressionStatement() {

        Expression expression = expression();

        return new ExpressionStatement(expression);
    }


    private Statement ifStatement() {

        Expression condition = expression();

        consume(
                TokenType.THEN,
                "Esperado 'then' após a condição."
        );

        BlockStatement thenBranch = block();

        BlockStatement elseBranch = null;

        if (match(TokenType.ELSE)) {
            elseBranch = block();
        }

        consume(
                TokenType.END,
                "Esperado 'end' para finalizar o if."
        );

        return new IfStatement(
                condition,
                thenBranch,
                elseBranch
        );
    }


    private BlockStatement block() {

        List<Statement> statements = new ArrayList<>();

        while (!check(TokenType.ELSE)
                && !check(TokenType.END)
                && !isAtEnd()) {

            statements.add(statement());
        }

        return new BlockStatement(statements);
    }


    private Statement variableDeclaration() {

        Token name = consume(
                TokenType.IDENTIFIER,
                "Esperado um nome para a variável."
        );

        consume(
                TokenType.ASSIGN,
                "Esperado '=' após o nome da variável."
        );

        Expression initializer = expression();

        return new VariableDeclarationStatement(
                name.getLexeme(),
                initializer
        );
    }

    private Statement assignment() {

        Token name = consume(
                TokenType.IDENTIFIER,
                "Esperado um nome de variável."
        );

        consume(
                TokenType.ASSIGN,
                "Esperado '=' após o nome da variável."
        );

        Expression value = expression();

        return new AssignmentStatement(
                name.getLexeme(),
                value
        );
    }

    private Expression expression() {
        return comparison();
    }

    private Expression comparison() {

        Expression expression = addition();

        while (match(
                TokenType.EQUAL_EQUAL,
                TokenType.NOT_EQUAL,
                TokenType.LESS,
                TokenType.GREATER,
                TokenType.LESS_EQUAL,
                TokenType.GREATER_EQUAL
        )) {

            TokenType operator = previous().getType();
            Expression right = addition();

            expression = new BinaryExpression(
                    expression,
                    operator,
                    right
            );
        }

        return expression;
    }

    private Expression addition() {

        Expression expression = multiplication();

        while (match(TokenType.PLUS, TokenType.MINUS)) {

            TokenType operator = previous().getType();
            Expression right = multiplication();

            expression = new BinaryExpression(
                    expression,
                    operator,
                    right
            );
        }

        return expression;
    }

    private Expression multiplication() {

        Expression expression = unary();

        while (match(TokenType.MULTIPLY, TokenType.DIVIDE)) {

            TokenType operator = previous().getType();
            Expression right = unary();

            expression = new BinaryExpression(
                    expression,
                    operator,
                    right
            );
        }

        return expression;
    }

    private Expression unary() {

        if (match(TokenType.MINUS)) {

            TokenType operator = previous().getType();
            Expression operand = unary();

            return new UnaryExpression(operator, operand);
        }

        return primary();
    }

    private Expression primary() {

        if (match(TokenType.INTEGER)) {
            return new LiteralExpression(
                    Integer.parseInt(previous().getLexeme())
            );
        }

        if (match(TokenType.TRUE)) {
            return new LiteralExpression(true);
        }

        if (match(TokenType.FALSE)) {
            return new LiteralExpression(false);
        }


        if (match(TokenType.IDENTIFIER)) {

            Token name = previous();

            if (match(TokenType.LEFT_PAREN)) {

                List<Expression> arguments = new ArrayList<>();

                if (!check(TokenType.RIGHT_PAREN)) {
                    arguments.add(expression());
                }

                consume(
                        TokenType.RIGHT_PAREN,
                        "Esperado ')' após os argumentos da função."
                );

                return new CallExpression(
                        name.getLexeme(),
                        arguments
                );
            }

            return new VariableExpression(name.getLexeme());
        }

        if (match(TokenType.LEFT_PAREN)) {

            Expression expression = expression();

            consume(
                    TokenType.RIGHT_PAREN,
                    "Esperado ')' após a expressão."
            );

            return expression;
        }

        throw error(peek(), "Esperada uma expressão.");
    }

    private boolean match(TokenType... types) {

        for (TokenType type : types) {

            if (check(type)) {
                advance();
                return true;
            }
        }

        return false;
    }

    private Token consume(TokenType type, String message) {

        if (check(type)) {
            return advance();
        }

        throw error(peek(), message);
    }

    private boolean check(TokenType type) {
        return peek().getType() == type;
    }

    private Token advance() {

        if (!isAtEnd()) {
            current++;
        }

        return previous();
    }

    private boolean isAtEnd() {
        return peek().getType() == TokenType.EOF;
    }

    private Token peek() {
        return tokens.get(current);
    }

    private Token previous() {
        return tokens.get(current - 1);
    }

    private RuntimeException error(Token token, String message) {

        return new RuntimeException(
                "Erro sintático na linha "
                + token.getLine()
                + ", coluna "
                + token.getColumn()
                + ": "
                + message
        );
    }
}
