package com.example.algorithminterpreter

fun main()
{
    val code = "int x x = 5 int mas[3] mas[0] = 1 mas[1] = 2 mas[2] = 3 console.write x console.write mas[0]" +
            "console.write mas[1] console.write mas[2]"

    val lexer = Lexer(code)
    lexer.lexAnalysis()

    val parser = Parser(lexer.tokens)
    val rootNode = parser.parseCode()
    parser.run(rootNode)
}