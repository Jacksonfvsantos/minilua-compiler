
package com.mycompany.minilua.compiler.ast.printer;

import com.mycompany.minilua.compiler.ast.expression.LiteralExpression;
import com.mycompany.minilua.compiler.ast.expression.VariableExpression;
import com.mycompany.minilua.compiler.ast.statement.AssignmentStatement;
import com.mycompany.minilua.compiler.ast.statement.ExpressionStatement;
import com.mycompany.minilua.compiler.ast.statement.VariableDeclarationStatement;
import com.mycompany.minilua.compiler.ast.expression.BinaryExpression;
import com.mycompany.minilua.compiler.ast.expression.UnaryExpression;
import com.mycompany.minilua.compiler.lexer.TokenType;
import com.mycompany.minilua.compiler.ast.statement.BlockStatement;
import com.mycompany.minilua.compiler.ast.statement.IfStatement;
import com.mycompany.minilua.compiler.ast.statement.WhileStatement;
import com.mycompany.minilua.compiler.ast.expression.CallExpression;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ASTPrinterTest {

    private final ASTPrinter printer = new ASTPrinter();

    @Test
    void shouldPrintVariableDeclaration() {

        List<com.mycompany.minilua.compiler.ast.statement.Statement> statements =
                List.of(
                        new VariableDeclarationStatement(
                                "idade",
                                new LiteralExpression(20)
                        )
                );

        String result = printer.print(statements);

        String expected =
                "Program\n" +
                "VariableDeclaration: idade\n" +
                "  Literal: 20\n";

        assertEquals(expected, result);
    }

    @Test
    void shouldPrintAssignment() {

        List<com.mycompany.minilua.compiler.ast.statement.Statement> statements =
                List.of(
                        new AssignmentStatement(
                                "idade",
                                new LiteralExpression(25)
                        )
                );

        String result = printer.print(statements);

        String expected =
                "Program\n" +
                "Assignment: idade\n" +
                "  Literal: 25\n";

        assertEquals(expected, result);
    }

    @Test
    void shouldPrintExpressionStatement() {

        List<com.mycompany.minilua.compiler.ast.statement.Statement> statements =
                List.of(
                        new ExpressionStatement(
                                new VariableExpression("idade")
                        )
                );

        String result = printer.print(statements);

        String expected =
                "Program\n" +
                "ExpressionStatement\n" +
                "  Variable: idade\n";

        assertEquals(expected, result);
    }

    @Test
    void shouldPrintMultipleStatements() {

        List<com.mycompany.minilua.compiler.ast.statement.Statement> statements =
                List.of(
                        new VariableDeclarationStatement(
                                "idade",
                                new LiteralExpression(20)
                        ),
                        new AssignmentStatement(
                                "idade",
                                new LiteralExpression(25)
                        ),
                        new ExpressionStatement(
                                new VariableExpression("idade")
                        )
                );

        String result = printer.print(statements);

        String expected =
                "Program\n" +
                "VariableDeclaration: idade\n" +
                "  Literal: 20\n" +
                "Assignment: idade\n" +
                "  Literal: 25\n" +
                "ExpressionStatement\n" +
                "  Variable: idade\n";

        assertEquals(expected, result);
    }
        
    @Test
    void shouldPrintBinaryExpression() {

        List<com.mycompany.minilua.compiler.ast.statement.Statement> statements =
                List.of(
                        new ExpressionStatement(
                                new BinaryExpression(
                                        new LiteralExpression(10),
                                        TokenType.PLUS,
                                        new LiteralExpression(5)
                                )
                        )
                );

        String result = printer.print(statements);

        String expected =
                "Program\n" +
                "ExpressionStatement\n" +
                "  BinaryExpression: PLUS\n" +
                "    Literal: 10\n" +
                "    Literal: 5\n";

        assertEquals(expected, result);
    }

    @Test
    void shouldPrintUnaryExpression() {

        List<com.mycompany.minilua.compiler.ast.statement.Statement> statements =
                List.of(
                        new ExpressionStatement(
                                new UnaryExpression(
                                        TokenType.MINUS,
                                        new LiteralExpression(10)
                                )
                        )
                );

        String result = printer.print(statements);

        String expected =
                "Program\n" +
                "ExpressionStatement\n" +
                "  UnaryExpression: MINUS\n" +
                "    Literal: 10\n";

        assertEquals(expected, result);
    }

    @Test
    void shouldPrintNestedBinaryExpressions() {

        List<com.mycompany.minilua.compiler.ast.statement.Statement> statements =
                List.of(
                        new ExpressionStatement(
                                new BinaryExpression(
                                        new LiteralExpression(2),
                                        TokenType.MULTIPLY,
                                        new BinaryExpression(
                                                new LiteralExpression(3),
                                                TokenType.PLUS,
                                                new LiteralExpression(4)
                                        )
                                )
                        )
                );

        String result = printer.print(statements);

        String expected =
                "Program\n" +
                "ExpressionStatement\n" +
                "  BinaryExpression: MULTIPLY\n" +
                "    Literal: 2\n" +
                "    BinaryExpression: PLUS\n" +
                "      Literal: 3\n" +
                "      Literal: 4\n";

        assertEquals(expected, result);
    }
    
    @Test
    void shouldPrintCallExpressionWithoutArguments() {

        List<com.mycompany.minilua.compiler.ast.statement.Statement> statements =
                List.of(
                        new ExpressionStatement(
                                new CallExpression("read", List.of())
                        )
                );

        String result = printer.print(statements);

        String expected =
                "Program\n" +
                "ExpressionStatement\n" +
                "  CallExpression: read\n";

        assertEquals(expected, result);
    }

    @Test
    void shouldPrintCallExpressionWithOneArgument() {

        List<com.mycompany.minilua.compiler.ast.statement.Statement> statements =
                List.of(
                        new ExpressionStatement(
                                new CallExpression(
                                        "print",
                                        List.of(new LiteralExpression(10))
                                )
                        )
                );

        String result = printer.print(statements);

        String expected =
                "Program\n" +
                "ExpressionStatement\n" +
                "  CallExpression: print\n" +
                "    Literal: 10\n";

        assertEquals(expected, result);
    }

    @Test
    void shouldPrintCallExpressionWithMultipleArguments() {

        List<com.mycompany.minilua.compiler.ast.statement.Statement> statements =
                List.of(
                        new ExpressionStatement(
                                new CallExpression(
                                        "calcular",
                                        List.of(
                                                new LiteralExpression(10),
                                                new VariableExpression("idade"),
                                                new LiteralExpression(5)
                                        )
                                )
                        )
                );

        String result = printer.print(statements);

        String expected =
                "Program\n" +
                "ExpressionStatement\n" +
                "  CallExpression: calcular\n" +
                "    Literal: 10\n" +
                "    Variable: idade\n" +
                "    Literal: 5\n";

        assertEquals(expected, result);
    }
    
    @Test
    void shouldPrintEmptyBlockStatement() {

        List<com.mycompany.minilua.compiler.ast.statement.Statement> statements =
                List.of(
                        new BlockStatement(List.of())
                );

        String result = printer.print(statements);

        String expected =
                "Program\n" +
                "BlockStatement\n";

        assertEquals(expected, result);
    }

    @Test
    void shouldPrintBlockStatementWithMultipleStatements() {

        List<com.mycompany.minilua.compiler.ast.statement.Statement> statements =
                List.of(
                        new BlockStatement(
                                List.of(
                                        new VariableDeclarationStatement(
                                                "idade",
                                                new LiteralExpression(20)
                                        ),
                                        new ExpressionStatement(
                                                new VariableExpression("idade")
                                        )
                                )
                        )
                );

        String result = printer.print(statements);

        String expected =
                "Program\n" +
                "BlockStatement\n" +
                "  VariableDeclaration: idade\n" +
                "    Literal: 20\n" +
                "  ExpressionStatement\n" +
                "    Variable: idade\n";

        assertEquals(expected, result);
    }

    @Test
    void shouldPrintIfStatementWithoutElseBranch() {

        List<com.mycompany.minilua.compiler.ast.statement.Statement> statements =
                List.of(
                        new IfStatement(
                                new LiteralExpression(true),
                                new BlockStatement(
                                        List.of(
                                                new ExpressionStatement(
                                                        new LiteralExpression(10)
                                                )
                                        )
                                ),
                                null
                        )
                );

        String result = printer.print(statements);

        String expected =
                "Program\n" +
                "IfStatement\n" +
                "  Condition\n" +
                "    Literal: true\n" +
                "  ThenBranch\n" +
                "    BlockStatement\n" +
                "      ExpressionStatement\n" +
                "        Literal: 10\n";

        assertEquals(expected, result);
    }

    @Test
    void shouldPrintIfStatementWithElseBranch() {

        List<com.mycompany.minilua.compiler.ast.statement.Statement> statements =
                List.of(
                        new IfStatement(
                                new LiteralExpression(false),
                                new BlockStatement(
                                        List.of(
                                                new ExpressionStatement(
                                                        new LiteralExpression(10)
                                                )
                                        )
                                ),
                                new BlockStatement(
                                        List.of(
                                                new ExpressionStatement(
                                                        new LiteralExpression(20)
                                                )
                                        )
                                )
                        )
                );

        String result = printer.print(statements);

        String expected =
                "Program\n" +
                "IfStatement\n" +
                "  Condition\n" +
                "    Literal: false\n" +
                "  ThenBranch\n" +
                "    BlockStatement\n" +
                "      ExpressionStatement\n" +
                "        Literal: 10\n" +
                "  ElseBranch\n" +
                "    BlockStatement\n" +
                "      ExpressionStatement\n" +
                "        Literal: 20\n";

        assertEquals(expected, result);
    }

    @Test
    void shouldPrintWhileStatement() {

        List<com.mycompany.minilua.compiler.ast.statement.Statement> statements =
                List.of(
                        new WhileStatement(
                                new LiteralExpression(true),
                                new BlockStatement(
                                        List.of(
                                                new ExpressionStatement(
                                                        new LiteralExpression(10)
                                                )
                                        )
                                )
                        )
                );

        String result = printer.print(statements);

        String expected =
                "Program\n" +
                "WhileStatement\n" +
                "  Condition\n" +
                "    Literal: true\n" +
                "  Body\n" +
                "    BlockStatement\n" +
                "      ExpressionStatement\n" +
                "        Literal: 10\n";

        assertEquals(expected, result);
    }
    
    @Test
    void shouldResetPrinterStateBetweenCalls() {

        String firstResult = printer.print(
                List.of(
                        new BlockStatement(
                                List.of(
                                        new ExpressionStatement(
                                                new LiteralExpression(10)
                                        )
                                )
                        )
                )
        );

        String secondResult = printer.print(
                List.of(
                        new ExpressionStatement(
                                new LiteralExpression(20)
                        )
                )
        );

        String expected =
                "Program\n" +
                "ExpressionStatement\n" +
                "  Literal: 20\n";

        assertEquals(expected, secondResult);
    }

    @Test
    void shouldPrintNestedBlocks() {

        List<com.mycompany.minilua.compiler.ast.statement.Statement> statements =
                List.of(
                        new BlockStatement(
                                List.of(
                                        new BlockStatement(
                                                List.of(
                                                        new ExpressionStatement(
                                                                new LiteralExpression(10)
                                                        )
                                                )
                                        )
                                )
                        )
                );

        String result = printer.print(statements);

        String expected =
                "Program\n" +
                "BlockStatement\n" +
                "  BlockStatement\n" +
                "    ExpressionStatement\n" +
                "      Literal: 10\n";

        assertEquals(expected, result);
    }

    @Test
    void shouldPrintEmptyProgram() {

        String result = printer.print(List.of());

        assertEquals("Program\n", result);
    }
    
    @Test
    void shouldPrintUnknownStatement() {

        com.mycompany.minilua.compiler.ast.statement.Statement unknownStatement =
                new com.mycompany.minilua.compiler.ast.statement.Statement() {};

        String result = printer.print(List.of(unknownStatement));

        String expected =
                "Program\n" +
                "UnknownStatement\n";

        assertEquals(expected, result);
    }

    @Test
    void shouldPrintUnknownExpression() {

        com.mycompany.minilua.compiler.ast.expression.Expression unknownExpression =
                new com.mycompany.minilua.compiler.ast.expression.Expression() {};

        String result = printer.print(
                List.of(
                        new ExpressionStatement(unknownExpression)
                )
        );

        String expected =
                "Program\n" +
                "ExpressionStatement\n" +
                "  UnknownExpression\n";

        assertEquals(expected, result);
    }
    
}
