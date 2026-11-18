
package com.mycompany.minilua.compiler.bytecode;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collections;

public class BytecodeProgram {

    private final List<BytecodeInstruction> instructions;
    private final Map<String, Integer> slots;

    public BytecodeProgram(
            List<BytecodeInstruction> instructions,
            Map<String, Integer> slots) {

        this.instructions = new ArrayList<>(instructions);
        this.slots = new HashMap<>(slots);
    }

    public List<BytecodeInstruction> getInstructions() {
        return Collections.unmodifiableList(instructions);
    }

    public Map<String, Integer> getSlots() {
        return Collections.unmodifiableMap(slots);
    }
}
