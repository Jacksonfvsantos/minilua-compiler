package com.mycompany.minilua.compiler.intermediate;

import com.mycompany.minilua.compiler.ast.expression.*;
import com.mycompany.minilua.compiler.ast.statement.*;

import com.mycompany.minilua.compiler.lexer.TokenType;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TACGenerator {

    private final List<TACInstruction> instructions = new ArrayList<>();

    private final Deque<Map<String, String>> variableScopes = new ArrayDeque<>();
    private final Map<String, Integer> variableCounters = new HashMap<>();

    private int temporaryCounter = 0;
    private int labelCounter = 0;

    public List<TACInstruction> generate(List<Statement> statements) {

        instructions.clear();
        temporaryCounter = 0;
        labelCounter = 0;
        variableCounters.clear();
        variableScopes.clear();
        variableScopes.push(new HashMap<>());

        for (Statement statement : statements) {
            generateStatement(statement);
        }

        return new ArrayList<>(instructions);
    }

    private void generateStatement(Statement statement) {

        if (statement instanceof VariableDeclarationStatement declaration) {

            String value = generateExpression(declaration.getInitializer());

            String variable = declareVariable(declaration.getName());
            emit("ASSIGN", value, null, variable);

        } else if (statement instanceof AssignmentStatement assignment) {

            String value = generateExpression(assignment.getValue());

            emit("ASSIGN", value, null, resolveVariable(assignment.getName()));

        } else if (statement instanceof ExpressionStatement expressionStatement) {

            generateExpression(expressionStatement.getExpression());

        } else if (statement instanceof BlockStatement block) {

            variableScopes.push(new HashMap<>());

            for (Statement child : block.getStatements()) {
                generateStatement(child);
            }

            variableScopes.pop();

        } else if (statement instanceof IfStatement ifStatement) {

            String condition = generateExpression(ifStatement.getCondition());

            String elseLabel = newLabel();
            String endLabel = newLabel();

            emit("IF_FALSE", condition, null, elseLabel);

            generateStatement(ifStatement.getThenBranch());

            if (ifStatement.getElseBranch() != null) {

                emit("GOTO", null, null, endLabel);

                emit("LABEL", null, null, elseLabel);

                generateStatement(ifStatement.getElseBranch());

                emit("LABEL", null, null, endLabel);

            } else {

                emit("LABEL", null, null, elseLabel);
            }

        } else if (statement instanceof WhileStatement whileStatement) {

            String startLabel = newLabel();
            String endLabel = newLabel();

            emit("LABEL", null, null, startLabel);

            String condition = generateExpression(whileStatement.getCondition());

            emit("IF_FALSE", condition, null, endLabel);

            generateStatement(whileStatement.getBody());

            emit("GOTO", null, null, startLabel);

            emit("LABEL", null, null, endLabel);
        }
    }

    private String generateExpression(Expression expression) {

        if (expression instanceof LiteralExpression literal) {

            return String.valueOf(literal.getValue());

        } else if (expression instanceof VariableExpression variable) {

            return resolveVariable(variable.getName());

        } else if (expression instanceof UnaryExpression unary) {

            String operand = generateExpression(unary.getOperand());

            String temporary = newTemporary();

            emit(
                    "NEG",
                    operand,
                    null,
                    temporary
            );

            return temporary;

        } else if (expression instanceof BinaryExpression binary) {

            String left = generateExpression(binary.getLeft());
            String right = generateExpression(binary.getRight());

            String temporary = newTemporary();

            emit(
                    binary.getOperator().name(),
                    left,
                    right,
                    temporary
            );

            return temporary;

        } else if (expression instanceof CallExpression call) {

            if (call.getName().equals("read")) {

                String temporary = newTemporary();

                emit("READ", null, null, temporary);

                return temporary;

            } else if (call.getName().equals("print")) {

                String argument = generateExpression(
                        call.getArguments().get(0)
                );

                emit("PRINT", argument, null, null);

                return null;
            }
        }

        throw new IllegalArgumentException(
                "Expressão não suportada pelo gerador TAC."
        );
    }

    private void emit(
            String operation,
            String arg1,
            String arg2,
            String result) {

        instructions.add(
                new TACInstruction(operation, arg1, arg2, result)
        );
    }

    private String newTemporary() {
        temporaryCounter++;
        return "$t" + temporaryCounter;
    }

    private String declareVariable(String name) {
        int version = variableCounters.getOrDefault(name, 0);
        variableCounters.put(name, version + 1);

        String internalName = version == 0 ? name : name + "$" + version;
        variableScopes.peek().put(name, internalName);
        return internalName;
    }

    private String resolveVariable(String name) {
        for (Map<String, String> scope : variableScopes) {
            String internalName = scope.get(name);
            if (internalName != null) {
                return internalName;
            }
        }

        throw new IllegalStateException(
                "Variável '" + name + "' não encontrada durante a geração TAC."
        );
    }

    private String newLabel() {
        labelCounter++;
        return "L" + labelCounter;
    }
}
