package com.mycompany.minilua.compiler.lexer;

public enum TokenType {
    LOCAL,
    IF,
    THEN,
    ELSE,
    END,
    WHILE,
    DO,
    TRUE,
    FALSE,
    IDENTIFIER,
    INTEGER,
    PLUS,
    MINUS,
    MULTIPLY,
    DIVIDE,
    ASSIGN,
    EQUAL_EQUAL,
    NOT_EQUAL,
    LESS,
    GREATER,
    LESS_EQUAL,
    GREATER_EQUAL,
    LEFT_PAREN,
    RIGHT_PAREN,
    EOF
}
