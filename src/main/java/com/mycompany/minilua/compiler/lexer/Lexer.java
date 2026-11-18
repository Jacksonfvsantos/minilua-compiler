package com.mycompany.minilua.compiler.lexer;

import java.util.ArrayList;
import java.util.List;

public class Lexer {

    private final String source;
    private final List<Token> tokens = new ArrayList<>();

    private int current = 0;
    private int line = 1;
    private int column = 1;

    public Lexer(String source) {
        this.source = source;
    }

    public List<Token> scanTokens() {

        while (!isAtEnd()) {

            char c = peek();

            if (Character.isWhitespace(c)) {
                advance();
            }
            else if (Character.isDigit(c)) {
                scanNumber();
            }
            else if (Character.isLetter(c) || c == '_') {
                scanIdentifier();
            }
            else {
                scanSymbol();
            }
        }

        tokens.add(new Token(
                TokenType.EOF, "", line, column));

        return tokens;
    }

    private boolean isAtEnd() {
        return current >= source.length();
    }

    private char peek() {
        return source.charAt(current);
    }

    private char advance() {

        char c = source.charAt(current++);

        if (c == '\n') {
            line++;
            column = 1;
        } else {
            column++;
        }

        return c;
    }


    private boolean match(char expected) {

        if (isAtEnd()) {
            return false;
        }

        if (source.charAt(current) != expected) {
            return false;
        }

        advance();
        return true;
    }


    private void scanNumber() {

        int start = current;
        int startColumn = column;

        while (!isAtEnd()
                && Character.isDigit(peek())) {
            advance();
        }

        String value = source.substring(start, current);

        tokens.add(new Token(
                TokenType.INTEGER,
                value,
                line,
                startColumn
        ));
    }

    private void scanIdentifier() {

        int start = current;
        int startColumn = column;

        while (!isAtEnd()
                && (Character.isLetterOrDigit(peek())
                    || peek() == '_')) {
            advance();
        }

        String value = source.substring(start, current);

        TokenType type;

        switch (value) {
            case "local":
                type = TokenType.LOCAL;
                break;
            case "if":
                type = TokenType.IF;
                break;
            case "then":
                type = TokenType.THEN;
                break;
            case "else":
                type = TokenType.ELSE;
                break;
            case "end":
                type = TokenType.END;
                break;
            case "while":
                type = TokenType.WHILE;
                break;
            case "do":
                type = TokenType.DO;
                break;
            case "true":
                type = TokenType.TRUE;
                break;
            case "false":
                type = TokenType.FALSE;
                break;
            default:
                type = TokenType.IDENTIFIER;
        }

        tokens.add(new Token(
                type, value, line, startColumn));
    }


    private void scanSymbol() {

        int start = current;
        int startLine = line;
        int startColumn = column;

        char c = advance();
        TokenType type;

        switch (c) {

            case '=':
                type = match('=')
                        ? TokenType.EQUAL_EQUAL
                        : TokenType.ASSIGN;
                break;

            case '~':
                if (match('=')) {
                    type = TokenType.NOT_EQUAL;
                } else {
                    throw new RuntimeException(
                        "Operador inválido '~' na linha "
                        + startLine + ", coluna "
                        + startColumn);
                }
                break;

            case '-':
                if (match('-')) {

                    while (!isAtEnd() && peek() != '\n') {
                        advance();
                    }

                    return;
                }

                type = TokenType.MINUS;
                break;

            case '+':
                type = TokenType.PLUS;
                break;

            case '*':
                type = TokenType.MULTIPLY;
                break;

            case '/':
                type = TokenType.DIVIDE;
                break;

            case '<':
                type = match('=')
                        ? TokenType.LESS_EQUAL
                        : TokenType.LESS;
                break;

            case '>':
                type = match('=')
                        ? TokenType.GREATER_EQUAL
                        : TokenType.GREATER;
                break;

            case '(':
                type = TokenType.LEFT_PAREN;
                break;

            case ')':
                type = TokenType.RIGHT_PAREN;
                break;

            default:
                throw new RuntimeException(
                    "Caractere inválido: " + c
                    + " na linha " + startLine
                    + ", coluna " + startColumn);
        }

        String lexeme = source.substring(start, current);

        tokens.add(new Token(
                type,
                lexeme,
                startLine,
                startColumn
        ));
    }
}
