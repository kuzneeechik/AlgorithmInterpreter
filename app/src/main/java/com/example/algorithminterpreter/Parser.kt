package com.example.algorithminterpreter

import com.example.algorithminterpreter.ast.*

class Parser(private val tokens: List<Token>)
{
    private var pos: Int = 0
    private var scope = mutableMapOf<String, Any>()

    private fun skipSpaces()
    {
        while (pos < tokens.size && tokens[pos].type.name == "SPACE")
        {
            pos++
        }
    }

    private fun match(vararg expected: TokenType): Token?
    {
        if (pos < tokens.size)
        {
            val currentToken = tokens[pos]

            if (expected.any { it.name == currentToken.type.name })
            {
                pos += 1
                return currentToken
            }
        }
        return null
    }

    private fun require(vararg expected: TokenType): Token
    {
        val token = match(*expected) ?: throw Error("on the position $pos expected ${expected[0].name}")

        return token
    }

    private fun parseVariableOrNumber(): ExpressionNode
    {
        val number = match(tokenTypeList.find { it.name == "NUMBER" }!!)

        if (number != null)
        {
            return NumberNode(number)
        }

        val variable = match(tokenTypeList.find { it.name == "VARIABLE" }!!)

        if (variable != null)
        {
            return VariableNode(variable)
        }

        throw Error("Variable or number is expected on the position $pos")
    }

    private fun parseParentheses(): ExpressionNode
    {
        if (match(tokenTypeList.find { it.name == "LEFT PAR" }!!) != null)
        {
            val node = parseFormula()
            require(tokenTypeList.find { it.name == "RIGHT PAR" }!!)
            return node
        }

        else
        {
            return parseVariableOrNumber()
        }
    }

    private fun parseFormula(): ExpressionNode
    {
        skipSpaces()
        var leftNode = parseParentheses()
        skipSpaces()
        var operator = match(
            tokenTypeList.find { it.name == "MINUS" }!!,
            tokenTypeList.find { it.name == "PLUS" }!!,
            tokenTypeList.find { it.name == "MULTI"}!!,
            tokenTypeList.find { it.name == "DIV" }!!,
            tokenTypeList.find { it.name == "MOD" }!!)

        while (operator != null)
        {
            skipSpaces()
            val rightNode = parseParentheses()

            leftNode = BinOperationNode(operator, leftNode, rightNode)
            skipSpaces()
            operator = match(
                tokenTypeList.find { it.name == "MINUS" }!!,
                tokenTypeList.find { it.name == "PLUS" }!!,
                tokenTypeList.find { it.name == "MULTI" }!!,
                tokenTypeList.find { it.name == "DIV" }!!,
                tokenTypeList.find { it.name == "MOD" }!!)
        }

        return leftNode
    }

    private fun parsePrint(): ExpressionNode
    {
        skipSpaces()
        val tokenPrint = match(tokenTypeList.find {it.name == "WRITE"}!!)
        skipSpaces()

        if (tokenPrint != null)
        {
            return UnarOperationNode(tokenPrint, parseFormula())
        }

        throw Error("The console.write operator is expected on the position $pos")
    }

    private fun parseExpression(): ExpressionNode
    {
        if (match(tokenTypeList.find { it.name == "VARIABLE" }!!) == null)
        {
            val printNode = parsePrint()
            return printNode
        }

        pos -= 1

        val variableNode = parseVariableOrNumber()
        skipSpaces()
        val assignOperator = match(tokenTypeList.find { it.name == "ASSIGN" }!!)

        if (assignOperator != null)
        {
            val rightFormulaNode = parseFormula()
            return BinOperationNode(assignOperator, variableNode, rightFormulaNode)
        }

        throw Error("The assign operator is expected on the position $pos")
    }

    fun parseCode(): ExpressionNode {
        val root = StatementsNode()

        while (pos < tokens.size)
        {
            skipSpaces()
            if (pos >= tokens.size) break

            val codeStringNode = parseExpression()
            root.addNode(codeStringNode)
            skipSpaces()
        }
        return root
    }

    fun run(node: ExpressionNode): Any?
    {
        when (node)
        {
            is NumberNode -> return node.number.text.toLong()

            is BinOperationNode -> {
                when (node.operator.type.name)
                {
                    "PLUS" -> return (run(node.leftNode) as Long + (run(node.rightNode) as Long))
                    "MINUS" -> return (run(node.leftNode) as Long - (run(node.rightNode) as Long))
                    "MULTI" -> return (run(node.leftNode) as Long * (run(node.rightNode) as Long))
                    "DIV" -> return (run(node.leftNode) as Long / (run(node.rightNode) as Long))
                    "MOD" -> return (run(node.leftNode) as Long % (run(node.rightNode) as Long))
                    "ASSIGN" -> {
                        val result = run(node.rightNode)
                        val variableNode = node.leftNode as VariableNode
                        scope[variableNode.variable.text] = result!!
                        return result
                    }
                }
            }

            is UnarOperationNode -> {
                when (node.operator.type.name)
                {
                    "WRITE" -> println(run(node.operand))
                }
            }

            is VariableNode -> {
                return scope[node.variable.text] ?: println("The variable with name ${node.variable.text} not found")
            }

            is StatementsNode -> {
                node.codeStrings.forEach { run(it) }
            }

            else -> println("Error!")
        }
        return null
    }
}