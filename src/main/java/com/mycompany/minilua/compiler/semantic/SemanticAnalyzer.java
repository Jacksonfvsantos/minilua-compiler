package com.mycompany.minilua.compiler.semantic;

import com.mycompany.minilua.compiler.ast.expression.*;
import com.mycompany.minilua.compiler.ast.statement.*;

import com.mycompany.minilua.compiler.lexer.TokenType;

import java.util.ArrayList;
import java.util.List;

public class SemanticAnalyzer {

    private final SymbolTable symbolTable = new SymbolTable();
    private final List<String> errors = new ArrayList<>();

    public List<String> analyze(List<Statement> statements) {

        errors.clear();

        symbolTable.enterScope();

        for (Statement statement : statements) {
            analyzeStatement(statement);
        }

        symbolTable.exitScope();

        return errors;
    }

    private void analyzeStatement(Statement statement) {

        if (statement instanceof VariableDeclarationStatement declaration) {

            MiniLuaType initializerType =
                    analyzeExpression(declaration.getInitializer());

            if (initializerType != MiniLuaType.VOID) {

                boolean declared = symbolTable.declare(
                        declaration.getName(),
                        initializerType
                );

                if (!declared) {
                    errors.add(
                            "Variável '" + declaration.getName()
                            + "' já foi declarada neste escopo."
                    );
                }

            } else {

                errors.add(
                        "Não é possível inicializar uma variável com uma expressão VOID."
                );
            }

        } else if (statement instanceof AssignmentStatement assignment) {

            MiniLuaType variableType =
                    symbolTable.lookup(assignment.getName());

            MiniLuaType valueType =
                    analyzeExpression(assignment.getValue());

            if (variableType == null) {

                errors.add(
                        "Variável '" + assignment.getName()
                        + "' não foi declarada."
                );

            } else if (variableType != MiniLuaType.ERROR && valueType != MiniLuaType.ERROR
                        && variableType != valueType) {

                errors.add(
                        "Tipo incompatível na atribuição da variável '"
                        + assignment.getName() + "'. Esperado: "
                        + variableType + ", recebido: " + valueType + "."
                );
            }

        } else if (statement instanceof ExpressionStatement expressionStatement) {

            analyzeExpression(expressionStatement.getExpression());

        } else if (statement instanceof BlockStatement block) {

            symbolTable.enterScope();

            for (Statement child : block.getStatements()) {
                analyzeStatement(child);
            }

            symbolTable.exitScope();

        } else if (statement instanceof IfStatement ifStatement) {

            MiniLuaType conditionType =
                    analyzeExpression(ifStatement.getCondition());

            if (conditionType != MiniLuaType.ERROR
                    && conditionType != MiniLuaType.BOOLEAN) {

                errors.add(
                        "A condição do if deve ser do tipo BOOLEAN."
                );
            }

            analyzeStatement(ifStatement.getThenBranch());

            if (ifStatement.getElseBranch() != null) {
                analyzeStatement(ifStatement.getElseBranch());
            }

        } else if (statement instanceof WhileStatement whileStatement) {

            MiniLuaType conditionType =
                    analyzeExpression(whileStatement.getCondition());

            if (conditionType != MiniLuaType.ERROR
                    && conditionType != MiniLuaType.BOOLEAN) {

                errors.add(
                        "A condição do while deve ser do tipo BOOLEAN."
                );
            }

            analyzeStatement(whileStatement.getBody());
        }
    }

