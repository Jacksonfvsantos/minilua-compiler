package com.mycompany.minilua.compiler.semantic;

import java.util.*;

public class SymbolTable {

    private final Deque<Map<String, MiniLuaType>> scopes =
            new ArrayDeque<>();

    public void enterScope() {
        scopes.push(new HashMap<>());
    }

    public void exitScope() {
        if (!scopes.isEmpty()) {
            scopes.pop();
        }
    }

    public boolean declare(String name, MiniLuaType type) {

        if (scopes.isEmpty()) {
            enterScope();
        }

        Map<String, MiniLuaType> currentScope = scopes.peek();

        if (currentScope.containsKey(name)) {
            return false;
        }

        currentScope.put(name, type);

        return true;
    }

    public MiniLuaType lookup(String name) {

        for (Map<String, MiniLuaType> scope : scopes) {

            if (scope.containsKey(name)) {
                return scope.get(name);
            }
        }

        return null;
    }

    public boolean isDeclared(String name) {
        return lookup(name) != null;
    }
}
