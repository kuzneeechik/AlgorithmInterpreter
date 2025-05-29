package com.example.algorithminterpreter

fun main()
{
    val code = "int n " +
            "n = 7 " +
            "int mas[n] " +
            "int i " +
            "while i < n " +
            "console.read mas[i] " +
            "i = i + 1 endwhile " +
            "int j " +
            "int k " +
            "int b " +
            "while j < n " +
            "k = 0 " +
            "while k < n - 1 " +
            "if mas[k] > mas[k + 1] " +
            "b = mas[k] " +
            "mas[k] = mas[k + 1] " +
            "mas[k + 1] = b endif k = k + 1 endwhile j = j + 1 endwhile " +
            "i = 0 " +
            "while i < n " +
            "console.write mas[i] i = i + 1 endwhile"

    val lexer = Lexer(code)
    lexer.lexAnalysis()

    val parser = Parser(lexer.tokens)
    val rootNode = parser.parseCode()
    parser.run(rootNode)
}