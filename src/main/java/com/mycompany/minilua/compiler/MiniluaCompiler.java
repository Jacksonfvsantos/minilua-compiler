
package com.mycompany.minilua.compiler;

import com.mycompany.minilua.compiler.lexer.Lexer;
import com.mycompany.minilua.compiler.lexer.Token;

import com.mycompany.minilua.compiler.parser.Parser;

import com.mycompany.minilua.compiler.ast.statement.Statement;
import com.mycompany.minilua.compiler.ast.printer.ASTPrinter;

import com.mycompany.minilua.compiler.semantic.SemanticAnalyzer;

import com.mycompany.minilua.compiler.intermediate.TACGenerator;
import com.mycompany.minilua.compiler.intermediate.TACInstruction;
import com.mycompany.minilua.compiler.intermediate.TACPrinter;

import com.mycompany.minilua.compiler.bytecode.BytecodeGenerator;
import com.mycompany.minilua.compiler.bytecode.BytecodeProgram;
import com.mycompany.minilua.compiler.bytecode.BytecodePrinter;
import com.mycompany.minilua.compiler.bytecode.VirtualMachine;

import java.util.List;
import java.util.Scanner;
import java.io.PrintStream;
import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class MiniluaCompiler {
    
    public static void main(String[] args) {
    System.setOut(new PrintStream(
        System.out,
        true,
        StandardCharsets.UTF_8
    ));
    if (args.length != 1) {
        System.out.println("Uso: MiniluaCompiler <arquivo.lua>");
        return;
    }

    try {

        Path caminho = Path.of(args[0]);

        String source = Files.readString(caminho, StandardCharsets.UTF_8);

        compile(source);

    } catch (IOException exception) {

        System.out.println("\nErro ao ler o arquivo:");
        System.out.println(exception.getMessage());

    }
}
    static void compile(String source){
        try {

            System.out.println("========== LEXER ==========");

            Lexer lexer = new Lexer(source);
            List<Token> tokens = lexer.scanTokens();

            for (Token token : tokens) {
                System.out.println(token);
            }

            System.out.println("\n========== AST ==========");

            Parser parser = new Parser(tokens);
            List<Statement> statements = parser.parse();

            ASTPrinter astPrinter = new ASTPrinter();
            System.out.println(astPrinter.print(statements));

            System.out.println("\n========== ANÁLISE SEMÂNTICA ==========");

            SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer();

            List<String> errors = semanticAnalyzer.analyze(statements);

            if (!errors.isEmpty()) {

                System.out.println("Erros semânticos encontrados:");

                for (String error : errors) {
                    System.out.println("- " + error);
                }

                return;
            }

            System.out.println("Análise semântica concluída sem erros.");

            System.out.println("\n========== CÓDIGO INTERMEDIÁRIO (TAC) ==========");

            TACGenerator tacGenerator = new TACGenerator();

            List<TACInstruction> tacInstructions =
                    tacGenerator.generate(statements);

            TACPrinter tacPrinter = new TACPrinter();
            tacPrinter.print(tacInstructions);

            System.out.println("\n========== GERAÇÃO DE BYTECODE ==========");

            BytecodeGenerator bytecodeGenerator = new BytecodeGenerator();

            BytecodeProgram bytecodeProgram =
                    bytecodeGenerator.generate(tacInstructions);

            BytecodePrinter bytecodePrinter = new BytecodePrinter();
            bytecodePrinter.print(bytecodeProgram);

            System.out.println("\nCompilação concluída.");

            System.out.println("\n========== EXECUÇÃO DA VM ==========");

            try {

                ByteArrayOutputStream buffer = new ByteArrayOutputStream();

                PrintStream output = new PrintStream(buffer);
                Scanner scanner = new Scanner(System.in);

                VirtualMachine vm = new VirtualMachine(
                    bytecodeProgram,
                    scanner,
                    output
                );

                vm.execute();

                System.out.println(buffer.toString(StandardCharsets.UTF_8));

            } catch (RuntimeException exception) {

                System.out.println("\nErro durante a execução:");
                System.out.println(exception.getMessage());

            }

        } catch (RuntimeException exception) {

            System.out.println("\nErro durante a compilação:");
            System.out.println(exception.getMessage());

        }
    }
}
