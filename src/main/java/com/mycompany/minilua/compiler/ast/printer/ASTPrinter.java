package com.mycompany.minilua.compiler.ast.printer;

import com.mycompany.minilua.compiler.ast.expression.*;
import com.mycompany.minilua.compiler.ast.statement.*;

import java.util.List;

public class ASTPrinter {

    private final StringBuilder builder = new StringBuilder();
    private int indentation = 0;

    public String print(List<Statement> statements) {

        builder.setLength(0);
        indentation = 0;

        builder.append("Program\n");

        for (Statement statement : statements) {
            printStatement(statement);
        }

        return builder.toString();
    }

    private void printStatement(Statement statement) {

        if (statement instanceof VariableDeclarationStatement declaration) {

            appendIndentation();
            builder.append("VariableDeclaration: ")
                    .append(declaration.getName())
                    .append("\n");

            indentation++;
            printExpression(declaration.getInitializer());
            indentation--;

        } else if (statement instanceof AssignmentStatement assignment) {

            appendIndentation();
            builder.append("Assignment: ")
                    .append(assignment.getName())
                    .append("\n");

            indentation++;
            printExpression(assignment.getValue());
            indentation--;

        } else if (statement instanceof ExpressionStatement expressionStatement) {

            appendIndentation();
            builder.append("ExpressionStatement\n");

            indentation++;
            printExpression(expressionStatement.getExpression());
            indentation--;

        } else if (statement instanceof BlockStatement block) {

            appendIndentation();
            builder.append("BlockStatement\n");

            indentation++;

            for (Statement child : block.getStatements()) {
                printStatement(child);
            }

            indentation--;

        } else if (statement instanceof IfStatement ifStatement) {

            appendIndentation();
            builder.append("IfStatement\n");

            indentation++;

            appendIndentation();
            builder.append("Condition\n");

            indentation++;
            printExpression(ifStatement.getCondition());
            indentation--;

            appendIndentation();
            builder.append("ThenBranch\n");

            indentation++;
            printStatement(ifStatement.getThenBranch());
            indentation--;

            if (ifStatement.getElseBranch() != null) {

                appendIndentation();
                builder.append("ElseBranch\n");

                indentation++;
                printStatement(ifStatement.getElseBranch());
                indentation--;
            }

            indentation--;


        } else if (statement instanceof WhileStatement whileStatement) {

            appendIndentation();
            builder.append("WhileStatement\n");

            indentation++;

            appendIndentation();
            builder.append("Condition\n");

            indentation++;
            printExpression(whileStatement.getCondition());
            indentation--;

            appendIndentation();
            builder.append("Body\n");

            indentation++;
            printStatement(whileStatement.getBody());
            indentation--;

            indentation--;

        } else {

            appendIndentation();
            builder.append("UnknownStatement\n");
        }
    }

    private void printExpression(Expression expression) {

        if (expression instanceof LiteralExpression literal) {

            appendIndentation();
            builder.append("Literal: ")
                    .append(literal.getValue())
                    .append("\n");

        } else if (expression instanceof VariableExpression variable) {

            appendIndentation();
            builder.append("Variable: ")
                    .append(variable.getName())
                    .append("\n");

        } else if (expression instanceof BinaryExpression binary) {

            appendIndentation();
            builder.append("BinaryExpression: ")
                    .append(binary.getOperator())
                    .append("\n");

            indentation++;
            printExpression(binary.getLeft());
            printExpression(binary.getRight());
            indentation--;

        } else if (expression instanceof UnaryExpression unary) {

            appendIndentation();
            builder.append("UnaryExpression: ")
                    .append(unary.getOperator())
                    .append("\n");

            indentation++;
            printExpression(unary.getOperand());
            indentation--;

        } else if (expression instanceof CallExpression call) {

            appendIndentation();
            builder.append("CallExpression: ")
                    .append(call.getName())
                    .append("\n");

            indentation++;

            for (Expression argument : call.getArguments()) {
                printExpression(argument);
            }

            indentation--;

        } else {

            appendIndentation();
            builder.append("UnknownExpression\n");
        }
    }

    private void appendIndentation() {

        for (int i = 0; i < indentation; i++) {
            builder.append("  ");
        }
    }
}
