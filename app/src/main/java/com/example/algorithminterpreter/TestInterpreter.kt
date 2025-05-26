package com.example.algorithminterpreter

fun main()
{
    val code = "int x x = 5 while x > 0 console.write x x = x - 1 endwhile"
    val lexer = Lexer(code)
    lexer.lexAnalysis()

    val parser = Parser(lexer.tokens)
    val rootNode = parser.parseCode()
    parser.run(rootNode)
}