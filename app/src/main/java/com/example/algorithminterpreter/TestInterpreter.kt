package com.example.algorithminterpreter

fun main()
{
    val code = "int x console.read x console.write x + 45 * 2"

    val lexer = Lexer(code)
    lexer.lexAnalysis()

    val parser = Parser(lexer.tokens)
    val rootNode = parser.parseCode()
    parser.run(rootNode)
}