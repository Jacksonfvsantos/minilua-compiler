
package com.mycompany.minilua.compiler.intermediate;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TACPrinterTest {

    private String captureOutput(List<TACInstruction> instructions) {

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        PrintStream originalOutput = System.out;

        try {
            System.setOut(new PrintStream(buffer));

            TACPrinter printer = new TACPrinter();
            printer.print(instructions);

        } finally {
            System.setOut(originalOutput);
        }

        return buffer.toString();
    }

    @Test
    public void shouldPrintAssignmentInstruction() {

        TACInstruction instruction = new TACInstruction(
                "ASSIGN", "10", null, "idade"
        );

        String output = captureOutput(List.of(instruction));

        assertEquals("idade = 10" + System.lineSeparator(), output);
    }

    @Test
    public void shouldPrintNegationInstruction() {

        TACInstruction instruction = new TACInstruction(
                "NEG", "10", null, "$t1"
        );

        String output = captureOutput(List.of(instruction));

        assertEquals("$t1 = -10" + System.lineSeparator(), output);
    }

    @Test
    public void shouldPrintReadInstruction() {

        TACInstruction instruction = new TACInstruction(
                "READ", null, null, "$t1"
        );

        String output = captureOutput(List.of(instruction));

        assertEquals("$t1 = read" + System.lineSeparator(), output);
    }

    @Test
    public void shouldPrintPrintInstruction() {

        TACInstruction instruction = new TACInstruction(
                "PRINT", "idade", null, null
        );

        String output = captureOutput(List.of(instruction));

        assertEquals("print idade" + System.lineSeparator(), output);
    }

    @Test
    public void shouldPrintConditionalAndJumpInstructions() {

        List<TACInstruction> instructions = List.of(
                new TACInstruction("IF_FALSE", "$t1", null, "L1"),
                new TACInstruction("GOTO", null, null, "L2")
        );

        String output = captureOutput(instructions);

        String expected =
                "ifFalse $t1 goto L1" + System.lineSeparator()
                + "goto L2" + System.lineSeparator();

        assertEquals(expected, output);
    }
    
    @Test
    public void shouldPrintLabelInstruction() {

        TACInstruction instruction = new TACInstruction(
                "LABEL", null, null, "L1"
        );

        String output = captureOutput(List.of(instruction));

        assertEquals("L1:" + System.lineSeparator(), output);
    }

    @Test
    public void shouldPrintAdditionInstruction() {

        TACInstruction instruction = new TACInstruction(
                "PLUS", "$t1", "$t2", "$t3"
        );

        String output = captureOutput(List.of(instruction));

        assertEquals("$t3 = $t1 + $t2" + System.lineSeparator(), output);
    }

    @Test
    public void shouldPrintSubtractionInstruction() {

        TACInstruction instruction = new TACInstruction(
                "MINUS", "$t1", "$t2", "$t3"
        );

        String output = captureOutput(List.of(instruction));

        assertEquals("$t3 = $t1 - $t2" + System.lineSeparator(), output);
    }

    @Test
    public void shouldPrintMultiplicationInstruction() {

        TACInstruction instruction = new TACInstruction(
                "MULTIPLY", "$t1", "$t2", "$t3"
        );

        String output = captureOutput(List.of(instruction));

        assertEquals("$t3 = $t1 * $t2" + System.lineSeparator(), output);
    }

    @Test
    public void shouldPrintDivisionInstruction() {

        TACInstruction instruction = new TACInstruction(
                "DIVIDE", "$t1", "$t2", "$t3"
        );

        String output = captureOutput(List.of(instruction));

        assertEquals("$t3 = $t1 / $t2" + System.lineSeparator(), output);
    }

    @Test
    public void shouldPrintEqualityComparisonInstruction() {

        TACInstruction instruction = new TACInstruction(
                "EQUAL_EQUAL", "$t1", "$t2", "$t3"
        );

        String output = captureOutput(List.of(instruction));

        assertEquals("$t3 = $t1 == $t2" + System.lineSeparator(), output);
    }

    @Test
    public void shouldPrintNotEqualComparisonInstruction() {

        TACInstruction instruction = new TACInstruction(
                "NOT_EQUAL", "$t1", "$t2", "$t3"
        );

        String output = captureOutput(List.of(instruction));

        assertEquals("$t3 = $t1 ~= $t2" + System.lineSeparator(), output);
    }

    @Test
    public void shouldPrintLessThanInstruction() {

        TACInstruction instruction = new TACInstruction(
                "LESS", "$t1", "$t2", "$t3"
        );

        String output = captureOutput(List.of(instruction));

        assertEquals("$t3 = $t1 < $t2" + System.lineSeparator(), output);
    }

    @Test
    public void shouldPrintGreaterThanInstruction() {

        TACInstruction instruction = new TACInstruction(
                "GREATER", "$t1", "$t2", "$t3"
        );

        String output = captureOutput(List.of(instruction));

        assertEquals("$t3 = $t1 > $t2" + System.lineSeparator(), output);
    }

    @Test
    public void shouldPrintLessOrEqualInstruction() {

        TACInstruction instruction = new TACInstruction(
                "LESS_EQUAL", "$t1", "$t2", "$t3"
        );

        String output = captureOutput(List.of(instruction));

        assertEquals("$t3 = $t1 <= $t2" + System.lineSeparator(), output);
    }

    @Test
    public void shouldPrintGreaterOrEqualInstruction() {

        TACInstruction instruction = new TACInstruction(
                "GREATER_EQUAL", "$t1", "$t2", "$t3"
        );

        String output = captureOutput(List.of(instruction));

        assertEquals("$t3 = $t1 >= $t2" + System.lineSeparator(), output);
    }

    @Test
    public void shouldPrintUnknownOperatorInstruction() {

        TACInstruction instruction = new TACInstruction(
                "UNKNOWN", "$t1", "$t2", "$t3"
        );

        String output = captureOutput(List.of(instruction));

        assertEquals("$t3 = $t1 UNKNOWN $t2" + System.lineSeparator(), output);
    }

}