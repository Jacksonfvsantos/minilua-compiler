
package com.mycompany.minilua.compiler.lexer;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LexerTest {

    @Test
    void shouldRecognizeReservedWords() {

        Lexer lexer = new Lexer(
                "local if then else end while do true false"
        );

        List<Token> tokens = lexer.scanTokens();

        TokenType[] expectedTypes = {
            TokenType.LOCAL,
            TokenType.IF,
            TokenType.THEN,
            TokenType.ELSE,
            TokenType.END,
            TokenType.WHILE,
            TokenType.DO,
            TokenType.TRUE,
            TokenType.FALSE,
            TokenType.EOF
        };

        assertEquals(expectedTypes.length, tokens.size());

        for (int i = 0; i < expectedTypes.length; i++) {
            assertEquals(expectedTypes[i], tokens.get(i).getType());
        }
    }

    @Test
    void shouldRecognizeIdentifiers() {

        Lexer lexer = new Lexer(
                "idade nome_usuario _contador valor123"
        );

        List<Token> tokens = lexer.scanTokens();

        assertEquals(TokenType.IDENTIFIER, tokens.get(0).getType());
        assertEquals("idade", tokens.get(0).getLexeme());

        assertEquals(TokenType.IDENTIFIER, tokens.get(1).getType());
        assertEquals("nome_usuario", tokens.get(1).getLexeme());

        assertEquals(TokenType.IDENTIFIER, tokens.get(2).getType());
        assertEquals("_contador", tokens.get(2).getLexeme());

        assertEquals(TokenType.IDENTIFIER, tokens.get(3).getType());
        assertEquals("valor123", tokens.get(3).getLexeme());

        assertEquals(TokenType.EOF, tokens.get(4).getType());
    }

    @Test
    void shouldRecognizeIntegerNumbers() {

        Lexer lexer = new Lexer("0 10 250 9999");

        List<Token> tokens = lexer.scanTokens();

        String[] expectedValues = {"0", "10", "250", "9999"};

        for (int i = 0; i < expectedValues.length; i++) {
            assertEquals(TokenType.INTEGER, tokens.get(i).getType());
            assertEquals(expectedValues[i], tokens.get(i).getLexeme());
        }

        assertEquals(TokenType.EOF, tokens.get(4).getType());
    }

    @Test
    void shouldRecognizeArithmeticOperators() {

        Lexer lexer = new Lexer("+ - * /");

        List<Token> tokens = lexer.scanTokens();

        TokenType[] expectedTypes = {
            TokenType.PLUS,
            TokenType.MINUS,
            TokenType.MULTIPLY,
            TokenType.DIVIDE,
            TokenType.EOF
        };

        for (int i = 0; i < expectedTypes.length; i++) {
            assertEquals(expectedTypes[i], tokens.get(i).getType());
        }
    }

    @Test
    void shouldRecognizeComparisonAndAssignmentOperators() {

        Lexer lexer = new Lexer("= == ~= < > <= >=");

        List<Token> tokens = lexer.scanTokens();

        TokenType[] expectedTypes = {
            TokenType.ASSIGN,
            TokenType.EQUAL_EQUAL,
            TokenType.NOT_EQUAL,
            TokenType.LESS,
            TokenType.GREATER,
            TokenType.LESS_EQUAL,
            TokenType.GREATER_EQUAL,
            TokenType.EOF
        };

        for (int i = 0; i < expectedTypes.length; i++) {
            assertEquals(expectedTypes[i], tokens.get(i).getType());
        }
    }
    
    @Test
    void shouldRecognizeParentheses() {

        Lexer lexer = new Lexer("( )");

        List<Token> tokens = lexer.scanTokens();

        assertEquals(TokenType.LEFT_PAREN, tokens.get(0).getType());
        assertEquals("(", tokens.get(0).getLexeme());

        assertEquals(TokenType.RIGHT_PAREN, tokens.get(1).getType());
        assertEquals(")", tokens.get(1).getLexeme());

        assertEquals(TokenType.EOF, tokens.get(2).getType());
    }

    @Test
    void shouldIgnoreComments() {

        Lexer lexer = new Lexer(
                "local x = 10 -- comentario\nprint"
        );

        List<Token> tokens = lexer.scanTokens();

        assertEquals(6, tokens.size());

        assertEquals(TokenType.LOCAL, tokens.get(0).getType());
        assertEquals(TokenType.IDENTIFIER, tokens.get(1).getType());
        assertEquals(TokenType.ASSIGN, tokens.get(2).getType());
        assertEquals(TokenType.INTEGER, tokens.get(3).getType());
        assertEquals(TokenType.IDENTIFIER, tokens.get(4).getType());
    }

    
    @Test
    void shouldIgnoreCommentAtEndOfFile() {

        Lexer lexer = new Lexer("local x -- comentario final");

        List<Token> tokens = lexer.scanTokens();

        assertEquals(3, tokens.size());

        assertEquals(TokenType.LOCAL, tokens.get(0).getType());
        assertEquals(TokenType.IDENTIFIER, tokens.get(1).getType());
        assertEquals(TokenType.EOF, tokens.get(2).getType());
    }

    @Test
    void shouldTrackTokenPositions() {

        Lexer lexer = new Lexer("local x\n  print");

        List<Token> tokens = lexer.scanTokens();

        assertEquals(1, tokens.get(0).getLine());
        assertEquals(1, tokens.get(0).getColumn());

        assertEquals(1, tokens.get(1).getLine());
        assertEquals(7, tokens.get(1).getColumn());

        assertEquals(2, tokens.get(2).getLine());
        assertEquals(3, tokens.get(2).getColumn());
    }

    @Test
    void shouldReturnEofForEmptySource() {

        Lexer lexer = new Lexer("");

        List<Token> tokens = lexer.scanTokens();

        assertEquals(1, tokens.size());
        assertEquals(TokenType.EOF, tokens.get(0).getType());
        assertEquals("", tokens.get(0).getLexeme());
        assertEquals(1, tokens.get(0).getLine());
        assertEquals(1, tokens.get(0).getColumn());
    }
    
    @Test
    void shouldThrowExceptionForInvalidCharacter() {

        Lexer lexer = new Lexer("@");

        RuntimeException exception = org.junit.jupiter.api.Assertions
                .assertThrows(
                        RuntimeException.class,
                        lexer::scanTokens
                );

        assertEquals(
                "Caractere inválido: @ na linha 1, coluna 1",
                exception.getMessage()
        );
    }

    @Test
    void shouldThrowExceptionForInvalidTildeOperator() {

        Lexer lexer = new Lexer("~");

        RuntimeException exception = org.junit.jupiter.api.Assertions
                .assertThrows(
                        RuntimeException.class,
                        lexer::scanTokens
                );

        assertEquals(
                "Operador inválido '~' na linha 1, coluna 1",
                exception.getMessage()
        );
    }

    @Test
    void shouldTrackPositionOfInvalidCharacter() {

        Lexer lexer = new Lexer("local x\n  @");

        RuntimeException exception = org.junit.jupiter.api.Assertions
                .assertThrows(
                        RuntimeException.class,
                        lexer::scanTokens
                );

        assertEquals(
                "Caractere inválido: @ na linha 2, coluna 3",
                exception.getMessage()
        );
    }

    @Test
    void shouldRecognizeCommentWithoutFollowingNewline() {

        Lexer lexer = new Lexer("-- comentario");

        List<Token> tokens = lexer.scanTokens();

        assertEquals(1, tokens.size());
        assertEquals(TokenType.EOF, tokens.get(0).getType());
        assertEquals(1, tokens.get(0).getLine());
        assertEquals(14, tokens.get(0).getColumn());
    }

    @Test
    void shouldRecognizeMultipleOperatorsWithoutSpaces() {

        Lexer lexer = new Lexer("a<=b~=c==d");

        List<Token> tokens = lexer.scanTokens();

        TokenType[] expectedTypes = {
            TokenType.IDENTIFIER,
            TokenType.LESS_EQUAL,
            TokenType.IDENTIFIER,
            TokenType.NOT_EQUAL,
            TokenType.IDENTIFIER,
            TokenType.EQUAL_EQUAL,
            TokenType.IDENTIFIER,
            TokenType.EOF
        };

        assertEquals(expectedTypes.length, tokens.size());

        for (int i = 0; i < expectedTypes.length; i++) {
            assertEquals(expectedTypes[i], tokens.get(i).getType());
        }
    }

}
