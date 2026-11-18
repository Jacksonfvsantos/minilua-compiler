
package com.mycompany.minilua.compiler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class MiniluaCompilerTest {

    @Test
    void shouldExecuteCompleteCompilationPipeline(@TempDir Path tempDir)
            throws IOException {

        Path sourceFile = tempDir.resolve("teste.lua");

        Files.writeString(sourceFile, """
                local numero = 10
                local dobro = numero * 2

                print(numero)
                print(dobro)
                """, StandardCharsets.UTF_8);

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        PrintStream originalOutput = System.out;

        try {
            System.setOut(new PrintStream(
                    buffer, true, StandardCharsets.UTF_8
            ));

            MiniluaCompiler.main(new String[]{sourceFile.toString()});

        } finally {
            System.setOut(originalOutput);
        }

        String output = buffer.toString(StandardCharsets.UTF_8);

        assertTrue(output.contains("LEXER"));
        assertTrue(output.contains("AST"));
        assertTrue(output.contains("ANÁLISE SEMÂNTICA"));
        assertTrue(output.contains("CÓDIGO INTERMEDIÁRIO (TAC)"));
        assertTrue(output.contains("GERAÇÃO DE BYTECODE"));
        assertTrue(output.contains("Compilação concluída."));
        assertTrue(output.contains("EXECUÇÃO DA VM"));

        assertTrue(output.contains("10"));
        assertTrue(output.contains("20"));
    }

    @Test
    void shouldShowUsageWhenNoArgumentsAreProvided() {

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        PrintStream originalOutput = System.out;

        try {
            System.setOut(new PrintStream(
                    buffer, true, StandardCharsets.UTF_8
            ));

            MiniluaCompiler.main(new String[0]);

        } finally {
            System.setOut(originalOutput);
        }

        String output = buffer.toString(StandardCharsets.UTF_8);

        assertTrue(output.contains(
                "Uso: MiniluaCompiler <arquivo.lua>"
        ));

        assertFalse(output.contains("Compilação concluída."));
    }

    @Test
    void shouldRejectMultipleCommandLineArguments() {

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        PrintStream originalOutput = System.out;

        try {
            System.setOut(new PrintStream(
                    buffer, true, StandardCharsets.UTF_8
            ));

            MiniluaCompiler.main(new String[]{
                    "programas/exemplo.lua",
                    "outro.lua"
            });

        } finally {
            System.setOut(originalOutput);
        }

        String output = buffer.toString(StandardCharsets.UTF_8);

        assertTrue(output.contains(
                "Uso: MiniluaCompiler <arquivo.lua>"
        ));

        assertFalse(output.contains("Compilação concluída."));
    }

    @Test
    void shouldInstantiateMiniluaCompiler() {
        MiniluaCompiler compiler = new MiniluaCompiler();

        assertNotNull(compiler);
    }

    @Test
    void shouldStopCompilationWhenSemanticErrorsAreFound() {

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        PrintStream originalOutput = System.out;

        try {
            System.setOut(new PrintStream(
                    buffer, true, StandardCharsets.UTF_8
            ));

            MiniluaCompiler.compile("""
                    local idade = 20
                    local idade = 30
                    """);

        } finally {
            System.setOut(originalOutput);
        }

        String output = buffer.toString(StandardCharsets.UTF_8);

        assertTrue(output.contains("Erros semânticos encontrados:"));
        assertTrue(output.contains("já foi declarada neste escopo."));
        assertFalse(output.contains("CÓDIGO INTERMEDIÁRIO (TAC)"));
    }

    @Test
    void shouldHandleRuntimeErrorDuringExecution() {

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        PrintStream originalOutput = System.out;

        try {
            System.setOut(new PrintStream(
                    buffer, true, StandardCharsets.UTF_8
            ));

            MiniluaCompiler.compile("""
                    local resultado = 10 / 0
                    print(resultado)
                    """);

        } finally {
            System.setOut(originalOutput);
        }

        String output = buffer.toString(StandardCharsets.UTF_8);

        assertTrue(output.contains("Compilação concluída."));
        assertTrue(output.contains("EXECUÇÃO DA VM"));
        assertTrue(output.contains("Erro durante a execução:"));
        assertTrue(output.contains("Divisão por zero."));
    }

    @Test
    void shouldHandleCompilationError() {

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        PrintStream originalOutput = System.out;

        try {
            System.setOut(new PrintStream(
                    buffer, true, StandardCharsets.UTF_8
            ));

            MiniluaCompiler.compile("""
                    local = 10
                    """);

        } finally {
            System.setOut(originalOutput);
        }

        String output = buffer.toString(StandardCharsets.UTF_8);

        assertTrue(output.contains("Erro durante a compilação:"));
        assertTrue(output.contains("Erro sintático"));
        assertFalse(output.contains("Compilação concluída."));
    }
}
