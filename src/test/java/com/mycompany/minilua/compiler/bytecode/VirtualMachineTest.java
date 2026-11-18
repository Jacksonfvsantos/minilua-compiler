
package com.mycompany.minilua.compiler.bytecode;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class VirtualMachineTest {

    private String executeAndCaptureOutput(
            List<BytecodeInstruction> instructions,
            Map<String, Integer> slots) {

        BytecodeProgram program = new BytecodeProgram(instructions, slots);

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        PrintStream output = new PrintStream(buffer);

        Scanner scanner = new Scanner("");

        VirtualMachine vm = new VirtualMachine(program, scanner, output);
        vm.execute();

        return buffer.toString();
    }

    @Test
    public void shouldExecuteAdditionAndPrintResult() {

        List<BytecodeInstruction> instructions = List.of(
            new BytecodeInstruction(BytecodeOpcode.LOAD_CONST, 0, -1, -1, 10, -1),
            new BytecodeInstruction(BytecodeOpcode.LOAD_CONST, 1, -1, -1, 5, -1),
            new BytecodeInstruction(BytecodeOpcode.ADD, 2, 0, 1, null, -1),
            new BytecodeInstruction(BytecodeOpcode.PRINT, -1, 2, -1, null, -1),
            new BytecodeInstruction(BytecodeOpcode.HALT, -1, -1, -1, null, -1)
        );

        String result = executeAndCaptureOutput(
            instructions,
            Map.of("const10", 0, "const5", 1, "resultado", 2)
        );

        assertEquals("15" + System.lineSeparator(), result);
    }

    @Test
    public void shouldExecuteSubtractionAndPrintResult() {

        List<BytecodeInstruction> instructions = List.of(
            new BytecodeInstruction(BytecodeOpcode.LOAD_CONST, 0, -1, -1, 10, -1),
            new BytecodeInstruction(BytecodeOpcode.LOAD_CONST, 1, -1, -1, 5, -1),
            new BytecodeInstruction(BytecodeOpcode.SUBTRACT, 2, 0, 1, null, -1),
            new BytecodeInstruction(BytecodeOpcode.PRINT, -1, 2, -1, null, -1),
            new BytecodeInstruction(BytecodeOpcode.HALT, -1, -1, -1, null, -1)
        );

        String result = executeAndCaptureOutput(
            instructions,
            Map.of("const10", 0, "const5", 1, "resultado", 2)
        );

        assertEquals("5" + System.lineSeparator(), result);
    }

    @Test
    public void shouldExecuteMultiplicationAndPrintResult() {

        List<BytecodeInstruction> instructions = List.of(
            new BytecodeInstruction(BytecodeOpcode.LOAD_CONST, 0, -1, -1, 10, -1),
            new BytecodeInstruction(BytecodeOpcode.LOAD_CONST, 1, -1, -1, 5, -1),
            new BytecodeInstruction(BytecodeOpcode.MULTIPLY, 2, 0, 1, null, -1),
            new BytecodeInstruction(BytecodeOpcode.PRINT, -1, 2, -1, null, -1),
            new BytecodeInstruction(BytecodeOpcode.HALT, -1, -1, -1, null, -1)
        );

        String result = executeAndCaptureOutput(
            instructions,
            Map.of("const10", 0, "const5", 1, "resultado", 2)
        );

        assertEquals("50" + System.lineSeparator(), result);
    }

    @Test
    public void shouldExecuteDivisionAndPrintResult() {

        List<BytecodeInstruction> instructions = List.of(
            new BytecodeInstruction(BytecodeOpcode.LOAD_CONST, 0, -1, -1, 10, -1),
            new BytecodeInstruction(BytecodeOpcode.LOAD_CONST, 1, -1, -1, 5, -1),
            new BytecodeInstruction(BytecodeOpcode.DIVIDE, 2, 0, 1, null, -1),
            new BytecodeInstruction(BytecodeOpcode.PRINT, -1, 2, -1, null, -1),
            new BytecodeInstruction(BytecodeOpcode.HALT, -1, -1, -1, null, -1)
        );

        String result = executeAndCaptureOutput(
            instructions,
            Map.of("const10", 0, "const5", 1, "resultado", 2)
        );

        assertEquals("2" + System.lineSeparator(), result);
    }
    
    
    @Test
    public void shouldExecuteEqualityAndPrintResult() {

        List<BytecodeInstruction> instructions = List.of(
            new BytecodeInstruction(BytecodeOpcode.LOAD_CONST, 0, -1, -1, 10, -1),
            new BytecodeInstruction(BytecodeOpcode.LOAD_CONST, 1, -1, -1, 10, -1),
            new BytecodeInstruction(BytecodeOpcode.EQUAL, 2, 0, 1, null, -1),
            new BytecodeInstruction(BytecodeOpcode.PRINT, -1, 2, -1, null, -1),
            new BytecodeInstruction(BytecodeOpcode.HALT, -1, -1, -1, null, -1)
        );

        String result = executeAndCaptureOutput(
            instructions,
            Map.of("valor1", 0, "valor2", 1, "resultado", 2)
        );

        assertEquals("true" + System.lineSeparator(), result);
    }

    @Test
    public void shouldExecuteNotEqualAndPrintResult() {

        List<BytecodeInstruction> instructions = List.of(
            new BytecodeInstruction(BytecodeOpcode.LOAD_CONST, 0, -1, -1, 10, -1),
            new BytecodeInstruction(BytecodeOpcode.LOAD_CONST, 1, -1, -1, 5, -1),
            new BytecodeInstruction(BytecodeOpcode.NOT_EQUAL, 2, 0, 1, null, -1),
            new BytecodeInstruction(BytecodeOpcode.PRINT, -1, 2, -1, null, -1),
            new BytecodeInstruction(BytecodeOpcode.HALT, -1, -1, -1, null, -1)
        );

        String result = executeAndCaptureOutput(
            instructions,
            Map.of("valor1", 0, "valor2", 1, "resultado", 2)
        );

        assertEquals("true" + System.lineSeparator(), result);
    }

    @Test
    public void shouldExecuteLessAndPrintResult() {

        List<BytecodeInstruction> instructions = List.of(
            new BytecodeInstruction(BytecodeOpcode.LOAD_CONST, 0, -1, -1, 5, -1),
            new BytecodeInstruction(BytecodeOpcode.LOAD_CONST, 1, -1, -1, 10, -1),
            new BytecodeInstruction(BytecodeOpcode.LESS, 2, 0, 1, null, -1),
            new BytecodeInstruction(BytecodeOpcode.PRINT, -1, 2, -1, null, -1),
            new BytecodeInstruction(BytecodeOpcode.HALT, -1, -1, -1, null, -1)
        );

        String result = executeAndCaptureOutput(
            instructions,
            Map.of("valor1", 0, "valor2", 1, "resultado", 2)
        );

        assertEquals("true" + System.lineSeparator(), result);
    }

    @Test
    public void shouldExecuteGreaterAndPrintResult() {

        List<BytecodeInstruction> instructions = List.of(
            new BytecodeInstruction(BytecodeOpcode.LOAD_CONST, 0, -1, -1, 10, -1),
            new BytecodeInstruction(BytecodeOpcode.LOAD_CONST, 1, -1, -1, 5, -1),
            new BytecodeInstruction(BytecodeOpcode.GREATER, 2, 0, 1, null, -1),
            new BytecodeInstruction(BytecodeOpcode.PRINT, -1, 2, -1, null, -1),
            new BytecodeInstruction(BytecodeOpcode.HALT, -1, -1, -1, null, -1)
        );

        String result = executeAndCaptureOutput(
            instructions,
            Map.of("valor1", 0, "valor2", 1, "resultado", 2)
        );

        assertEquals("true" + System.lineSeparator(), result);
    }

    @Test
    public void shouldExecuteLessEqualAndPrintResult() {

        List<BytecodeInstruction> instructions = List.of(
            new BytecodeInstruction(BytecodeOpcode.LOAD_CONST, 0, -1, -1, 10, -1),
            new BytecodeInstruction(BytecodeOpcode.LOAD_CONST, 1, -1, -1, 10, -1),
            new BytecodeInstruction(BytecodeOpcode.LESS_EQUAL, 2, 0, 1, null, -1),
            new BytecodeInstruction(BytecodeOpcode.PRINT, -1, 2, -1, null, -1),
            new BytecodeInstruction(BytecodeOpcode.HALT, -1, -1, -1, null, -1)
        );

        String result = executeAndCaptureOutput(
            instructions,
            Map.of("valor1", 0, "valor2", 1, "resultado", 2)
        );

        assertEquals("true" + System.lineSeparator(), result);
    }

    @Test
    public void shouldExecuteGreaterEqualAndPrintResult() {

        List<BytecodeInstruction> instructions = List.of(
            new BytecodeInstruction(BytecodeOpcode.LOAD_CONST, 0, -1, -1, 10, -1),
            new BytecodeInstruction(BytecodeOpcode.LOAD_CONST, 1, -1, -1, 10, -1),
            new BytecodeInstruction(BytecodeOpcode.GREATER_EQUAL, 2, 0, 1, null, -1),
            new BytecodeInstruction(BytecodeOpcode.PRINT, -1, 2, -1, null, -1),
            new BytecodeInstruction(BytecodeOpcode.HALT, -1, -1, -1, null, -1)
        );

        String result = executeAndCaptureOutput(
            instructions,
            Map.of("valor1", 0, "valor2", 1, "resultado", 2)
        );

        assertEquals("true" + System.lineSeparator(), result);
    }
    
    
    @Test
    public void shouldExecuteNegateAndPrintResult() {

        List<BytecodeInstruction> instructions = List.of(
            new BytecodeInstruction(BytecodeOpcode.LOAD_CONST, 0, -1, -1, 10, -1),
            new BytecodeInstruction(BytecodeOpcode.NEGATE, 1, 0, -1, null, -1),
            new BytecodeInstruction(BytecodeOpcode.PRINT, -1, 1, -1, null, -1),
            new BytecodeInstruction(BytecodeOpcode.HALT, -1, -1, -1, null, -1)
        );

        String result = executeAndCaptureOutput(
            instructions,
            Map.of("valor", 0, "resultado", 1)
        );

        assertEquals("-10" + System.lineSeparator(), result);
    }

    
    @Test
    public void shouldExecuteJumpAndSkipInstruction() {

        List<BytecodeInstruction> instructions = List.of(
            new BytecodeInstruction(BytecodeOpcode.LOAD_CONST, 0, -1, -1, 10, -1),
            new BytecodeInstruction(BytecodeOpcode.JUMP, -1, -1, -1, null, 3),
            new BytecodeInstruction(BytecodeOpcode.PRINT, -1, 0, -1, null, -1),
            new BytecodeInstruction(BytecodeOpcode.LOAD_CONST, 1, -1, -1, 20, -1),
            new BytecodeInstruction(BytecodeOpcode.PRINT, -1, 1, -1, null, -1),
            new BytecodeInstruction(BytecodeOpcode.HALT, -1, -1, -1, null, -1)
        );

        String result = executeAndCaptureOutput(
            instructions,
            Map.of("valor1", 0, "valor2", 1)
        );

        assertEquals("20" + System.lineSeparator(), result);
    }
    
    
    @Test
    public void shouldExecuteJumpIfFalseAndSkipInstructions() {

        List<BytecodeInstruction> instructions = List.of(
            new BytecodeInstruction(BytecodeOpcode.LOAD_CONST, 0, -1, -1, false, -1),
            new BytecodeInstruction(BytecodeOpcode.JUMP_IF_FALSE, -1, 0, -1, null, 4),
            new BytecodeInstruction(BytecodeOpcode.LOAD_CONST, 1, -1, -1, 10, -1),
            new BytecodeInstruction(BytecodeOpcode.PRINT, -1, 1, -1, null, -1),
            new BytecodeInstruction(BytecodeOpcode.LOAD_CONST, 2, -1, -1, 20, -1),
            new BytecodeInstruction(BytecodeOpcode.PRINT, -1, 2, -1, null, -1),
            new BytecodeInstruction(BytecodeOpcode.HALT, -1, -1, -1, null, -1)
        );

        String result = executeAndCaptureOutput(
            instructions,
            Map.of("condicao", 0, "valor1", 1, "valor2", 2)
        );

        assertEquals("20" + System.lineSeparator(), result);
    }
    
    
    @Test
    public void shouldExecuteReadAndPrintResult() {

        List<BytecodeInstruction> instructions = List.of(
            new BytecodeInstruction(BytecodeOpcode.READ, 0, -1, -1, null, -1),
            new BytecodeInstruction(BytecodeOpcode.PRINT, -1, 0, -1, null, -1),
            new BytecodeInstruction(BytecodeOpcode.HALT, -1, -1, -1, null, -1)
        );

        BytecodeProgram program = new BytecodeProgram(
            instructions,
            Map.of("entrada", 0)
        );

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        PrintStream output = new PrintStream(buffer);

        Scanner scanner = new Scanner("42");

        VirtualMachine vm = new VirtualMachine(program, scanner, output);
        vm.execute();

        assertEquals(
            "Digite um valor inteiro: 42" + System.lineSeparator(),
            buffer.toString()
        );
    }

    
    @Test
    public void shouldExecuteMoveAndPrintResult() {

        List<BytecodeInstruction> instructions = List.of(
            new BytecodeInstruction(BytecodeOpcode.LOAD_CONST, 0, -1, -1, 100, -1),
            new BytecodeInstruction(BytecodeOpcode.MOVE, 1, 0, -1, null, -1),
            new BytecodeInstruction(BytecodeOpcode.PRINT, -1, 1, -1, null, -1),
            new BytecodeInstruction(BytecodeOpcode.HALT, -1, -1, -1, null, -1)
        );

        String result = executeAndCaptureOutput(
            instructions,
            Map.of("original", 0, "copia", 1)
        );

        assertEquals("100" + System.lineSeparator(), result);
    }
    
    
    @Test
    public void shouldThrowExceptionWhenDividingByZero() {

        List<BytecodeInstruction> instructions = List.of(
            new BytecodeInstruction(BytecodeOpcode.LOAD_CONST, 0, -1, -1, 10, -1),
            new BytecodeInstruction(BytecodeOpcode.LOAD_CONST, 1, -1, -1, 0, -1),
            new BytecodeInstruction(BytecodeOpcode.DIVIDE, 2, 0, 1, null, -1),
            new BytecodeInstruction(BytecodeOpcode.HALT, -1, -1, -1, null, -1)
        );

        BytecodeProgram program = new BytecodeProgram(
            instructions,
            Map.of("dividendo", 0, "divisor", 1, "resultado", 2)
        );

        VirtualMachine vm = new VirtualMachine(
            program,
            new Scanner(""),
            new PrintStream(new ByteArrayOutputStream())
        );

        assertThrows(ArithmeticException.class, vm::execute);
    }
    
    
    @Test
    public void shouldThrowExceptionWhenReadingInvalidInput() {

        List<BytecodeInstruction> instructions = List.of(
            new BytecodeInstruction(BytecodeOpcode.READ, 0, -1, -1, null, -1),
            new BytecodeInstruction(BytecodeOpcode.HALT, -1, -1, -1, null, -1)
        );

        BytecodeProgram program = new BytecodeProgram(
            instructions,
            Map.of("entrada", 0)
        );

        VirtualMachine vm = new VirtualMachine(
            program,
            new Scanner("abc"),
            new PrintStream(new ByteArrayOutputStream())
        );

        assertThrows(IllegalArgumentException.class, vm::execute);
    }

    
    @Test
    public void shouldThrowExceptionWhenUsingInvalidMemorySlot() {

        List<BytecodeInstruction> instructions = List.of(
            new BytecodeInstruction(BytecodeOpcode.LOAD_CONST, 5, -1, -1, 100, -1),
            new BytecodeInstruction(BytecodeOpcode.HALT, -1, -1, -1, null, -1)
        );

        BytecodeProgram program = new BytecodeProgram(
            instructions,
            Map.of("variavel", 0)
        );

        VirtualMachine vm = new VirtualMachine(
            program,
            new Scanner(""),
            new PrintStream(new ByteArrayOutputStream())
        );

        assertThrows(IllegalStateException.class, vm::execute);
    }
    
    @Test
    public void shouldExecuteUsingDefaultConstructor() {

        List<BytecodeInstruction> instructions = List.of(
            new BytecodeInstruction(
                BytecodeOpcode.HALT, -1, -1, -1, null, -1
            )
        );

        BytecodeProgram program = new BytecodeProgram(
            instructions,
            Map.of()
        );

        VirtualMachine vm = new VirtualMachine(program);

        vm.execute();
    }
    
    @Test
    public void shouldThrowExceptionWhenNoInputIsAvailable() {

        List<BytecodeInstruction> instructions = List.of(
            new BytecodeInstruction(
                BytecodeOpcode.READ, 0, -1, -1, null, -1
            ),
            new BytecodeInstruction(
                BytecodeOpcode.HALT, -1, -1, -1, null, -1
            )
        );

        BytecodeProgram program = new BytecodeProgram(
            instructions,
            Map.of("entrada", 0)
        );

        VirtualMachine vm = new VirtualMachine(
            program,
            new Scanner(""),
            new PrintStream(new ByteArrayOutputStream())
        );

        assertThrows(IllegalArgumentException.class, vm::execute);
    }
    
    @Test
    public void shouldThrowExceptionWhenIntegerOperationReceivesBoolean() {

        List<BytecodeInstruction> instructions = List.of(
            new BytecodeInstruction(
                BytecodeOpcode.LOAD_CONST, 0, -1, -1, true, -1
            ),
            new BytecodeInstruction(
                BytecodeOpcode.LOAD_CONST, 1, -1, -1, 10, -1
            ),
            new BytecodeInstruction(
                BytecodeOpcode.ADD, 2, 0, 1, null, -1
            ),
            new BytecodeInstruction(
                BytecodeOpcode.HALT, -1, -1, -1, null, -1
            )
        );

        BytecodeProgram program = new BytecodeProgram(
            instructions,
            Map.of("booleano", 0, "inteiro", 1, "resultado", 2)
        );

        VirtualMachine vm = new VirtualMachine(
            program,
            new Scanner(""),
            new PrintStream(new ByteArrayOutputStream())
        );

        assertThrows(IllegalStateException.class, vm::execute);
    }
    
    @Test
    public void shouldThrowExceptionWhenConditionalReceivesInteger() {

        List<BytecodeInstruction> instructions = List.of(
            new BytecodeInstruction(
                BytecodeOpcode.LOAD_CONST, 0, -1, -1, 10, -1
            ),
            new BytecodeInstruction(
                BytecodeOpcode.JUMP_IF_FALSE, -1, 0, -1, null, 2
            ),
            new BytecodeInstruction(
                BytecodeOpcode.HALT, -1, -1, -1, null, -1
            )
        );

        BytecodeProgram program = new BytecodeProgram(
            instructions,
            Map.of("condicao", 0)
        );

        VirtualMachine vm = new VirtualMachine(
            program,
            new Scanner(""),
            new PrintStream(new ByteArrayOutputStream())
        );

        assertThrows(IllegalStateException.class, vm::execute);
    }
    
    @Test
    public void shouldContinueExecutionWhenJumpConditionIsTrue() {

        List<BytecodeInstruction> instructions = List.of(
            new BytecodeInstruction(
                BytecodeOpcode.LOAD_CONST, 0, -1, -1, true, -1
            ),
            new BytecodeInstruction(
                BytecodeOpcode.JUMP_IF_FALSE, -1, 0, -1, null, 4
            ),
            new BytecodeInstruction(
                BytecodeOpcode.LOAD_CONST, 1, -1, -1, 10, -1
            ),
            new BytecodeInstruction(
                BytecodeOpcode.PRINT, -1, 1, -1, null, -1
            ),
            new BytecodeInstruction(
                BytecodeOpcode.HALT, -1, -1, -1, null, -1
            )
        );

        String result = executeAndCaptureOutput(
            instructions,
            Map.of("condicao", 0, "valor", 1)
        );

        assertEquals("10" + System.lineSeparator(), result);
    }
    
    @Test
    public void shouldThrowExceptionWhenJumpTargetExceedsProgramBounds() {

        List<BytecodeInstruction> instructions = List.of(
            new BytecodeInstruction(
                BytecodeOpcode.JUMP, -1, -1, -1, null, 2
            ),
            new BytecodeInstruction(
                BytecodeOpcode.HALT, -1, -1, -1, null, -1
            )
        );

        BytecodeProgram program = new BytecodeProgram(
            instructions,
            Map.of()
        );

        VirtualMachine vm = new VirtualMachine(
            program,
            new Scanner(""),
            new PrintStream(new ByteArrayOutputStream())
        );

        assertThrows(IllegalStateException.class, vm::execute);
    }
    
    @Test
    public void shouldThrowExceptionWhenJumpTargetIsNegative() {

        List<BytecodeInstruction> instructions = List.of(
            new BytecodeInstruction(
                BytecodeOpcode.JUMP, -1, -1, -1, null, -1
            ),
            new BytecodeInstruction(
                BytecodeOpcode.HALT, -1, -1, -1, null, -1
            )
        );

        BytecodeProgram program = new BytecodeProgram(
            instructions,
            Map.of()
        );

        VirtualMachine vm = new VirtualMachine(
            program,
            new Scanner(""),
            new PrintStream(new ByteArrayOutputStream())
        );

        assertThrows(IllegalStateException.class, vm::execute);
    }
    
    @Test
    public void shouldThrowExceptionWhenScannerIsNull() {

        BytecodeProgram program = new BytecodeProgram(
            List.of(
                new BytecodeInstruction(
                    BytecodeOpcode.HALT, -1, -1, -1, null, -1
                )
            ),
            Map.of()
        );

        assertThrows(
            NullPointerException.class,
            () -> new VirtualMachine(
                program,
                null,
                new PrintStream(new ByteArrayOutputStream())
            )
        );
    }
    
    @Test
    public void shouldThrowExceptionWhenOutputIsNull() {

        BytecodeProgram program = new BytecodeProgram(
            List.of(
                new BytecodeInstruction(
                    BytecodeOpcode.HALT, -1, -1, -1, null, -1
                )
            ),
            Map.of()
        );

        assertThrows(
            NullPointerException.class,
            () -> new VirtualMachine(
                program,
                new Scanner(""),
                null
            )
        );
    }
    
    @Test
    public void shouldThrowExceptionWhenUsingNegativeMemorySlot() {

        List<BytecodeInstruction> instructions = List.of(
            new BytecodeInstruction(
                BytecodeOpcode.LOAD_CONST, -1, -1, -1, 100, -1
            ),
            new BytecodeInstruction(
                BytecodeOpcode.HALT, -1, -1, -1, null, -1
            )
        );

        BytecodeProgram program = new BytecodeProgram(
            instructions,
            Map.of("variavel", 0)
        );

        VirtualMachine vm = new VirtualMachine(
            program,
            new Scanner(""),
            new PrintStream(new ByteArrayOutputStream())
        );

        assertThrows(IllegalStateException.class, vm::execute);
    }
    @Test
    public void shouldExecuteNotEqualWithEqualValuesAndPrintFalse() {

        List<BytecodeInstruction> instructions = List.of(
            new BytecodeInstruction(BytecodeOpcode.LOAD_CONST, 0, -1, -1, 10, -1),
            new BytecodeInstruction(BytecodeOpcode.LOAD_CONST, 1, -1, -1, 10, -1),
            new BytecodeInstruction(BytecodeOpcode.NOT_EQUAL, 2, 0, 1, null, -1),
            new BytecodeInstruction(BytecodeOpcode.PRINT, -1, 2, -1, null, -1),
            new BytecodeInstruction(BytecodeOpcode.HALT, -1, -1, -1, null, -1)
        );

        String result = executeAndCaptureOutput(
            instructions,
            Map.of("valor1", 0, "valor2", 1, "resultado", 2)
        );

        assertEquals("false" + System.lineSeparator(), result);
    }
    @Test
    public void shouldExecuteLessEqualWithGreaterValueAndPrintFalse() {

        List<BytecodeInstruction> instructions = List.of(
            new BytecodeInstruction(BytecodeOpcode.LOAD_CONST, 0, -1, -1, 10, -1),
            new BytecodeInstruction(BytecodeOpcode.LOAD_CONST, 1, -1, -1, 5, -1),
            new BytecodeInstruction(BytecodeOpcode.LESS_EQUAL, 2, 0, 1, null, -1),
            new BytecodeInstruction(BytecodeOpcode.PRINT, -1, 2, -1, null, -1),
            new BytecodeInstruction(BytecodeOpcode.HALT, -1, -1, -1, null, -1)
        );

        String result = executeAndCaptureOutput(
            instructions,
            Map.of("valor1", 0, "valor2", 1, "resultado", 2)
        );

        assertEquals("false" + System.lineSeparator(), result);
    }
}
