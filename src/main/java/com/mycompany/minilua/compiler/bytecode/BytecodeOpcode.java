
package com.mycompany.minilua.compiler.bytecode;

public enum BytecodeOpcode {

    LOAD_CONST,
    MOVE,

    ADD,
    SUBTRACT,
    MULTIPLY,
    DIVIDE,
    NEGATE,

    EQUAL,
    NOT_EQUAL,
    LESS,
    GREATER,
    LESS_EQUAL,
    GREATER_EQUAL,

    JUMP,
    JUMP_IF_FALSE,

    READ,
    PRINT,

    HALT
}
