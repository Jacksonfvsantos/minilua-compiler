
package com.mycompany.minilua.compiler.bytecode;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BytecodePrinterTest {

    private String captureOutput(BytecodeProgram program) {

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        PrintStream originalOutput = System.out;

        try {
            System.setOut(new PrintStream(buffer));

            BytecodePrinter printer = new BytecodePrinter();
            printer.print(program);

        } finally {
            System.setOut(originalOutput);
        }

        return buffer.toString();
    }

    @Test
    public void shouldPrintLoadConstantInstruction() {

        List<BytecodeInstruction> instructions = List.of(
                new BytecodeInstruction(
                        BytecodeOpcode.LOAD_CONST,
                        0, -1, -1, 10, -1
                )
        );

        BytecodeProgram program = new BytecodeProgram(
                instructions,
                Map.of("idade", 0)
        );

        String output = captureOutput(program);

        assertEquals(
                "0: LOAD_CONST idade, 10" + System.lineSeparator(),
                output
        );
    }

    @Test
    public void shouldPrintMoveInstruction() {

        List<BytecodeInstruction> instructions = List.of(
                new BytecodeInstruction(
                        BytecodeOpcode.MOVE,
                        0, 1, -1, null, -1
                )
        );

        BytecodeProgram program = new BytecodeProgram(
                instructions,
                Map.of("destino", 0, "origem", 1)
        );

        String output = captureOutput(program);

        assertEquals(
                "0: MOVE destino, origem" + System.lineSeparator(),
                output
        );
    }

    @Test
    public void shouldPrintNegateInstruction() {

        List<BytecodeInstruction> instructions = List.of(
                new BytecodeInstruction(
                        BytecodeOpcode.NEGATE,
                        1, 0, -1, null, -1
                )
        );

        BytecodeProgram program = new BytecodeProgram(
                instructions,
                Map.of("valor", 0, "resultado", 1)
        );

        String output = captureOutput(program);

        assertEquals(
                "0: NEGATE resultado, valor" + System.lineSeparator(),
                output
        );
    }

    @Test
    public void shouldPrintReadInstruction() {

        List<BytecodeInstruction> instructions = List.of(
                new BytecodeInstruction(
                        BytecodeOpcode.READ,
                        0, -1, -1, null, -1
                )
        );

        BytecodeProgram program = new BytecodeProgram(
                instructions,
                Map.of("entrada", 0)
        );

        String output = captureOutput(program);

        assertEquals(
                "0: READ entrada" + System.lineSeparator(),
                output
        );
    }

    @Test
    public void shouldPrintPrintInstruction() {

        List<BytecodeInstruction> instructions = List.of(
                new BytecodeInstruction(
                        BytecodeOpcode.PRINT,
                        -1, 0, -1, null, -1
                )
        );

        BytecodeProgram program = new BytecodeProgram(
                instructions,
                Map.of("mensagem", 0)
        );

        String output = captureOutput(program);

        assertEquals(
                "0: PRINT mensagem" + System.lineSeparator(),
                output
        );
    }
    
    @Test
    public void shouldPrintAdditionInstruction() {

        BytecodeInstruction instruction = new BytecodeInstruction(
                BytecodeOpcode.ADD, 2, 0, 1, null, -1
        );

        BytecodeProgram program = new BytecodeProgram(
                List.of(instruction),
                Map.of("a", 0, "b", 1, "resultado", 2)
        );

        String output = captureOutput(program);

        assertEquals(
                "0: ADD resultado, a, b" + System.lineSeparator(),
                output
        );
    }

    @Test
    public void shouldPrintSubtractionInstruction() {

        BytecodeInstruction instruction = new BytecodeInstruction(
                BytecodeOpcode.SUBTRACT, 2, 0, 1, null, -1
        );

        BytecodeProgram program = new BytecodeProgram(
                List.of(instruction),
                Map.of("a", 0, "b", 1, "resultado", 2)
        );

        String output = captureOutput(program);

        assertEquals(
                "0: SUBTRACT resultado, a, b" + System.lineSeparator(),
                output
        );
    }

    @Test
    public void shouldPrintMultiplicationInstruction() {

        BytecodeInstruction instruction = new BytecodeInstruction(
                BytecodeOpcode.MULTIPLY, 2, 0, 1, null, -1
        );

        BytecodeProgram program = new BytecodeProgram(
                List.of(instruction),
                Map.of("a", 0, "b", 1, "resultado", 2)
        );

        String output = captureOutput(program);

        assertEquals(
                "0: MULTIPLY resultado, a, b" + System.lineSeparator(),
                output
        );
    }

    @Test
    public void shouldPrintDivisionInstruction() {

        BytecodeInstruction instruction = new BytecodeInstruction(
                BytecodeOpcode.DIVIDE, 2, 0, 1, null, -1
        );

        BytecodeProgram program = new BytecodeProgram(
                List.of(instruction),
                Map.of("a", 0, "b", 1, "resultado", 2)
        );

        String output = captureOutput(program);

        assertEquals(
                "0: DIVIDE resultado, a, b" + System.lineSeparator(),
                output
        );
    }

    @Test
    public void shouldPrintEqualityInstruction() {

        BytecodeInstruction instruction = new BytecodeInstruction(
                BytecodeOpcode.EQUAL, 2, 0, 1, null, -1
        );

        BytecodeProgram program = new BytecodeProgram(
                List.of(instruction),
                Map.of("a", 0, "b", 1, "resultado", 2)
        );

        String output = captureOutput(program);

        assertEquals(
                "0: EQUAL resultado, a, b" + System.lineSeparator(),
                output
        );
    }
    
    @Test
    public void shouldPrintNotEqualInstruction() {

        BytecodeInstruction instruction = new BytecodeInstruction(
                BytecodeOpcode.NOT_EQUAL, 2, 0, 1, null, -1
        );

        BytecodeProgram program = new BytecodeProgram(
                List.of(instruction),
                Map.of("a", 0, "b", 1, "resultado", 2)
        );

        String output = captureOutput(program);

        assertEquals(
                "0: NOT_EQUAL resultado, a, b" + System.lineSeparator(),
                output
        );
    }

    @Test
    public void shouldPrintLessInstruction() {

        BytecodeInstruction instruction = new BytecodeInstruction(
                BytecodeOpcode.LESS, 2, 0, 1, null, -1
        );

        BytecodeProgram program = new BytecodeProgram(
                List.of(instruction),
                Map.of("a", 0, "b", 1, "resultado", 2)
        );

        String output = captureOutput(program);

        assertEquals(
                "0: LESS resultado, a, b" + System.lineSeparator(),
                output
        );
    }

    @Test
    public void shouldPrintGreaterInstruction() {

        BytecodeInstruction instruction = new BytecodeInstruction(
                BytecodeOpcode.GREATER, 2, 0, 1, null, -1
        );

        BytecodeProgram program = new BytecodeProgram(
                List.of(instruction),
                Map.of("a", 0, "b", 1, "resultado", 2)
        );

        String output = captureOutput(program);

        assertEquals(
                "0: GREATER resultado, a, b" + System.lineSeparator(),
                output
        );
    }

    @Test
    public void shouldPrintLessEqualInstruction() {

        BytecodeInstruction instruction = new BytecodeInstruction(
                BytecodeOpcode.LESS_EQUAL, 2, 0, 1, null, -1
        );

        BytecodeProgram program = new BytecodeProgram(
                List.of(instruction),
                Map.of("a", 0, "b", 1, "resultado", 2)
        );

        String output = captureOutput(program);

        assertEquals(
                "0: LESS_EQUAL resultado, a, b" + System.lineSeparator(),
                output
        );
    }

    @Test
    public void shouldPrintGreaterEqualInstruction() {

        BytecodeInstruction instruction = new BytecodeInstruction(
                BytecodeOpcode.GREATER_EQUAL, 2, 0, 1, null, -1
        );

        BytecodeProgram program = new BytecodeProgram(
                List.of(instruction),
                Map.of("a", 0, "b", 1, "resultado", 2)
        );

        String output = captureOutput(program);

        assertEquals(
                "0: GREATER_EQUAL resultado, a, b" + System.lineSeparator(),
                output
        );
    }

    @Test
    public void shouldPrintJumpInstruction() {

        BytecodeInstruction instruction = new BytecodeInstruction(
                BytecodeOpcode.JUMP, -1, -1, -1, null, 5
        );

        BytecodeProgram program = new BytecodeProgram(
                List.of(instruction),
                Map.of()
        );

        String output = captureOutput(program);

        assertEquals(
                "0: JUMP 5" + System.lineSeparator(),
                output
        );
    }

    @Test
    public void shouldPrintJumpIfFalseInstruction() {

        BytecodeInstruction instruction = new BytecodeInstruction(
                BytecodeOpcode.JUMP_IF_FALSE, -1, 0, -1, null, 5
        );

        BytecodeProgram program = new BytecodeProgram(
                List.of(instruction),
                Map.of("condicao", 0)
        );

        String output = captureOutput(program);

        assertEquals(
                "0: JUMP_IF_FALSE condicao, 5" + System.lineSeparator(),
                output
        );
    }

    @Test
    public void shouldPrintHaltInstruction() {

        BytecodeInstruction instruction = new BytecodeInstruction(
                BytecodeOpcode.HALT, -1, -1, -1, null, -1
        );

        BytecodeProgram program = new BytecodeProgram(
                List.of(instruction),
                Map.of()
        );

        String output = captureOutput(program);

        assertEquals(
                "0: HALT" + System.lineSeparator(),
                output
        );
    }

    @Test
    public void shouldPrintEmptyProgramWithoutOutput() {

        BytecodeProgram program = new BytecodeProgram(
                List.of(),
                Map.of()
        );

        String output = captureOutput(program);

        assertEquals("", output);
    }

    @Test
    public void shouldPrintSequentialInstructionIndexes() {

        List<BytecodeInstruction> instructions = List.of(
                new BytecodeInstruction(
                        BytecodeOpcode.LOAD_CONST,
                        0, -1, -1, 10, -1
                ),
                new BytecodeInstruction(
                        BytecodeOpcode.PRINT,
                        -1, 0, -1, null, -1
                ),
                new BytecodeInstruction(
                        BytecodeOpcode.HALT,
                        -1, -1, -1, null, -1
                )
        );

        BytecodeProgram program = new BytecodeProgram(
                instructions,
                Map.of("valor", 0)
        );

        String output = captureOutput(program);

        String expected =
                "0: LOAD_CONST valor, 10" + System.lineSeparator()
                + "1: PRINT valor" + System.lineSeparator()
                + "2: HALT" + System.lineSeparator();

        assertEquals(expected, output);
    }
}
