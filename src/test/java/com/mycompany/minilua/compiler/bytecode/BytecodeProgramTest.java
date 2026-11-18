
package com.mycompany.minilua.compiler.bytecode;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BytecodeProgramTest {

    @Test
    void shouldCreateDefensiveCopyOfInstructions() {

        List<BytecodeInstruction> instructions = new ArrayList<>();

        instructions.add(new BytecodeInstruction(
                BytecodeOpcode.LOAD_CONST,
                0, 0, 0, 10, -1
        ));

        BytecodeProgram program = new BytecodeProgram(
                instructions,
                new HashMap<>()
        );

        instructions.add(new BytecodeInstruction(
                BytecodeOpcode.HALT,
                -1, -1, -1, null, -1
        ));

        assertEquals(1, program.getInstructions().size());
    }

    @Test
    void shouldCreateDefensiveCopyOfSlots() {

        Map<String, Integer> slots = new HashMap<>();

        slots.put("numero", 0);

        BytecodeProgram program = new BytecodeProgram(
                new ArrayList<>(),
                slots
        );

        slots.put("dobro", 1);

        assertEquals(1, program.getSlots().size());
        assertFalse(program.getSlots().containsKey("dobro"));
    }

    @Test
    void shouldNotAllowModifyingInstructionsThroughGetter() {

        BytecodeProgram program = new BytecodeProgram(
                new ArrayList<>(),
                new HashMap<>()
        );

        assertThrows(
                UnsupportedOperationException.class,
                () -> program.getInstructions().add(
                        new BytecodeInstruction(
                                BytecodeOpcode.HALT,
                                -1, -1, -1, null, -1
                        )
                )
        );
    }

    @Test
    void shouldNotAllowModifyingSlotsThroughGetter() {

        Map<String, Integer> slots = new HashMap<>();
        slots.put("numero", 0);

        BytecodeProgram program = new BytecodeProgram(
                new ArrayList<>(),
                slots
        );

        assertThrows(
                UnsupportedOperationException.class,
                () -> program.getSlots().put("dobro", 1)
        );
    }
}
