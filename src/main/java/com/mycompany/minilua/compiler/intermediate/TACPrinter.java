package com.mycompany.minilua.compiler.intermediate;

import java.util.List;

public class TACPrinter {

    public void print(List<TACInstruction> instructions) {

        for (TACInstruction instruction : instructions) {

            String operation = instruction.getOperation();
            String arg1 = instruction.getArg1();
            String arg2 = instruction.getArg2();
            String result = instruction.getResult();

            switch (operation) {

                case "ASSIGN":
                    System.out.println(result + " = " + arg1);
                    break;

                case "NEG":
                    System.out.println(result + " = -" + arg1);
                    break;

                case "READ":
                    System.out.println(result + " = read");
                    break;

                case "PRINT":
                    System.out.println("print " + arg1);
                    break;

                case "IF_FALSE":
                    System.out.println(
                            "ifFalse " + arg1 + " goto " + result
                    );
                    break;

                case "GOTO":
                    System.out.println("goto " + result);
                    break;

                case "LABEL":
                    System.out.println(result + ":");
                    break;

                default:
                    System.out.println(
                            result + " = " + arg1 + " "
                            + getOperator(operation) + " " + arg2
                    );
                    break;
            }
        }
    }

    private String getOperator(String operation) {

        switch (operation) {

            case "PLUS":
                return "+";

            case "MINUS":
                return "-";

            case "MULTIPLY":
                return "*";

            case "DIVIDE":
                return "/";

            case "EQUAL_EQUAL":
                return "==";

            case "NOT_EQUAL":
                return "~=";

            case "LESS":
                return "<";

            case "GREATER":
                return ">";

            case "LESS_EQUAL":
                return "<=";

            case "GREATER_EQUAL":
                return ">=";

            default:
                return operation;
        }
    }
}
