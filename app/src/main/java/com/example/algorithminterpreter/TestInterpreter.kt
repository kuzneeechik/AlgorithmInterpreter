package com.example.algorithminterpreter

fun main()
{
    val code = "int mas[5] mas[0] = 56 mas[4] = 101 console.write mas[1] console.write mas[4]"

    val lexer = Lexer(code)
    lexer.lexAnalysis()

    val parser = Parser(lexer.tokens)
    val rootNode = parser.parseCode()
    parser.run(rootNode)
}