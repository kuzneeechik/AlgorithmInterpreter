package com.example.algorithminterpreter

fun main()
{
    val code = "int x x = 2 + 3 * 4 console.write x"

    val lexer = Lexer(code)
    lexer.lexAnalysis()

    val parser = Parser(lexer.tokens)
    val rootNode = parser.parseCode()
    parser.run(rootNode)
}