
package com.mycompany.minilua.compiler.semantic;

import com.mycompany.minilua.compiler.ast.expression.*;
import com.mycompany.minilua.compiler.ast.statement.*;
import com.mycompany.minilua.compiler.lexer.TokenType;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SemanticAnalyzerTest {

    private final SemanticAnalyzer analyzer = new SemanticAnalyzer();

    @Test
    void shouldAcceptIntegerVariableDeclaration() {

        Statement statement = new VariableDeclarationStatement(
                "idade",
                new LiteralExpression(20)
        );

        List<String> errors = analyzer.analyze(
                List.of(statement)
        );

        assertTrue(errors.isEmpty());
    }

    @Test
    void shouldAcceptBooleanVariableDeclaration() {

        Statement statement = new VariableDeclarationStatement(
                "ativo",
                new LiteralExpression(true)
        );

        List<String> errors = analyzer.analyze(
                List.of(statement)
        );

        assertTrue(errors.isEmpty());
    }

    @Test
    void shouldRejectDuplicateVariableDeclaration() {

        Statement first = new VariableDeclarationStatement(
                "idade",
                new LiteralExpression(20)
        );

        Statement second = new VariableDeclarationStatement(
                "idade",
                new LiteralExpression(30)
        );

        List<String> errors = analyzer.analyze(
                List.of(first, second)
        );

        assertEquals(1, errors.size());
        assertTrue(errors.get(0).contains("já foi declarada"));
    }

    @Test
    void shouldRejectUndeclaredVariableInExpression() {

        Statement statement = new ExpressionStatement(
                new VariableExpression("idade")
        );

        List<String> errors = analyzer.analyze(
                List.of(statement)
        );

        assertEquals(1, errors.size());
        assertTrue(errors.get(0).contains("não foi declarada"));
    }

    @Test
    void shouldRejectAssignmentToUndeclaredVariable() {

        Statement statement = new AssignmentStatement(
                "idade",
                new LiteralExpression(20)
        );

        List<String> errors = analyzer.analyze(
                List.of(statement)
        );

        assertEquals(1, errors.size());
        assertTrue(errors.get(0).contains("não foi declarada"));
    }
    
    
    @Test
    void shouldRejectAssignmentWithIncompatibleType() {

        Statement declaration = new VariableDeclarationStatement(
                "idade",
                new LiteralExpression(20)
        );

        Statement assignment = new AssignmentStatement(
                "idade",
                new LiteralExpression(true)
        );

        List<String> errors = analyzer.analyze(
                List.of(declaration, assignment)
        );

        assertEquals(1, errors.size());
        assertTrue(errors.get(0).contains("Tipo incompatível"));
    }

    @Test
    void shouldRejectArithmeticOperationWithBoolean() {

        Expression expression = new BinaryExpression(
                new LiteralExpression(10),
                com.mycompany.minilua.compiler.lexer.TokenType.PLUS,
                new LiteralExpression(true)
        );

        List<String> errors = analyzer.analyze(
                List.of(new ExpressionStatement(expression))
        );

        assertEquals(1, errors.size());
        assertTrue(errors.get(0).contains("operandos INTEGER"));
    }

    @Test
    void shouldRejectRelationalOperationWithBoolean() {

        Expression expression = new BinaryExpression(
                new LiteralExpression(true),
                com.mycompany.minilua.compiler.lexer.TokenType.LESS,
                new LiteralExpression(10)
        );

        List<String> errors = analyzer.analyze(
                List.of(new ExpressionStatement(expression))
        );

        assertEquals(1, errors.size());
        assertTrue(errors.get(0).contains("Operadores relacionais"));
    }

    @Test
    void shouldRejectEqualityBetweenDifferentTypes() {

        Expression expression = new BinaryExpression(
                new LiteralExpression(10),
                com.mycompany.minilua.compiler.lexer.TokenType.EQUAL_EQUAL,
                new LiteralExpression(true)
        );

        List<String> errors = analyzer.analyze(
                List.of(new ExpressionStatement(expression))
        );

        assertEquals(1, errors.size());
        assertTrue(errors.get(0).contains("mesmo tipo"));
    }
    
    
    @Test
    void shouldAcceptUnaryMinusWithInteger() {

        Expression expression = new UnaryExpression(
                TokenType.MINUS,
                new LiteralExpression(10)
        );

        List<String> errors = analyzer.analyze(
                List.of(new ExpressionStatement(expression))
        );

        assertTrue(errors.isEmpty());
    }

    @Test
    void shouldRejectUnaryMinusWithBoolean() {

        Expression expression = new UnaryExpression(
                TokenType.MINUS,
                new LiteralExpression(true)
        );

        List<String> errors = analyzer.analyze(
                List.of(new ExpressionStatement(expression))
        );

        assertEquals(1, errors.size());
        assertTrue(errors.get(0).contains("operando INTEGER"));
    }

    @Test
    void shouldRejectIfConditionWithInteger() {

        Statement statement = new IfStatement(
                new LiteralExpression(10),
                new BlockStatement(List.of()),
                null
        );

        List<String> errors = analyzer.analyze(
                List.of(statement)
        );

        assertEquals(1, errors.size());
        assertTrue(errors.get(0).contains("condição do if"));
    }

    @Test
    void shouldRejectWhileConditionWithInteger() {

        Statement statement = new WhileStatement(
                new LiteralExpression(10),
                new BlockStatement(List.of())
        );

        List<String> errors = analyzer.analyze(
                List.of(statement)
        );

        assertEquals(1, errors.size());
        assertTrue(errors.get(0).contains("condição do while"));
    }

    @Test
    void shouldAcceptReadWithoutArguments() {

        Expression expression = new CallExpression(
                "read",
                List.of()
        );

        Statement declaration = new VariableDeclarationStatement(
                "entrada",
                expression
        );

        List<String> errors = analyzer.analyze(
                List.of(declaration)
        );

        assertTrue(errors.isEmpty());
    }

    @Test
    void shouldRejectReadWithArguments() {

        Expression expression = new CallExpression(
                "read",
                List.of(new LiteralExpression(10))
        );

        List<String> errors = analyzer.analyze(
                List.of(new ExpressionStatement(expression))
        );

        assertEquals(1, errors.size());
        assertTrue(errors.get(0).contains("read() não aceita argumentos"));
    }
    
    
    @Test
    void shouldAcceptPrintWithOneArgument() {

        Expression expression = new CallExpression(
                "print",
                List.of(new LiteralExpression(10))
        );

        List<String> errors = analyzer.analyze(
                List.of(new ExpressionStatement(expression))
        );

        assertTrue(errors.isEmpty());
    }

    @Test
    void shouldRejectPrintWithoutArguments() {

        Expression expression = new CallExpression(
                "print",
                List.of()
        );

        List<String> errors = analyzer.analyze(
                List.of(new ExpressionStatement(expression))
        );

        assertEquals(1, errors.size());
        assertTrue(errors.get(0).contains("exatamente um argumento"));
    }

    @Test
    void shouldRejectPrintWithMultipleArguments() {

        Expression expression = new CallExpression(
                "print",
                List.of(
                        new LiteralExpression(10),
                        new LiteralExpression(20)
                )
        );

        List<String> errors = analyzer.analyze(
                List.of(new ExpressionStatement(expression))
        );

        assertEquals(1, errors.size());
        assertTrue(errors.get(0).contains("exatamente um argumento"));
    }

    @Test
    void shouldRejectUndefinedFunction() {

        Expression expression = new CallExpression(
                "minhaFuncao",
                List.of()
        );

        List<String> errors = analyzer.analyze(
                List.of(new ExpressionStatement(expression))
        );

        assertEquals(1, errors.size());
        assertTrue(errors.get(0).contains("não foi definida"));
    }

    @Test
    void shouldAllowVariableShadowingInNestedScope() {

        Statement declaration = new VariableDeclarationStatement(
                "valor",
                new LiteralExpression(10)
        );

        Statement innerDeclaration = new VariableDeclarationStatement(
                "valor",
                new LiteralExpression(true)
        );

        Statement block = new BlockStatement(
                List.of(innerDeclaration)
        );

        List<String> errors = analyzer.analyze(
                List.of(declaration, block)
        );

        assertTrue(errors.isEmpty());
    }

    @Test
    void shouldRejectDuplicateVariableInNestedScope() {

        Statement first = new VariableDeclarationStatement(
                "valor",
                new LiteralExpression(10)
        );

        Statement second = new VariableDeclarationStatement(
                "valor",
                new LiteralExpression(20)
        );

        Statement innerBlock = new BlockStatement(
                List.of(second, new VariableDeclarationStatement(
                        "valor",
                        new LiteralExpression(30)
                ))
        );

        List<String> errors = analyzer.analyze(
                List.of(first, innerBlock)
        );

        assertEquals(1, errors.size());
        assertTrue(errors.get(0).contains("já foi declarada"));
    }
    
    
    @Test
    void shouldRejectVariableInitializedWithPrint() {

        Expression printCall = new CallExpression(
                "print",
                List.of(new LiteralExpression(10))
        );

        Statement declaration = new VariableDeclarationStatement(
                "resultado",
                printCall
        );

        List<String> errors = analyzer.analyze(
                List.of(declaration)
        );

        assertEquals(1, errors.size());
        assertTrue(errors.get(0).contains("expressão VOID"));
    }

    @Test
    void shouldRejectPrintNestedInsidePrint() {

        Expression innerPrint = new CallExpression(
                "print",
                List.of(new LiteralExpression(10))
        );

        Expression outerPrint = new CallExpression(
                "print",
                List.of(innerPrint)
        );

        List<String> errors = analyzer.analyze(
                List.of(new ExpressionStatement(outerPrint))
        );

        assertEquals(1, errors.size());
        assertTrue(errors.get(0).contains("não aceita outra chamada print"));
    }

    @Test
    void shouldRejectEqualityWithVoidExpression() {

        Expression printCall = new CallExpression(
                "print",
                List.of(new LiteralExpression(10))
        );

        Expression comparison = new BinaryExpression(
                printCall,
                TokenType.EQUAL_EQUAL,
                new LiteralExpression(10)
        );

        List<String> errors = analyzer.analyze(
                List.of(new ExpressionStatement(comparison))
        );

        assertEquals(1, errors.size());
        assertTrue(errors.get(0).contains("não aceitam expressões VOID"));
    }

    @Test
    void shouldAcceptBooleanEquality() {

        Expression comparison = new BinaryExpression(
                new LiteralExpression(true),
                TokenType.EQUAL_EQUAL,
                new LiteralExpression(false)
        );

        List<String> errors = analyzer.analyze(
                List.of(new ExpressionStatement(comparison))
        );

        assertTrue(errors.isEmpty());
    }

    @Test
    void shouldResetErrorsBetweenAnalysisCalls() {

        Statement invalidStatement = new ExpressionStatement(
                new VariableExpression("inexistente")
        );

        List<String> firstAnalysis = analyzer.analyze(
                List.of(invalidStatement)
        );

        assertEquals(1, firstAnalysis.size());

        Statement validStatement = new VariableDeclarationStatement(
                "idade",
                new LiteralExpression(20)
        );

        List<String> secondAnalysis = analyzer.analyze(
                List.of(validStatement)
        );

        assertTrue(secondAnalysis.isEmpty());
    }
    
    
    @Test
    void shouldAcceptSubtractionWithIntegers() {

        Expression expression = new BinaryExpression(
                new LiteralExpression(10),
                TokenType.MINUS,
                new LiteralExpression(5)
        );

        List<String> errors = analyzer.analyze(
                List.of(new ExpressionStatement(expression))
        );

        assertTrue(errors.isEmpty());
    }

    @Test
    void shouldAcceptMultiplicationWithIntegers() {

        Expression expression = new BinaryExpression(
                new LiteralExpression(10),
                TokenType.MULTIPLY,
                new LiteralExpression(5)
        );

        List<String> errors = analyzer.analyze(
                List.of(new ExpressionStatement(expression))
        );

        assertTrue(errors.isEmpty());
    }
    
    @Test
    void shouldAcceptDivisionWithIntegers() {

        Expression expression = new BinaryExpression(
                new LiteralExpression(10),
                TokenType.DIVIDE,
                new LiteralExpression(5)
        );

        List<String> errors = analyzer.analyze(
                List.of(new ExpressionStatement(expression))
        );

        assertTrue(errors.isEmpty());
    }
    
    @Test
    void shouldAcceptLessThanWithIntegers() {

        Expression expression = new BinaryExpression(
                new LiteralExpression(5),
                TokenType.LESS,
                new LiteralExpression(10)
        );

        List<String> errors = analyzer.analyze(
                List.of(new ExpressionStatement(expression))
        );

        assertTrue(errors.isEmpty());
    }

    @Test
    void shouldAcceptGreaterThanWithIntegers() {

        Expression expression = new BinaryExpression(
                new LiteralExpression(10),
                TokenType.GREATER,
                new LiteralExpression(5)
        );

        List<String> errors = analyzer.analyze(
                List.of(new ExpressionStatement(expression))
        );

        assertTrue(errors.isEmpty());
    }

    @Test
    void shouldAcceptLessOrEqualWithIntegers() {

        Expression expression = new BinaryExpression(
                new LiteralExpression(5),
                TokenType.LESS_EQUAL,
                new LiteralExpression(10)
        );

        List<String> errors = analyzer.analyze(
                List.of(new ExpressionStatement(expression))
        );

        assertTrue(errors.isEmpty());
    }

    @Test
    void shouldAcceptGreaterOrEqualWithIntegers() {

        Expression expression = new BinaryExpression(
                new LiteralExpression(10),
                TokenType.GREATER_EQUAL,
                new LiteralExpression(5)
        );

        List<String> errors = analyzer.analyze(
                List.of(new ExpressionStatement(expression))
        );

        assertTrue(errors.isEmpty());
    }
    
    @Test
    void shouldAcceptIntegerEquality() {

        Expression expression = new BinaryExpression(
                new LiteralExpression(10),
                TokenType.EQUAL_EQUAL,
                new LiteralExpression(10)
        );

        List<String> errors = analyzer.analyze(
                List.of(new ExpressionStatement(expression))
        );

        assertTrue(errors.isEmpty());
    }

    @Test
    void shouldAcceptNotEqualWithIntegers() {

        Expression expression = new BinaryExpression(
                new LiteralExpression(10),
                TokenType.NOT_EQUAL,
                new LiteralExpression(5)
        );

        List<String> errors = analyzer.analyze(
                List.of(new ExpressionStatement(expression))
        );

        assertTrue(errors.isEmpty());
    }
    @Test
    void shouldAcceptAssignmentWithCompatibleType() {

        Statement declaration = new VariableDeclarationStatement(
                "idade",
                new LiteralExpression(20)
        );

        Statement assignment = new AssignmentStatement(
                "idade",
                new LiteralExpression(30)
        );

        List<String> errors = analyzer.analyze(
                List.of(declaration, assignment)
        );

        assertTrue(errors.isEmpty());
    }

    @Test
    void shouldRejectDeclarationWithUndeclaredVariableInitializer() {

        Statement declaration = new VariableDeclarationStatement(
                "resultado",
                new VariableExpression("inexistente")
        );

        List<String> errors = analyzer.analyze(
                List.of(declaration)
        );

        assertEquals(1, errors.size());
        assertTrue(errors.get(0).contains("não foi declarada"));
    }

    @Test
    void shouldAcceptIfConditionWithBoolean() {

        Statement statement = new IfStatement(
                new LiteralExpression(true),
                new BlockStatement(List.of()),
                null
        );

        List<String> errors = analyzer.analyze(
                List.of(statement)
        );

        assertTrue(errors.isEmpty());
    }

    @Test
    void shouldAcceptWhileConditionWithBoolean() {

        Statement statement = new WhileStatement(
                new LiteralExpression(true),
                new BlockStatement(List.of())
        );

        List<String> errors = analyzer.analyze(
                List.of(statement)
        );

        assertTrue(errors.isEmpty());
    }
    @Test
    void shouldRejectArithmeticWithUndeclaredVariable() {

        Expression expression = new BinaryExpression(
                new VariableExpression("inexistente"),
                TokenType.PLUS,
                new LiteralExpression(10)
        );

        List<String> errors = analyzer.analyze(
                List.of(new ExpressionStatement(expression))
        );

        assertEquals(1, errors.size());
        assertTrue(errors.get(0).contains("não foi declarada"));
    }

    @Test
    void shouldRejectRelationalOperationWithIncompatibleTypes() {

        Expression expression = new BinaryExpression(
                new LiteralExpression(10),
                TokenType.GREATER,
                new LiteralExpression(true)
        );

        List<String> errors = analyzer.analyze(
                List.of(new ExpressionStatement(expression))
        );

        assertEquals(1, errors.size());
        assertTrue(errors.get(0).contains("Operadores relacionais"));
    }

    @Test
    void shouldRejectEqualityBetweenTwoVoidExpressions() {

        Expression left = new CallExpression(
                "print",
                List.of(new LiteralExpression(10))
        );

        Expression right = new CallExpression(
                "print",
                List.of(new LiteralExpression(20))
        );

        Expression comparison = new BinaryExpression(
                left,
                TokenType.EQUAL_EQUAL,
                right
        );

        List<String> errors = analyzer.analyze(
                List.of(new ExpressionStatement(comparison))
        );

        assertEquals(1, errors.size());
        assertTrue(errors.get(0).contains("não aceitam expressões VOID"));
    }

    @Test
    void shouldRejectUnsupportedUnaryOperator() {

        Expression expression = new UnaryExpression(
                TokenType.PLUS,
                new LiteralExpression(10)
        );

        List<String> errors = analyzer.analyze(
                List.of(new ExpressionStatement(expression))
        );

        assertEquals(1, errors.size());
        assertTrue(errors.get(0).contains(
                "Operador unário não suportado"
        ));
    }
    @Test
    void shouldRejectVariableDeclaredOnlyInsideBlock() {

        Statement block = new BlockStatement(
                List.of(
                        new VariableDeclarationStatement(
                                "temporaria",
                                new LiteralExpression(10)
                        )
                )
        );

        Statement expression = new ExpressionStatement(
                new VariableExpression("temporaria")
        );

        List<String> errors = analyzer.analyze(
                List.of(block, expression)
        );

        assertEquals(1, errors.size());
        assertTrue(errors.get(0).contains("não foi declarada"));
    }

    @Test
    void shouldAllowAssignmentToOuterVariableInsideBlock() {

        Statement declaration = new VariableDeclarationStatement(
                "idade",
                new LiteralExpression(20)
        );

        Statement block = new BlockStatement(
                List.of(
                        new AssignmentStatement(
                                "idade",
                                new LiteralExpression(30)
                        )
                )
        );

        List<String> errors = analyzer.analyze(
                List.of(declaration, block)
        );

        assertTrue(errors.isEmpty());
    }

    @Test
    void shouldNotKeepDeclarationsBetweenAnalysisCalls() {

        Statement declaration = new VariableDeclarationStatement(
                "idade",
                new LiteralExpression(20)
        );

        analyzer.analyze(List.of(declaration));

        Statement assignment = new AssignmentStatement(
                "idade",
                new LiteralExpression(30)
        );

        List<String> errors = analyzer.analyze(
                List.of(assignment)
        );

        assertEquals(1, errors.size());
        assertTrue(errors.get(0).contains("não foi declarada"));
    }
    @Test
    void shouldRejectUnaryMinusWithUndeclaredVariable() {

        Expression expression = new UnaryExpression(
                TokenType.MINUS,
                new VariableExpression("inexistente")
        );

        List<String> errors = analyzer.analyze(
                List.of(new ExpressionStatement(expression))
        );

        assertEquals(1, errors.size());
        assertTrue(errors.get(0).contains("não foi declarada"));
    }

    @Test
    void shouldRejectPrintWithUndeclaredVariableArgument() {

        Expression printCall = new CallExpression(
                "print",
                List.of(new VariableExpression("inexistente"))
        );

        List<String> errors = analyzer.analyze(
                List.of(new ExpressionStatement(printCall))
        );

        assertEquals(1, errors.size());
        assertTrue(errors.get(0).contains("não foi declarada"));
    }

    @Test
    void shouldAnalyzeIfBodyEvenWhenConditionIsUndeclared() {

        Statement statement = new IfStatement(
                new VariableExpression("condicao"),
                new BlockStatement(
                        List.of(
                                new ExpressionStatement(
                                        new VariableExpression("outraVariavel")
                                )
                        )
                ),
                null
        );

        List<String> errors = analyzer.analyze(
                List.of(statement)
        );

        assertEquals(2, errors.size());

        assertTrue(errors.stream().anyMatch(
                error -> error.contains("condicao")
        ));

        assertTrue(errors.stream().anyMatch(
                error -> error.contains("outraVariavel")
        ));
    }
    @Test
    void shouldRejectWhileConditionWithUndeclaredVariable() {

        Statement statement = new WhileStatement(
                new VariableExpression("condicao"),
                new BlockStatement(List.of())
        );

        List<String> errors = analyzer.analyze(
                List.of(statement)
        );

        assertEquals(1, errors.size());
        assertTrue(errors.get(0).contains(
                "Variável 'condicao' não foi declarada."
        ));
    }
    @Test
    void shouldAnalyzeWhileBodyEvenWhenConditionIsInvalid() {

        Expression printCall = new CallExpression(
                "print",
                List.of(new VariableExpression("inexistente"))
        );

        Statement statement = new WhileStatement(
                new LiteralExpression(10),
                new BlockStatement(List.of(
                        new ExpressionStatement(printCall)
                ))
        );

        List<String> errors = analyzer.analyze(
                List.of(statement)
        );

        assertEquals(2, errors.size());

        assertTrue(errors.stream().anyMatch(error ->
                error.contains("condição do while")
        ));

        assertTrue(errors.stream().anyMatch(error ->
                error.contains("não foi declarada")
        ));
    }
    @Test
    void shouldAnalyzeElseBranchEvenWhenConditionIsInvalid() {

        Statement statement = new IfStatement(
                new LiteralExpression(10),
                new BlockStatement(List.of()),
                new BlockStatement(
                        List.of(
                                new ExpressionStatement(
                                        new VariableExpression("inexistente")
                                )
                        )
                )
        );

        List<String> errors = analyzer.analyze(
                List.of(statement)
        );

        assertEquals(2, errors.size());

        assertTrue(errors.stream().anyMatch(error ->
                error.contains("condição do if")
        ));

        assertTrue(errors.stream().anyMatch(error ->
                error.contains("não foi declarada")
        ));
    }
    @Test
    void shouldRejectVariableDeclaredOnlyInsideWhile() {

        Statement whileStatement = new WhileStatement(
                new LiteralExpression(true),
                new BlockStatement(
                        List.of(
                                new VariableDeclarationStatement(
                                        "temporaria",
                                        new LiteralExpression(10)
                                )
                        )
                )
        );

        Statement expression = new ExpressionStatement(
                new VariableExpression("temporaria")
        );

        List<String> errors = analyzer.analyze(
                List.of(whileStatement, expression)
        );

        assertEquals(1, errors.size());

        assertTrue(errors.get(0).contains(
                "não foi declarada"
        ));
    }
    @Test
    void shouldAllowAccessToOuterVariableInsideWhile() {

        Statement declaration = new VariableDeclarationStatement(
                "contador",
                new LiteralExpression(10)
        );

        Statement whileStatement = new WhileStatement(
                new LiteralExpression(true),
                new BlockStatement(
                        List.of(
                                new ExpressionStatement(
                                        new VariableExpression("contador")
                                )
                        )
                )
        );

        List<String> errors = analyzer.analyze(
                List.of(declaration, whileStatement)
        );

        assertTrue(errors.isEmpty());
    }
    @Test
    void shouldAllowAssignmentToOuterVariableInsideWhile() {

        Statement declaration = new VariableDeclarationStatement(
                "contador",
                new LiteralExpression(10)
        );

        Statement whileStatement = new WhileStatement(
                new LiteralExpression(true),
                new BlockStatement(
                        List.of(
                                new AssignmentStatement(
                                        "contador",
                                        new LiteralExpression(20)
                                )
                        )
                )
        );

        List<String> errors = analyzer.analyze(
                List.of(declaration, whileStatement)
        );

        assertTrue(errors.isEmpty());
    }
    @Test
    void shouldAllowShadowingOuterVariableInsideWhile() {

        Statement declaration = new VariableDeclarationStatement(
                "contador",
                new LiteralExpression(10)
        );

        Statement whileStatement = new WhileStatement(
                new LiteralExpression(true),
                new BlockStatement(
                        List.of(
                                new VariableDeclarationStatement(
                                        "contador",
                                        new LiteralExpression(true)
                                )
                        )
                )
        );

        Statement assignment = new AssignmentStatement(
                "contador",
                new LiteralExpression(20)
        );

        List<String> errors = analyzer.analyze(
                List.of(declaration, whileStatement, assignment)
        );

        assertTrue(errors.isEmpty());
    }
    @Test
    void shouldRejectVariableDeclaredOnlyInsideIfThenBranch() {

        Statement ifStatement = new IfStatement(
                new LiteralExpression(true),
                new BlockStatement(
                        List.of(
                                new VariableDeclarationStatement(
                                        "temporaria",
                                        new LiteralExpression(10)
                                )
                        )
                ),
                null
        );

        Statement expression = new ExpressionStatement(
                new VariableExpression("temporaria")
        );

        List<String> errors = analyzer.analyze(
                List.of(ifStatement, expression)
        );

        assertEquals(1, errors.size());
        assertTrue(errors.get(0).contains("não foi declarada"));
    }
    @Test
    void shouldRejectVariableDeclaredOnlyInsideIfElseBranch() {

        Statement ifStatement = new IfStatement(
                new LiteralExpression(false),
                new BlockStatement(List.of()),
                new BlockStatement(
                        List.of(
                                new VariableDeclarationStatement(
                                        "temporaria",
                                        new LiteralExpression(10)
                                )
                        )
                )
        );

        Statement expression = new ExpressionStatement(
                new VariableExpression("temporaria")
        );

        List<String> errors = analyzer.analyze(
                List.of(ifStatement, expression)
        );

        assertEquals(1, errors.size());
        assertTrue(errors.get(0).contains("não foi declarada"));
    }
    @Test
    void shouldAllowOuterVariableAccessInNestedBlocks() {

        Statement declaration = new VariableDeclarationStatement(
                "valor",
                new LiteralExpression(100)
        );

        Statement nestedBlock = new BlockStatement(
                List.of(
                        new BlockStatement(
                                List.of(
                                        new ExpressionStatement(
                                                new VariableExpression("valor")
                                        )
                                )
                        )
                )
        );

        List<String> errors = analyzer.analyze(
                List.of(declaration, nestedBlock)
        );

        assertTrue(errors.isEmpty());
    }
    @Test
    void shouldRejectDuplicateDeclarationInsideWhileBlock() {

        Statement whileStatement = new WhileStatement(
                new LiteralExpression(true),
                new BlockStatement(
                        List.of(
                                new VariableDeclarationStatement(
                                        "contador",
                                        new LiteralExpression(10)
                                ),
                                new VariableDeclarationStatement(
                                        "contador",
                                        new LiteralExpression(20)
                                )
                        )
                )
        );

        List<String> errors = analyzer.analyze(
                List.of(whileStatement)
        );

        assertEquals(1, errors.size());
        assertTrue(errors.get(0).contains("já foi declarada"));
    }
    @Test
    void shouldRejectArithmeticWithUndeclaredRightOperand() {

        Expression expression = new BinaryExpression(
                new LiteralExpression(10),
                TokenType.PLUS,
                new VariableExpression("inexistente")
        );

        List<String> errors = analyzer.analyze(
                List.of(new ExpressionStatement(expression))
        );

        assertEquals(1, errors.size());
        assertTrue(errors.get(0).contains("não foi declarada"));
    }
    @Test
    void shouldRejectRelationalOperationWithUndeclaredRightOperand() {

        Expression expression = new BinaryExpression(
                new LiteralExpression(10),
                TokenType.GREATER,
                new VariableExpression("inexistente")
        );

        List<String> errors = analyzer.analyze(
                List.of(new ExpressionStatement(expression))
        );

        assertEquals(1, errors.size());
        assertTrue(errors.get(0).contains("não foi declarada"));
    }
    @Test
    void shouldRejectEqualityWithTwoUndeclaredOperands() {

        Expression expression = new BinaryExpression(
                new VariableExpression("primeira"),
                TokenType.EQUAL_EQUAL,
                new VariableExpression("segunda")
        );

        List<String> errors = analyzer.analyze(
                List.of(new ExpressionStatement(expression))
        );

        assertEquals(2, errors.size());

        assertTrue(errors.stream().anyMatch(error ->
                error.contains("primeira")
        ));

        assertTrue(errors.stream().anyMatch(error ->
                error.contains("segunda")
        ));
    }
    @Test
    void shouldAcceptEqualityBetweenBooleanVariables() {

        Statement first = new VariableDeclarationStatement(
                "ativo",
                new LiteralExpression(true)
        );

        Statement second = new VariableDeclarationStatement(
                "habilitado",
                new LiteralExpression(false)
        );

        Expression comparison = new BinaryExpression(
                new VariableExpression("ativo"),
                TokenType.EQUAL_EQUAL,
                new VariableExpression("habilitado")
        );

        List<String> errors = analyzer.analyze(
                List.of(first, second, new ExpressionStatement(comparison))
        );

        assertTrue(errors.isEmpty());
    }
    @Test
    void shouldAcceptNotEqualBetweenBooleanValues() {

        Expression expression = new BinaryExpression(
                new LiteralExpression(true),
                TokenType.NOT_EQUAL,
                new LiteralExpression(false)
        );

        List<String> errors = analyzer.analyze(
                List.of(new ExpressionStatement(expression))
        );

        assertTrue(errors.isEmpty());
    }
    @Test
    void shouldRejectArithmeticBetweenTwoBooleanValues() {

        Expression expression = new BinaryExpression(
                new LiteralExpression(true),
                TokenType.PLUS,
                new LiteralExpression(false)
        );

        List<String> errors = analyzer.analyze(
                List.of(new ExpressionStatement(expression))
        );

        assertEquals(1, errors.size());
        assertTrue(errors.get(0).contains("operandos INTEGER"));
    }
    @Test
    void shouldRejectRelationalComparisonBetweenBooleanValues() {

        Expression expression = new BinaryExpression(
                new LiteralExpression(true),
                TokenType.LESS,
                new LiteralExpression(false)
        );

        List<String> errors = analyzer.analyze(
                List.of(new ExpressionStatement(expression))
        );

        assertEquals(1, errors.size());
        assertTrue(errors.get(0).contains("Operadores relacionais"));
    }
    @Test
    void shouldRejectAssignmentWithUndeclaredValueVariable() {

        List<Statement> statements = List.of(
                new VariableDeclarationStatement(
                        "idade",
                        new LiteralExpression(20)
                ),
                new AssignmentStatement(
                        "idade",
                        new VariableExpression("inexistente")
                )
        );

        List<String> errors = new SemanticAnalyzer().analyze(statements);

        assertTrue(errors.stream().anyMatch(
                error -> error.contains(
                        "Variável 'inexistente' não foi declarada."
                )
        ));

        assertFalse(errors.stream().anyMatch(
                error -> error.contains("Tipo incompatível na atribuição")
        ));
    }
    @Test
    void shouldRejectEqualityWithVoidOnRightOperand() {

        Expression printCall = new CallExpression(
                "print",
                List.of(new LiteralExpression(10))
        );

        Expression comparison = new BinaryExpression(
                new LiteralExpression(10),
                TokenType.EQUAL_EQUAL,
                printCall
        );

        List<String> errors = analyzer.analyze(
                List.of(new ExpressionStatement(comparison))
        );

        assertEquals(1, errors.size());
        assertTrue(errors.get(0).contains(
                "não aceitam expressões VOID"
        ));
    }

    @Test
    void shouldRejectUnsupportedBinaryOperator() {

        Expression expression = new BinaryExpression(
                new LiteralExpression(10),
                TokenType.ASSIGN,
                new LiteralExpression(20)
        );

        List<String> errors = analyzer.analyze(
                List.of(new ExpressionStatement(expression))
        );

        assertEquals(1, errors.size());
        assertTrue(errors.get(0).contains(
                "Operador binário não suportado."
        ));
    }
    @Test
    void shouldReturnNullForUnsupportedLiteralType() {

        Expression expression = new LiteralExpression("texto");

        List<String> errors = analyzer.analyze(
                List.of(new ExpressionStatement(expression))
        );

        assertTrue(errors.isEmpty());
    }
    @Test
    void shouldIgnoreUnsupportedStatementType() {

        Statement statement = new Statement() {};

        List<String> errors = analyzer.analyze(
                List.of(statement)
        );

        assertTrue(errors.isEmpty());
    }
    @Test
    void shouldReturnNullForUnsupportedExpressionType() {

        Expression expression = new Expression() {};

        List<String> errors = analyzer.analyze(
                List.of(new ExpressionStatement(expression))
        );

        assertTrue(errors.isEmpty());
    }    
    @Test
    void shouldNotReportUndeclaredVariableAfterInvalidInitializer() {

        Statement declaration = new VariableDeclarationStatement(
                "resultado",
                new BinaryExpression(
                        new VariableExpression("desconhecida"),
                        TokenType.PLUS,
                        new LiteralExpression(10)
                )
        );

        Statement print = new ExpressionStatement(
                new CallExpression(
                        "print",
                        List.of(new VariableExpression("resultado"))
                )
        );

        List<String> errors = analyzer.analyze(
                List.of(declaration, print)
        );

        assertEquals(1, errors.size());

        assertTrue(errors.get(0).contains(
                "Variável 'desconhecida' não foi declarada."
        ));

        assertFalse(errors.stream().anyMatch(error ->
                error.contains("resultado")
        ));
    }
    
}
