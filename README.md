# Compilador MiniLua

## Sobre o projeto

O MiniLua Compiler é um projeto desenvolvido para a disciplina de Compiladores. Ele consiste na criação de um compilador simplificado, baseado em um subconjunto da linguagem Lua, utilizando Java.

O objetivo do projeto é colocar em prática os conceitos estudados durante a disciplina, desde a leitura do código-fonte até sua execução.

O compilador recebe um arquivo `.lua`, realiza as etapas de análise e transforma o programa em instruções que são executadas por uma máquina virtual desenvolvida no próprio projeto.

## Como funciona

O compilador possui algumas etapas, cada uma responsável por uma parte do processo:

1. **Lexer:** faz a leitura do código e identifica os elementos presentes, como palavras reservadas, números e operadores.
2. **Parser:** verifica se o código está escrito corretamente e organiza sua estrutura.
3. **AST:** representa a organização do programa em uma estrutura de árvore.
4. **Análise semântica:** verifica questões como variáveis não declaradas e incompatibilidade de tipos.
5. **Código intermediário (TAC):** transforma o programa em uma representação intermediária.
6. **Bytecode:** converte o código intermediário em instruções para a máquina virtual.
7. **Máquina virtual:** executa as instruções geradas pelo compilador.

## Funcionalidades

O MiniLua permite utilizar:

- Declaração de variáveis com `local`.
- Atribuição de valores.
- Operações matemáticas: `+`, `-`, `*` e `/`.
- Comparações: `==`, `~=`, `<`, `>`, `<=` e `>=`.
- Valores inteiros e booleanos (`true` e `false`).
- Estruturas condicionais `if`, `else` e `end`.
- Estruturas de repetição `while` e `do`.
- Comando `print()` para exibir valores.
- Comando `read()` para receber números inteiros digitados pelo usuário.
- Comentários utilizando `--`.

## Limitações

Como o projeto implementa apenas uma parte da linguagem Lua, alguns recursos não estão disponíveis.

Entre eles estão:

- Utilização de strings e números decimais.
- Criação de funções pelo usuário.
- Utilização de tabelas.
- Estrutura de repetição `for`.
- Operadores lógicos `and`, `or` e `not`.
- Comandos `return` e `break`.
- Utilização de vários argumentos em uma chamada de função.

O comando `print()` aceita apenas um argumento, e o `read()` não recebe argumentos.

## Tecnologias utilizadas

- Java 21
- Maven
- JUnit 5
- JaCoCo
- Apache NetBeans

## Organização do projeto

O projeto está organizado da seguinte maneira:

```text
minilua-compiler/
│
├── programas/
│   ├── exemplo.lua
│   ├── fatorial.lua
│   └── entrada_usuario.lua
│
├── src/
│   ├── main/
│   │   └── java/
│   └── test/
│       └── java/
│
├── pom.xml
├── .gitignore
└── README.md
```

A pasta `src/main/java` contém o código-fonte do compilador, enquanto `src/test/java` contém os testes automatizados.

A pasta `programas` reúne alguns arquivos `.lua` utilizados para demonstrar o funcionamento do projeto.

## Como executar o projeto

### Requisitos

Para executar o projeto, é necessário ter:

- JDK 21 instalado.
- Maven instalado.
- Um terminal ou ambiente de desenvolvimento Java.

### Compilando o projeto

Abra o terminal na pasta principal do projeto e execute:

```bash
mvn clean compile
```

### Executando um programa

Depois de compilar, execute o comando abaixo informando o arquivo `.lua` desejado:

```bash
java -cp target/classes com.mycompany.minilua.compiler.MiniluaCompiler programas/exemplo.lua
```

O compilador apresentará as etapas realizadas e, ao final, exibirá o resultado da execução.

### Programas disponíveis

Na pasta `programas`, estão disponíveis três exemplos:

| Arquivo | O que demonstra |
|---|---|
| `exemplo.lua` | Declaração de variáveis e operações matemáticas. |
| `fatorial.lua` | Utilização de repetição com `while` e operações matemáticas. |
| `entrada_usuario.lua` | Leitura de um número com `read()` e utilização de `if/else`. |

Para executar outro exemplo, basta substituir o nome do arquivo no comando.

**Observação:** o programa `entrada_usuario.lua` precisa receber um número pelo teclado. Por isso, recomenda-se executá-lo em um terminal que permita entrada de dados, como o PowerShell.

## Testes

O projeto possui testes automatizados para verificar o funcionamento das diferentes partes do compilador.

Para executar todos os testes, utilize:

```bash
mvn clean test
```

Também é possível gerar o relatório de cobertura dos testes com:

```bash
mvn clean verify
```

O relatório do JaCoCo ficará disponível em:

```text
target/site/jacoco/index.html
```

## Considerações finais

O desenvolvimento do MiniLua Compiler permitiu colocar em prática os principais conceitos estudados na disciplina de Compiladores.

Durante o projeto, foram trabalhadas etapas como análise léxica, análise sintática, análise semântica, geração de código e execução em uma máquina virtual.

Apesar de possuir algumas limitações em relação à linguagem Lua original, o projeto atende à proposta de implementar um compilador simplificado e demonstrar seu funcionamento por meio de programas de exemplo.
