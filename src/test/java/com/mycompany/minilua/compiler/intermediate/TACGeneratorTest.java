
package com.mycompany.minilua.compiler.intermediate;

import com.mycompany.minilua.compiler.ast.expression.CallExpression;
import com.mycompany.minilua.compiler.ast.expression.Expression;
import com.mycompany.minilua.compiler.ast.expression.LiteralExpression;
import com.mycompany.minilua.compiler.ast.statement.AssignmentStatement;
import com.mycompany.minilua.compiler.ast.statement.ExpressionStatement;
import com.mycompany.minilua.compiler.ast.statement.Statement;
import com.mycompany.minilua.compiler.ast.statement.IfStatement;
import com.mycompany.minilua.compiler.ast.statement.BlockStatement;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TACGeneratorTest {

    @Test
    public void shouldThrowExceptionWhenVariableDoesNotExist() {

        Statement statement = new AssignmentStatement(
                "inexistente",
                new LiteralExpression(10)
        );

        TACGenerator generator = new TACGenerator();

        assertThrows(
                IllegalStateException.class,
                () -> generator.generate(List.of(statement))
        );
    }

    @Test
    public void shouldThrowExceptionWhenExpressionIsNotSupported() {

        Expression unsupportedExpression = new Expression() {
        };

        Statement statement = new ExpressionStatement(
                unsupportedExpression
        );

        TACGenerator generator = new TACGenerator();

        assertThrows(
                IllegalArgumentException.class,
                () -> generator.generate(List.of(statement))
        );
    }

    @Test
    public void shouldThrowExceptionWhenFunctionIsNotSupported() {

        Expression unsupportedCall = new CallExpression(
                "funcaoDesconhecida",
                List.of()
        );

        Statement statement = new ExpressionStatement(
                unsupportedCall
        );

        TACGenerator generator = new TACGenerator();

        assertThrows(
                IllegalArgumentException.class,
                () -> generator.generate(List.of(statement))
        );
    }
    @Test
    public void shouldGenerateIfStatementWithoutElseBranch() {

        Statement statement = new IfStatement(
                new LiteralExpression(true),
                new BlockStatement(
                        List.of(
                                new ExpressionStatement(
                                        new LiteralExpression(10)
                                )
                        )
                ),
                null
        );

        TACGenerator generator = new TACGenerator();

        List<TACInstruction> instructions = generator.generate(
                List.of(statement)
        );

        assertEquals(2, instructions.size());

        assertEquals("IF_FALSE", instructions.get(0).getOperation());
        assertEquals("true", instructions.get(0).getArg1());
        assertEquals("L1", instructions.get(0).getResult());

        assertEquals("LABEL", instructions.get(1).getOperation());
        assertEquals("L1", instructions.get(1).getResult());
    }
    @Test
    void shouldIgnoreUnsupportedStatement() {
        Statement unsupported = new Statement() {};

        TACGenerator generator = new TACGenerator();

        List<TACInstruction> result = generator.generate(
                List.of(unsupported)
        );

        assertTrue(result.isEmpty());
    }
}
