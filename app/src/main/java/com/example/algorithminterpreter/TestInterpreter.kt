package com.example.algorithminterpreter

fun main()
{
    val code = "x = 145 / 5 y = x * 3 console.write x + y"

    val lexer = Lexer(code)
    lexer.lexAnalysis()

    val parser = Parser(lexer.tokens)
    val rootNode = parser.parseCode()
    parser.run(rootNode)
}