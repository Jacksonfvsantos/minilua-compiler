
package com.mycompany.minilua.compiler.parser;

import com.mycompany.minilua.compiler.ast.statement.*;
import com.mycompany.minilua.compiler.lexer.Lexer;
import com.mycompany.minilua.compiler.lexer.Token;
import org.junit.jupiter.api.Test;
import com.mycompany.minilua.compiler.ast.expression.*;
import com.mycompany.minilua.compiler.lexer.TokenType;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ParserTest {

    private List<Statement> parse(String source) {

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();

        Parser parser = new Parser(tokens);

        return parser.parse();
    }

    @Test
    void shouldParseVariableDeclaration() {

        List<Statement> statements = parse(
                "local idade = 20"
        );

        assertEquals(1, statements.size());

        assertTrue(
                statements.get(0)
                        instanceof VariableDeclarationStatement
        );
    }

    @Test
    void shouldParseAssignment() {

        List<Statement> statements = parse(
                "idade = 25"
        );

        assertEquals(1, statements.size());

        assertTrue(
                statements.get(0)
                        instanceof AssignmentStatement
        );
    }

    @Test
    void shouldParseIfWithoutElse() {

        List<Statement> statements = parse(
                "if idade > 18 then "
                + "print(idade) "
                + "end"
        );

        assertEquals(1, statements.size());

        assertTrue(
                statements.get(0) instanceof IfStatement
        );
    }

    @Test
    void shouldParseIfWithElse() {

        List<Statement> statements = parse(
                "if idade > 18 then "
                + "print(idade) "
                + "else "
                + "print(0) "
                + "end"
        );

        assertEquals(1, statements.size());

        assertTrue(
                statements.get(0) instanceof IfStatement
        );
    }

    @Test
    void shouldParseWhileStatement() {

        List<Statement> statements = parse(
                "while contador < 10 do "
                + "contador = contador + 1 "
                + "end"
        );

        assertEquals(1, statements.size());

        assertTrue(
                statements.get(0) instanceof WhileStatement
        );
    }
        
    @Test
    void shouldParseExpressionStatement() {

        List<Statement> statements = parse("resultado + 20");

        assertEquals(1, statements.size());

        assertTrue(
                statements.get(0) instanceof ExpressionStatement
        );
    }

    @Test
    void shouldParseFunctionCallWithoutArguments() {

        List<Statement> statements = parse("read()");

        assertEquals(1, statements.size());

        assertTrue(
                statements.get(0) instanceof ExpressionStatement
        );
    }

    @Test
    void shouldParseFunctionCallWithOneArgument() {

        List<Statement> statements = parse("print(10)");

        assertEquals(1, statements.size());

        assertTrue(
                statements.get(0) instanceof ExpressionStatement
        );
    }

    @Test
    void shouldParseUnaryExpression() {

        List<Statement> statements = parse("local resultado = -10");

        assertEquals(1, statements.size());

        assertTrue(
                statements.get(0)
                        instanceof VariableDeclarationStatement
        );
    }

    @Test
    void shouldParseExpressionWithOperatorPrecedence() {

        List<Statement> statements = parse(
                "local resultado = 10 + 5 * 2"
        );

        assertEquals(1, statements.size());

        assertTrue(
                statements.get(0)
                        instanceof VariableDeclarationStatement
        );
    }

    @Test
    void shouldRespectMultiplicationPrecedenceOverAddition() {

        List<Statement> statements = parse(
                "local resultado = 10 + 5 * 2"
        );

        VariableDeclarationStatement declaration =
                (VariableDeclarationStatement) statements.get(0);

        BinaryExpression root =
                (BinaryExpression) declaration.getInitializer();

        assertEquals(TokenType.PLUS, root.getOperator());

        BinaryExpression right =
                (BinaryExpression) root.getRight();

        assertEquals(TokenType.MULTIPLY, right.getOperator());
    }

    @Test
    void shouldParseComparisonExpression() {

        List<Statement> statements = parse(
                "local resultado = idade >= 18"
        );

        VariableDeclarationStatement declaration =
                (VariableDeclarationStatement) statements.get(0);

        BinaryExpression expression =
                (BinaryExpression) declaration.getInitializer();

        assertEquals(TokenType.GREATER_EQUAL, expression.getOperator());
    }

    @Test
    void shouldParseBooleanLiteralTrue() {

        List<Statement> statements = parse(
                "local ativo = true"
        );

        VariableDeclarationStatement declaration =
                (VariableDeclarationStatement) statements.get(0);

        LiteralExpression expression =
                (LiteralExpression) declaration.getInitializer();

        assertEquals(true, expression.getValue());
    }

    @Test
    void shouldParseBooleanLiteralFalse() {

        List<Statement> statements = parse(
                "local ativo = false"
        );

        VariableDeclarationStatement declaration =
                (VariableDeclarationStatement) statements.get(0);

        LiteralExpression expression =
                (LiteralExpression) declaration.getInitializer();

        assertEquals(false, expression.getValue());
    }

    @Test
    void shouldParseParenthesizedExpression() {

        List<Statement> statements = parse(
                "local resultado = (10 + 5) * 2"
        );

        VariableDeclarationStatement declaration =
                (VariableDeclarationStatement) statements.get(0);

        BinaryExpression expression =
                (BinaryExpression) declaration.getInitializer();

        assertEquals(TokenType.MULTIPLY, expression.getOperator());

        BinaryExpression left =
                (BinaryExpression) expression.getLeft();

        assertEquals(TokenType.PLUS, left.getOperator());
    }
    
    @Test
    void shouldThrowExceptionWhenVariableNameIsMissing() {

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> parse("local = 10")
        );

        assertTrue(
                exception.getMessage().contains(
                        "Esperado um nome para a variável."
                )
        );
    }

    @Test
    void shouldThrowExceptionWhenAssignmentOperatorIsMissing() {

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> parse("local idade 20")
        );

        assertTrue(
                exception.getMessage().contains(
                        "Esperado '=' após o nome da variável."
                )
        );
    }

    @Test
    void shouldThrowExceptionWhenThenIsMissing() {

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> parse("if idade > 18 print(idade) end")
        );

        assertTrue(
                exception.getMessage().contains(
                        "Esperado 'then' após a condição."
                )
        );
    }

    @Test
    void shouldThrowExceptionWhenWhileDoIsMissing() {

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> parse("while idade < 18 idade = idade + 1 end")
        );

        assertTrue(
                exception.getMessage().contains(
                        "Esperado 'do' após a condição do while."
                )
        );
    }

    @Test
    void shouldThrowExceptionWhenIfEndIsMissing() {

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> parse("if idade > 18 then print(idade)")
        );

        assertTrue(
                exception.getMessage().contains(
                        "Esperado 'end' para finalizar o if."
                )
        );
    }

    @Test
    void shouldThrowExceptionWhenExpressionIsMissingInDeclaration() {

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> parse("local idade =")
        );

        assertTrue(
                exception.getMessage().contains(
                        "Esperada uma expressão."
                )
        );
    }

    @Test
    void shouldThrowExceptionWhenFunctionClosingParenthesisIsMissing() {

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> parse("print(10")
        );

        assertTrue(
                exception.getMessage().contains(
                        "Esperado ')' após os argumentos da função."
                )
        );
    }

    @Test
    void shouldThrowExceptionWhenExpressionClosingParenthesisIsMissing() {

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> parse("local resultado = (10 + 5")
        );

        assertTrue(
                exception.getMessage().contains(
                        "Esperado ')' após a expressão."
                )
        );
    }

    @Test
    void shouldThrowExceptionWhenIfConditionIsMissing() {

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> parse("if then print(10) end")
        );

        assertTrue(
                exception.getMessage().contains(
                        "Esperada uma expressão."
                )
        );
    }

    @Test
    void shouldThrowExceptionWhenWhileConditionIsMissing() {

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> parse("while do print(10) end")
        );

        assertTrue(
                exception.getMessage().contains(
                        "Esperada uma expressão."
                )
        );
    }

    @Test
    void shouldThrowExceptionForInvalidStatement() {

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> parse("10")
        );

        assertTrue(
                exception.getMessage().contains(
                        "Instrução inválida."
                )
        );
    }
    
    @Test
    void shouldNotIncrementPositionWhenAtEnd() throws Exception {

        Lexer lexer = new Lexer("10");
        List<Token> tokens = lexer.scanTokens();

        Parser parser = new Parser(tokens);

        var advance = Parser.class.getDeclaredMethod("advance");
        advance.setAccessible(true);

        advance.invoke(parser); // Consome INTEGER e posiciona no EOF.

        Token result = (Token) advance.invoke(parser);

        assertEquals(TokenType.INTEGER, result.getType());
    }
    
    @Test
    void shouldReturnFalseWhenThereIsNoNextToken() throws Exception {

        Lexer lexer = new Lexer("idade");
        List<Token> tokens = lexer.scanTokens();

        Parser parser = new Parser(tokens);

        var checkNext = Parser.class.getDeclaredMethod(
                "checkNext",
                TokenType.class
        );

        checkNext.setAccessible(true);

        assertEquals(
                false,
                checkNext.invoke(parser, TokenType.ASSIGN)
        );
    }
    @Test
    void shouldCheckNextTokenWhenNextTokenExists() throws Exception {

        Lexer lexer = new Lexer("idade = 10");
        List<Token> tokens = lexer.scanTokens();

        Parser parser = new Parser(tokens);

        var checkNext = Parser.class.getDeclaredMethod(
                "checkNext",
                TokenType.class
        );

        checkNext.setAccessible(true);

        assertEquals(
                true,
                checkNext.invoke(parser, TokenType.ASSIGN)
        );
    }
    @Test
    void shouldReturnFalseWhenCurrentTokenIsEof() throws Exception {
        Lexer lexer = new Lexer("idade");
        List<Token> tokens = lexer.scanTokens();
        Parser parser = new Parser(tokens);

        var currentField = Parser.class.getDeclaredField("current");
        currentField.setAccessible(true);
        currentField.setInt(parser, tokens.size() - 1);

        var checkNext = Parser.class.getDeclaredMethod("checkNext", TokenType.class);
        checkNext.setAccessible(true);

        assertEquals(false, checkNext.invoke(parser, TokenType.ASSIGN));
    }
}