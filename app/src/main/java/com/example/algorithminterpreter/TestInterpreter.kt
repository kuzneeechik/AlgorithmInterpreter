package com.example.algorithminterpreter

fun main()
{
    val code = "int mas[3] int i i = 0 while i < 3 console.read mas[i] i = i + 1 endwhile" +
            "i = 0 while i < 3 console.write mas[i] i = i + 1 endwhile"
    val lexer = Lexer(code)
    lexer.lexAnalysis()

    val parser = Parser(lexer.tokens)
    val rootNode = parser.parseCode()
    parser.run(rootNode)
}