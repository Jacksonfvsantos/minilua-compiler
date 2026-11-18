
package com.mycompany.minilua.compiler;

import com.mycompany.minilua.compiler.lexer.Lexer;
import com.mycompany.minilua.compiler.lexer.Token;
import com.mycompany.minilua.compiler.parser.Parser;
import com.mycompany.minilua.compiler.ast.statement.Statement;
import com.mycompany.minilua.compiler.semantic.SemanticAnalyzer;
import com.mycompany.minilua.compiler.intermediate.TACGenerator;
import com.mycompany.minilua.compiler.intermediate.TACInstruction;
import com.mycompany.minilua.compiler.bytecode.BytecodeGenerator;
import com.mycompany.minilua.compiler.bytecode.BytecodeProgram;
import com.mycompany.minilua.compiler.bytecode.VirtualMachine;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CompilerIntegrationTest {

    private String compileAndExecute(String source, String input) {

        // Análise léxica
        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();

        // Análise sintática
        Parser parser = new Parser(tokens);
        List<Statement> statements = parser.parse();

        // Análise semântica
        SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer();

        List<String> errors = semanticAnalyzer.analyze(statements);

        if(!errors.isEmpty()){
            throw new IllegalStateException(
                    "Erros semânticos encontrados: "+errors
            );
        }

        // Geração do código intermediário
        TACGenerator tacGenerator = new TACGenerator();

        List<TACInstruction> tacInstructions =
                tacGenerator.generate(statements);

        // Geração do bytecode
        BytecodeGenerator bytecodeGenerator = new BytecodeGenerator();

        BytecodeProgram bytecodeProgram =
                bytecodeGenerator.generate(tacInstructions);

        // Execução da máquina virtual
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        PrintStream output = new PrintStream(buffer);
        Scanner scanner = new Scanner(input);

        VirtualMachine vm = new VirtualMachine(
                bytecodeProgram,
                scanner,
                output
        );

        vm.execute();

        return buffer.toString();
    }

    @Test
    public void shouldCompileAndExecuteSimpleProgram() {

        String source = """
                local resultado = 10 + 5
                print(resultado)
                """;

        String actualOutput = compileAndExecute(source, "");

        assertEquals(
                "15" + System.lineSeparator(),
                actualOutput
        );
    }

    @Test
    public void shouldCompileAndExecuteConditionalAndLoop() {

        String source = """
                local contador = 0

                while contador < 5 do
                    if contador >= 2 then
                        print(contador)
                    else
                        print(0)
                    end

                    contador = contador + 1
                end
                """;

        String expectedOutput = String.join(
                System.lineSeparator(),
                "0",
                "0",
                "2",
                "3",
                "4"
        ) + System.lineSeparator();

        String actualOutput = compileAndExecute(source, "");

        assertEquals(expectedOutput, actualOutput);
    }
    
    
    @Test
    public void shouldCompileAndExecuteProgramWithInput() {

        String source = """
                local numero = read()
                local resultado = numero * 2
                print(resultado)
                """;

        String actualOutput = compileAndExecute(
                source,
                "21"
        );

        assertEquals(
                "Digite um valor inteiro: 42" + System.lineSeparator(),
                actualOutput
        );
    }
    
    @Test
    public void shouldThrowExceptionWhenDividingByZero() {

        String source = """
                local resultado = 10 / 0
                print(resultado)
                """;

        assertThrows(
                ArithmeticException.class,
                () -> compileAndExecute(source, "")
        );
    }
    
    @Test
    public void shouldRejectUndeclaredVariable() {

        String source = """
                local resultado = total + 5
                print(resultado)
                """;

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> compileAndExecute(source, "")
        );

        assertTrue(
                exception.getMessage().contains(
                        "Variável 'total' não foi declarada."
                )
        );
    }
    
    @Test
    public void shouldRejectIncompatibleAssignment() {

        String source = """
                local numero = 10
                numero = true
                print(numero)
                """;

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> compileAndExecute(source, "")
        );

        assertTrue(
                exception.getMessage().contains(
                        "Tipo incompatível na atribuição da variável 'numero'."
                )
        );
    }
    
    @Test
    public void shouldCompileAndExecuteUnaryExpression() {

        String source = """
                local numero = 5
                local resultado = -numero
                print(resultado)
                """;

        String actualOutput = compileAndExecute(source, "");

        assertEquals(
                "-5" + System.lineSeparator(),
                actualOutput
        );
    }
    @Test
    public void shouldCompileAndExecuteIfWithoutElse() {

        String source = """
                local numero = 10

                if numero > 20 then
                    print(99)
                end

                print(numero)
                """;

        String actualOutput = compileAndExecute(source, "");

        assertEquals(
                "10" + System.lineSeparator(),
                actualOutput
        );
    }
    @Test
    public void shouldCompileAndExecuteVariableShadowing() {

        String source = """
                local valor = 10

                if true then
                    local valor = 20
                    print(valor)
                end

                print(valor)
                """;

        String actualOutput = compileAndExecute(source, "");

        String expectedOutput = String.join(
                System.lineSeparator(),
                "20",
                "10"
        ) + System.lineSeparator();

        assertEquals(expectedOutput, actualOutput);
    }
    @Test
    public void shouldCompileAndExecuteAllComparisonOperators() {

        String source = """
                local a = 10
                local b = 20

                if a < b then
                    print(1)
                else
                    print(0)
                end

                if b > a then
                    print(2)
                else
                    print(0)
                end

                if a <= 10 then
                    print(3)
                else
                    print(0)
                end

                if b >= 20 then
                    print(4)
                else
                    print(0)
                end

                if a == 10 then
                    print(5)
                else
                    print(0)
                end

                if a ~= b then
                    print(6)
                else
                    print(0)
                end
                """;

        String expectedOutput = String.join(
                System.lineSeparator(),
                "1", "2", "3", "4", "5", "6"
        ) + System.lineSeparator();

        String actualOutput = compileAndExecute(source, "");

        assertEquals(expectedOutput, actualOutput);
    }
    @Test
    public void shouldRespectOperatorPrecedenceAndParentheses() {

        String source = """
                local resultado1 = 2 + 3 * 4
                local resultado2 = (2 + 3) * 4

                print(resultado1)
                print(resultado2)
                """;

        String expectedOutput = String.join(
                System.lineSeparator(),
                "14",
                "20"
        ) + System.lineSeparator();

        String actualOutput = compileAndExecute(source, "");

        assertEquals(expectedOutput, actualOutput);
    }
    @Test
    public void shouldCompileAndExecuteNestedConditionals() {

        String source1 = """
                local idade = 20
                local possuiCarteira = 1

                if idade >= 18 then
                    if possuiCarteira == 1 then
                        print(1)
                    else
                        print(2)
                    end
                else
                    print(3)
                end
                """;

        String source2 = """
                local idade = 20
                local possuiCarteira = 0

                if idade >= 18 then
                    if possuiCarteira == 1 then
                        print(1)
                    else
                        print(2)
                    end
                else
                    print(3)
                end
                """;

        String source3 = """
                local idade = 15
                local possuiCarteira = 1

                if idade >= 18 then
                    if possuiCarteira == 1 then
                        print(1)
                    else
                        print(2)
                    end
                else
                    print(3)
                end
                """;

        assertEquals(
                "1" + System.lineSeparator(),
                compileAndExecute(source1, "")
        );

        assertEquals(
                "2" + System.lineSeparator(),
                compileAndExecute(source2, "")
        );

        assertEquals(
                "3" + System.lineSeparator(),
                compileAndExecute(source3, "")
        );
    }
}
