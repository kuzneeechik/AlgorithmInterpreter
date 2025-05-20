package com.example.algorithminterpreter

class Lexer(private var code: String) {
    private var pos: Int = 0
    val tokens: MutableList<Token> = mutableListOf()

    fun lexAnalysis()
    {
        val codeLength: Int = code.length

        while (nextToken(codeLength))
        {
            /* to do */
        }
    }

    private fun nextToken(codeLength: Int): Boolean
    {
        if (pos >= codeLength) {
            return false
        }

        for (i in 0 until tokenTypeList.size) {
            val currentType: TokenType = tokenTypeList[i]
            val currentRegular = currentType.regular.toRegex()

            val match = currentRegular.find(code)
            var result = ""

            if (match != null)
            {
                val start = match.range.first
                val end = match.range.last + 1

                result = code.substring(start, end)

                code = code.removeRange(start, end)
            }

            if (result.isNotEmpty())
            {
                val token = Token(currentType, result, pos)
                tokens.add(token)

                pos += result.length

                return true
            }
        }
        throw Error ("There is error on the position $pos")
    }
}