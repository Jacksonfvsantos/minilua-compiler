
package com.mycompany.minilua.compiler.bytecode;

import com.mycompany.minilua.compiler.intermediate.TACInstruction;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BytecodeGeneratorTest {

    @Test
    public void shouldGenerateArithmeticOperations() {

        List<TACInstruction> tac = List.of(
            new TACInstruction("PLUS", "a", "b", "resultado1"),
            new TACInstruction("MINUS", "a", "b", "resultado2"),
            new TACInstruction("MULTIPLY", "a", "b", "resultado3"),
            new TACInstruction("DIVIDE", "a", "b", "resultado4")
        );

        BytecodeGenerator generator = new BytecodeGenerator();

        BytecodeProgram program = generator.generate(tac);

        List<BytecodeInstruction> instructions =
                program.getInstructions();

        assertEquals(BytecodeOpcode.ADD, instructions.get(0).getOpcode());
        assertEquals(BytecodeOpcode.SUBTRACT, instructions.get(1).getOpcode());
        assertEquals(BytecodeOpcode.MULTIPLY, instructions.get(2).getOpcode());
        assertEquals(BytecodeOpcode.DIVIDE, instructions.get(3).getOpcode());
    }
    
    @Test
    public void shouldGenerateComparisonOperations() {

        List<TACInstruction> tac = List.of(
            new TACInstruction("EQUAL_EQUAL", "a", "b", "r1"),
            new TACInstruction("NOT_EQUAL", "a", "b", "r2"),
            new TACInstruction("LESS", "a", "b", "r3"),
            new TACInstruction("GREATER", "a", "b", "r4"),
            new TACInstruction("LESS_EQUAL", "a", "b", "r5"),
            new TACInstruction("GREATER_EQUAL", "a", "b", "r6")
        );

        BytecodeGenerator generator = new BytecodeGenerator();

        BytecodeProgram program = generator.generate(tac);

        List<BytecodeInstruction> instructions =
                program.getInstructions();

        assertEquals(BytecodeOpcode.EQUAL, instructions.get(0).getOpcode());
        assertEquals(BytecodeOpcode.NOT_EQUAL, instructions.get(1).getOpcode());
        assertEquals(BytecodeOpcode.LESS, instructions.get(2).getOpcode());
        assertEquals(BytecodeOpcode.GREATER, instructions.get(3).getOpcode());
        assertEquals(BytecodeOpcode.LESS_EQUAL, instructions.get(4).getOpcode());
        assertEquals(BytecodeOpcode.GREATER_EQUAL, instructions.get(5).getOpcode());
    }
        
    @Test
    public void shouldGenerateBooleanConstants() {

        List<TACInstruction> tac = List.of(
            new TACInstruction("ASSIGN", "true", null, "ativo"),
            new TACInstruction("ASSIGN", "false", null, "inativo")
        );

        BytecodeGenerator generator = new BytecodeGenerator();

        BytecodeProgram program = generator.generate(tac);

        List<BytecodeInstruction> instructions =
                program.getInstructions();

        assertEquals(BytecodeOpcode.LOAD_CONST, instructions.get(0).getOpcode());
        assertEquals(true, instructions.get(0).getConstant());

        assertEquals(BytecodeOpcode.LOAD_CONST, instructions.get(1).getOpcode());
        assertEquals(false, instructions.get(1).getConstant());
    }
        
    @Test
    public void shouldGenerateInstructionsWithNumericConstants() {

        List<TACInstruction> tac = List.of(
            new TACInstruction("PLUS", "10", "5", "resultado")
        );

        BytecodeGenerator generator = new BytecodeGenerator();

        BytecodeProgram program = generator.generate(tac);

        List<BytecodeInstruction> instructions =
                program.getInstructions();

        assertEquals(BytecodeOpcode.LOAD_CONST, instructions.get(0).getOpcode());
        assertEquals(10, instructions.get(0).getConstant());

        assertEquals(BytecodeOpcode.LOAD_CONST, instructions.get(1).getOpcode());
        assertEquals(5, instructions.get(1).getConstant());

        assertEquals(BytecodeOpcode.ADD, instructions.get(2).getOpcode());
    }
        
    @Test
    public void shouldGenerateNegativeNumericConstant() {

        List<TACInstruction> tac = List.of(
            new TACInstruction("ASSIGN", "-15", null, "valor")
        );

        BytecodeGenerator generator = new BytecodeGenerator();

        BytecodeProgram program = generator.generate(tac);

        List<BytecodeInstruction> instructions =
                program.getInstructions();

        assertEquals(BytecodeOpcode.LOAD_CONST, instructions.get(0).getOpcode());
        assertEquals(-15, instructions.get(0).getConstant());
    }

    @Test
    public void shouldGenerateReadAndPrintInstructions() {

        List<TACInstruction> tac = List.of(
            new TACInstruction("READ", null, null, "entrada"),
            new TACInstruction("PRINT", "entrada", null, null)
        );

        BytecodeGenerator generator = new BytecodeGenerator();

        BytecodeProgram program = generator.generate(tac);

        List<BytecodeInstruction> instructions =
                program.getInstructions();

        assertEquals(BytecodeOpcode.READ, instructions.get(0).getOpcode());
        assertEquals(BytecodeOpcode.PRINT, instructions.get(1).getOpcode());
    }

    @Test
    public void shouldGenerateUnconditionalJump() {

        List<TACInstruction> tac = List.of(
            new TACInstruction("GOTO", null, null, "fim"),
            new TACInstruction("LABEL", null, null, "fim")
        );

        BytecodeGenerator generator = new BytecodeGenerator();

        BytecodeProgram program = generator.generate(tac);

        List<BytecodeInstruction> instructions =
                program.getInstructions();

        assertEquals(BytecodeOpcode.JUMP, instructions.get(0).getOpcode());
        assertEquals(1, instructions.get(0).getJumpTarget());
    }

    @Test
    public void shouldGenerateConditionalJump() {

        List<TACInstruction> tac = List.of(
            new TACInstruction("IF_FALSE", "condicao", null, "fim"),
            new TACInstruction("PRINT", "condicao", null, null),
            new TACInstruction("LABEL", null, null, "fim")
        );

        BytecodeGenerator generator = new BytecodeGenerator();

        BytecodeProgram program = generator.generate(tac);

        List<BytecodeInstruction> instructions =
                program.getInstructions();

        assertEquals(
            BytecodeOpcode.JUMP_IF_FALSE,
            instructions.get(0).getOpcode()
        );

        assertEquals(2, instructions.get(0).getJumpTarget());
    }

    @Test
    public void shouldGenerateNegateInstruction() {

        List<TACInstruction> tac = List.of(
            new TACInstruction("NEG", "valor", null, "resultado")
        );

        BytecodeGenerator generator = new BytecodeGenerator();

        BytecodeProgram program = generator.generate(tac);

        List<BytecodeInstruction> instructions =
                program.getInstructions();

        assertEquals(
            BytecodeOpcode.NEGATE,
            instructions.get(0).getOpcode()
        );
    }

    @Test
    public void shouldThrowExceptionForUnknownOperation() {

        List<TACInstruction> tac = List.of(
            new TACInstruction("UNKNOWN", "a", "b", "resultado")
        );

        BytecodeGenerator generator = new BytecodeGenerator();

        assertThrows(
            IllegalArgumentException.class,
            () -> generator.generate(tac)
        );
    }

    @Test
    public void shouldThrowExceptionForUnknownOperator() {

        List<TACInstruction> tac = List.of(
            new TACInstruction("UNKNOWN_OPERATOR", "a", "b", "resultado")
        );

        BytecodeGenerator generator = new BytecodeGenerator();

        assertThrows(
            IllegalArgumentException.class,
            () -> generator.generate(tac)
        );
    }

    @Test
    public void shouldThrowExceptionForMissingLabelInGoto() {

        List<TACInstruction> tac = List.of(
            new TACInstruction("GOTO", null, null, "rotulo_inexistente")
        );

        BytecodeGenerator generator = new BytecodeGenerator();

        assertThrows(
            IllegalArgumentException.class,
            () -> generator.generate(tac)
        );
    }

    @Test
    public void shouldThrowExceptionForMissingLabelInIfFalse() {

        List<TACInstruction> tac = List.of(
            new TACInstruction("IF_FALSE", "condicao", null, "rotulo_inexistente")
        );

        BytecodeGenerator generator = new BytecodeGenerator();

        assertThrows(
            IllegalArgumentException.class,
            () -> generator.generate(tac)
        );
    }

    @Test
    public void shouldGenerateLabelWithoutInstruction() {

        List<TACInstruction> tac = List.of(
            new TACInstruction("LABEL", null, null, "inicio"),
            new TACInstruction("ASSIGN", "10", null, "valor")
        );

        BytecodeGenerator generator = new BytecodeGenerator();

        BytecodeProgram program = generator.generate(tac);

        List<BytecodeInstruction> instructions =
                program.getInstructions();

        assertEquals(BytecodeOpcode.LOAD_CONST, instructions.get(0).getOpcode());
        assertEquals(10, instructions.get(0).getConstant());
        assertEquals(BytecodeOpcode.HALT, instructions.get(1).getOpcode());
    }

    @Test
    public void shouldResetGeneratorStateBetweenGenerations() {

        BytecodeGenerator generator = new BytecodeGenerator();

        BytecodeProgram firstProgram = generator.generate(
            List.of(
                new TACInstruction("ASSIGN", "10", null, "primeiro")
            )
        );

        BytecodeProgram secondProgram = generator.generate(
            List.of(
                new TACInstruction("ASSIGN", "20", null, "segundo")
            )
        );

        assertEquals(2, firstProgram.getInstructions().size());
        assertEquals(2, secondProgram.getInstructions().size());

        assertEquals(10, firstProgram.getInstructions().get(0).getConstant());
        assertEquals(20, secondProgram.getInstructions().get(0).getConstant());

        assertEquals(0, secondProgram.getSlots().get("segundo"));
    }

    @Test
    public void shouldGenerateNegateWithConstantOperand() {

        List<TACInstruction> tac = List.of(
            new TACInstruction("NEG", "10", null, "resultado")
        );

        BytecodeGenerator generator = new BytecodeGenerator();

        BytecodeProgram program = generator.generate(tac);

        List<BytecodeInstruction> instructions =
                program.getInstructions();

        assertEquals(BytecodeOpcode.LOAD_CONST, instructions.get(0).getOpcode());
        assertEquals(10, instructions.get(0).getConstant());

        assertEquals(BytecodeOpcode.NEGATE, instructions.get(1).getOpcode());
    }

    @Test
    public void shouldGeneratePrintWithConstantOperand() {

        List<TACInstruction> tac = List.of(
            new TACInstruction("PRINT", "42", null, null)
        );

        BytecodeGenerator generator = new BytecodeGenerator();

        BytecodeProgram program = generator.generate(tac);

        List<BytecodeInstruction> instructions =
                program.getInstructions();

        assertEquals(BytecodeOpcode.LOAD_CONST, instructions.get(0).getOpcode());
        assertEquals(42, instructions.get(0).getConstant());

        assertEquals(BytecodeOpcode.PRINT, instructions.get(1).getOpcode());
    }

    @Test
    public void shouldGenerateConditionalJumpWithBooleanConstant() {

        List<TACInstruction> tac = List.of(
            new TACInstruction("IF_FALSE", "false", null, "fim"),
            new TACInstruction("LABEL", null, null, "fim")
        );

        BytecodeGenerator generator = new BytecodeGenerator();

        BytecodeProgram program = generator.generate(tac);

        List<BytecodeInstruction> instructions =
                program.getInstructions();

        assertEquals(BytecodeOpcode.LOAD_CONST, instructions.get(0).getOpcode());
        assertEquals(false, instructions.get(0).getConstant());

        assertEquals(
            BytecodeOpcode.JUMP_IF_FALSE,
            instructions.get(1).getOpcode()
        );

        assertEquals(2, instructions.get(1).getJumpTarget());
    }

    @Test
    public void shouldGenerateOperationWithConstantAsFirstOperand() {

        List<TACInstruction> tac = List.of(
            new TACInstruction("PLUS", "10", "variavel", "resultado")
        );

        BytecodeProgram program = new BytecodeGenerator().generate(tac);

        List<BytecodeInstruction> instructions = program.getInstructions();

        assertEquals(BytecodeOpcode.LOAD_CONST, instructions.get(0).getOpcode());
        assertEquals(10, instructions.get(0).getConstant());
        assertEquals(BytecodeOpcode.ADD, instructions.get(1).getOpcode());
    }

    @Test
    public void shouldGenerateOperationWithConstantAsSecondOperand() {

        List<TACInstruction> tac = List.of(
            new TACInstruction("MULTIPLY", "variavel", "5", "resultado")
        );

        BytecodeProgram program = new BytecodeGenerator().generate(tac);

        List<BytecodeInstruction> instructions = program.getInstructions();

        assertEquals(BytecodeOpcode.LOAD_CONST, instructions.get(0).getOpcode());
        assertEquals(5, instructions.get(0).getConstant());
        assertEquals(BytecodeOpcode.MULTIPLY, instructions.get(1).getOpcode());
    }

    @Test
    public void shouldGenerateComparisonWithTwoConstants() {

        List<TACInstruction> tac = List.of(
            new TACInstruction("LESS", "5", "10", "resultado")
        );

        BytecodeProgram program = new BytecodeGenerator().generate(tac);

        List<BytecodeInstruction> instructions = program.getInstructions();

        assertEquals(BytecodeOpcode.LOAD_CONST, instructions.get(0).getOpcode());
        assertEquals(5, instructions.get(0).getConstant());

        assertEquals(BytecodeOpcode.LOAD_CONST, instructions.get(1).getOpcode());
        assertEquals(10, instructions.get(1).getConstant());

        assertEquals(BytecodeOpcode.LESS, instructions.get(2).getOpcode());
    }

    @Test
    public void shouldGenerateEqualityWithBooleanConstants() {

        List<TACInstruction> tac = List.of(
            new TACInstruction("EQUAL_EQUAL", "true", "false", "resultado")
        );

        BytecodeProgram program = new BytecodeGenerator().generate(tac);

        List<BytecodeInstruction> instructions = program.getInstructions();

        assertEquals(BytecodeOpcode.LOAD_CONST, instructions.get(0).getOpcode());
        assertEquals(true, instructions.get(0).getConstant());

        assertEquals(BytecodeOpcode.LOAD_CONST, instructions.get(1).getOpcode());
        assertEquals(false, instructions.get(1).getConstant());

        assertEquals(BytecodeOpcode.EQUAL, instructions.get(2).getOpcode());
    }

    @Test
    public void shouldGenerateMoveForVariableAssignment() {

        List<TACInstruction> tac = List.of(
            new TACInstruction("ASSIGN", "origem", null, "destino")
        );

        BytecodeProgram program = new BytecodeGenerator().generate(tac);

        List<BytecodeInstruction> instructions = program.getInstructions();

        assertEquals(BytecodeOpcode.MOVE, instructions.get(0).getOpcode());
        assertEquals(1, instructions.get(0).getOperand1());
        assertEquals(0, instructions.get(0).getDestination());
    }

    @Test
    public void shouldUseLastPositionForRepeatedLabel() {

        List<TACInstruction> tac = List.of(
            new TACInstruction("LABEL", null, null, "inicio"),
            new TACInstruction("ASSIGN", "10", null, "valor"),
            new TACInstruction("LABEL", null, null, "inicio"),
            new TACInstruction("GOTO", null, null, "inicio")
        );

        BytecodeProgram program = new BytecodeGenerator().generate(tac);

        List<BytecodeInstruction> instructions = program.getInstructions();

        assertEquals(BytecodeOpcode.JUMP, instructions.get(1).getOpcode());
        assertEquals(1, instructions.get(1).getJumpTarget());
    }

    @Test
    public void shouldGenerateHaltForEmptyTac() {

        BytecodeProgram program = new BytecodeGenerator().generate(List.of());

        List<BytecodeInstruction> instructions = program.getInstructions();

        assertEquals(1, instructions.size());
        assertEquals(BytecodeOpcode.HALT, instructions.get(0).getOpcode());
    }
    @Test
    void shouldThrowExceptionForNullNegateOperand() {

        List<TACInstruction> tac = List.of(
                new TACInstruction("NEG", null, null, "resultado")
        );
        BytecodeGenerator generator = new BytecodeGenerator();
        assertThrows(
                IllegalArgumentException.class,
                () -> generator.generate(tac)
        );
    }
    @Test
    void shouldThrowExceptionForUnknownOpcode() throws Exception {

        BytecodeGenerator generator = new BytecodeGenerator();

        java.lang.reflect.Method method =
                BytecodeGenerator.class.getDeclaredMethod(
                        "getOpcode",
                        String.class
                );

        method.setAccessible(true);

        java.lang.reflect.InvocationTargetException exception =
                assertThrows(
                        java.lang.reflect.InvocationTargetException.class,
                        () -> method.invoke(generator, "UNKNOWN_OPERATOR")
                );

        assertEquals(
                IllegalArgumentException.class,
                exception.getCause().getClass()
        );
    }
}
