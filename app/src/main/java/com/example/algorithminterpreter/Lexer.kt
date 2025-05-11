package com.example.algorithminterpreter

class Lexer(private val code: String) {
    private var pos: Int = 0
    private val tokens: MutableList<Token> = mutableListOf()

    fun lexAnalysis(): MutableList<Token>
    {
        while (nextToken())
        {
            println("work")
        }

        return tokens
    }

    private fun nextToken(): Boolean
    {
        if (pos >= code.length) {
            return false
        }

        for (i in 0 until tokenTypeList.size) {
            val currentType: TokenType = tokenTypeList[i]
            val currentRegular = Regex('^' + currentType.regular)
            var result = ""

            if (currentRegular.containsMatchIn(code)) {
                result = code.substring(pos)
            }

            if (result.isNotEmpty()) {
                val token = Token(currentType, result, pos)
                tokens.add(token)

                pos += result.length

                return true
            }
        }
        println("На позиции $pos обнаружена ошибка")
        return false
    }
}