    private MiniLuaType analyzeExpression(Expression expression) {

        if (expression instanceof LiteralExpression literal) {

            if (literal.getValue() instanceof Integer) {
                return MiniLuaType.INTEGER;
            }

            if (literal.getValue() instanceof Boolean) {
                return MiniLuaType.BOOLEAN;
            }

        } else if (expression instanceof VariableExpression variable) {

            MiniLuaType type = symbolTable.lookup(variable.getName());

            if (type == null) {
                errors.add(
                        "Variável '" + variable.getName()
                        + "' não foi declarada."
                );

                return MiniLuaType.ERROR;
            }

            return type;

        } else if (expression instanceof UnaryExpression unary) {

            MiniLuaType operandType =
                    analyzeExpression(unary.getOperand());

            if (operandType == MiniLuaType.ERROR) {
                return MiniLuaType.ERROR;
            }

            if (unary.getOperator() == TokenType.MINUS) {

                if (operandType != MiniLuaType.INTEGER) {
                    errors.add(
                            "O operador '-' exige um operando INTEGER."
                    );

                    return MiniLuaType.ERROR;
                }

                return MiniLuaType.INTEGER;
            } else {
                errors.add(
                        "Operador unário não suportado."
                        );
                
                return MiniLuaType.ERROR;
            }

        } else if (expression instanceof BinaryExpression binary) {

            MiniLuaType leftType =
                    analyzeExpression(binary.getLeft());

            MiniLuaType rightType =
                    analyzeExpression(binary.getRight());

            if (leftType == MiniLuaType.ERROR || rightType == MiniLuaType.ERROR) {
                return MiniLuaType.ERROR;
            }

            TokenType operator = binary.getOperator();

            switch (operator) {

                case PLUS:
                case MINUS:
                case MULTIPLY:
                case DIVIDE:

                    if (leftType != MiniLuaType.INTEGER
                            || rightType != MiniLuaType.INTEGER) {

                        errors.add(
                                "Operadores aritméticos exigem operandos INTEGER."
                        );

                        return MiniLuaType.ERROR;
                    }

                    return MiniLuaType.INTEGER;

                case LESS:
                case GREATER:
                case LESS_EQUAL:
                case GREATER_EQUAL:

                    if (leftType != MiniLuaType.INTEGER
                            || rightType != MiniLuaType.INTEGER) {

                        errors.add(
                                "Operadores relacionais exigem operandos INTEGER."
                        );

                        return MiniLuaType.ERROR;
                    }

                    return MiniLuaType.BOOLEAN;

                case EQUAL_EQUAL:
                case NOT_EQUAL:

                    if (leftType == MiniLuaType.VOID
                            || rightType == MiniLuaType.VOID) {
                        errors.add(
                                "Operadores de igualdade não aceitam expressões VOID."
                        );
                        return MiniLuaType.ERROR;
                    }

                    if (leftType != rightType) {

                        errors.add(
                                "Operadores de igualdade exigem operandos do mesmo tipo."
                        );

                        return MiniLuaType.ERROR;
                    }

                    return MiniLuaType.BOOLEAN;

                default:
                    errors.add("Operador binário não suportado.");
                    return MiniLuaType.ERROR;
            }

        } else if (expression instanceof CallExpression call) {

            if (call.getName().equals("read")) {

                if (!call.getArguments().isEmpty()) {
                    errors.add(
                            "A função read() não aceita argumentos."
                    );

                    return MiniLuaType.ERROR;
                }

                return MiniLuaType.INTEGER;

            } else if (call.getName().equals("print")) {

                if (call.getArguments().size() != 1) {
                    errors.add(
                            "A função print() exige exatamente um argumento."
                    );

                    return MiniLuaType.ERROR;
                }

                MiniLuaType argumentType =
                        analyzeExpression(call.getArguments().get(0));
                
                if (argumentType == MiniLuaType.ERROR){
                    return MiniLuaType.ERROR;
                }

                if (argumentType == MiniLuaType.VOID) {
                    errors.add(
                            "A função print() não aceita outra chamada print() como argumento."
                    );
                    return MiniLuaType.ERROR;
                }

                return MiniLuaType.VOID;

            } else {

                errors.add(
                        "Função '" + call.getName()
                        + "' não foi definida."
                );

                return MiniLuaType.ERROR;
            }
        }

        return MiniLuaType.ERROR;
    }
}
