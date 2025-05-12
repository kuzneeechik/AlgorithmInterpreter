package com.example.algorithminterpreter

fun main()
{
    val code = "x=3 y=3+9"

    val lexer = Lexer(code)
    val tokens = lexer.lexAnalysis()

    val parser = Parser(lexer.tokens)
    val rootNode = parser.parseCode()
    parser.run(rootNode)
}