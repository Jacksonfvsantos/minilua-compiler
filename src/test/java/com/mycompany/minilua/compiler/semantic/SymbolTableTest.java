
package com.mycompany.minilua.compiler.semantic;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SymbolTableTest {

    @Test
    void shouldDeclareAndFindVariable() {

        SymbolTable symbolTable = new SymbolTable();

        symbolTable.enterScope();

        boolean declared = symbolTable.declare(
                "idade",
                MiniLuaType.INTEGER
        );

        assertTrue(declared);
        assertEquals(
                MiniLuaType.INTEGER,
                symbolTable.lookup("idade")
        );
    }

    @Test
    void shouldRejectDuplicateVariableInSameScope() {

        SymbolTable symbolTable = new SymbolTable();

        symbolTable.enterScope();

        symbolTable.declare(
                "idade",
                MiniLuaType.INTEGER
        );

        boolean declared = symbolTable.declare(
                "idade",
                MiniLuaType.BOOLEAN
        );

        assertFalse(declared);
        assertEquals(
                MiniLuaType.INTEGER,
                symbolTable.lookup("idade")
        );
    }

    @Test
    void shouldAllowSameVariableNameInDifferentScopes() {

        SymbolTable symbolTable = new SymbolTable();

        symbolTable.enterScope();

        symbolTable.declare(
                "valor",
                MiniLuaType.INTEGER
        );

        symbolTable.enterScope();

        boolean declared = symbolTable.declare(
                "valor",
                MiniLuaType.BOOLEAN
        );

        assertTrue(declared);
        assertEquals(
                MiniLuaType.BOOLEAN,
                symbolTable.lookup("valor")
        );
    }

    @Test
    void shouldFindVariableInOuterScope() {

        SymbolTable symbolTable = new SymbolTable();

        symbolTable.enterScope();

        symbolTable.declare(
                "idade",
                MiniLuaType.INTEGER
        );

        symbolTable.enterScope();

        assertEquals(
                MiniLuaType.INTEGER,
                symbolTable.lookup("idade")
        );
    }

    @Test
    void shouldReturnNullForUndeclaredVariable() {

        SymbolTable symbolTable = new SymbolTable();

        symbolTable.enterScope();

        assertNull(
                symbolTable.lookup("inexistente")
        );
    }
        
    @Test
    void shouldRemoveVariableWhenExitingScope() {
        SymbolTable symbolTable = new SymbolTable();
        symbolTable.enterScope();
        symbolTable.declare("idade", MiniLuaType.INTEGER);

        symbolTable.exitScope();

        assertNull(symbolTable.lookup("idade"));
    }

    @Test
    void shouldKeepOuterVariableAfterExitingInnerScope() {
        SymbolTable symbolTable = new SymbolTable();
        symbolTable.enterScope();
        symbolTable.declare("idade", MiniLuaType.INTEGER);

        symbolTable.enterScope();
        symbolTable.declare("idade", MiniLuaType.BOOLEAN);

        symbolTable.exitScope();

        assertEquals(MiniLuaType.INTEGER, symbolTable.lookup("idade"));
    }

    @Test
    void shouldCreateScopeWhenDeclaringWithoutEnteringScope() {
        SymbolTable symbolTable = new SymbolTable();

        boolean declared = symbolTable.declare("idade", MiniLuaType.INTEGER);

        assertTrue(declared);
        assertEquals(MiniLuaType.INTEGER, symbolTable.lookup("idade"));
    }

    @Test
    void shouldIdentifyDeclaredAndUndeclaredVariables() {
        SymbolTable symbolTable = new SymbolTable();
        symbolTable.enterScope();
        symbolTable.declare("idade", MiniLuaType.INTEGER);

        assertTrue(symbolTable.isDeclared("idade"));
        assertFalse(symbolTable.isDeclared("inexistente"));
    }

    @Test
    void shouldAllowExitingWhenNoScopeExists() {
        SymbolTable symbolTable = new SymbolTable();

        assertDoesNotThrow(symbolTable::exitScope);
    }

}
