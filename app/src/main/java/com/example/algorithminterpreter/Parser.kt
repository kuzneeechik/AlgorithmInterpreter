package com.example.algorithminterpreter

import com.example.algorithminterpreter.AST.*

class Parser(private val tokens: List<Token>) {
    private var pos: Int = 0
    private var scope = mutableMapOf<String, Any>()

    private fun match(vararg expected: TokenType): Token? {
        if (pos < tokens.size) {
            val currentToken = tokens[pos]
            if (expected.any { it.name == currentToken.type.name }) {
                pos += 1
                return currentToken
            }
        }
        return null
    }

    private fun require(vararg expected: TokenType): Token {
        val token = match(*expected)
        if(token == null) {
            throw Error("на позиции $pos ожидается ${expected[0].name}")
        }
        return token
    }

    private fun parseVariableOrNumber(): ExpressionNode {
        val number = match(tokenTypeList.find { it.name == "NUMBER" }!!)
        if (number != null) {
            return NumberNode(number)
        }
        val variable = match(tokenTypeList.find { it.name == "VARIABLE" }!!)
        if (variable != null) {
            return VariableNode(variable)
        }
        throw Error("Ожидается переменная или число на позиции $pos")
    }

    private fun parseParentheses(): ExpressionNode {
        if (match(tokenTypeList.find { it.name == "LEFT PAR" }!!) != null) {
            val node = parseFormula()
            require(tokenTypeList.find { it.name == "RIGHT PAR" }!!)
            return node
        }

        else {
            return parseVariableOrNumber()
        }
    }

    private fun parseFormula(): ExpressionNode {
        var leftNode = parseParentheses()
        var operator = match(
            tokenTypeList.find { it.name == "MINUS" }!!,
            tokenTypeList.find { it.name == "PLUS" }!!
        )
        while (operator != null) {
            val rightNode = parseParentheses()
            leftNode = BinOperationNode(operator, leftNode, rightNode)
            operator = match(
                tokenTypeList.find { it.name == "MINUS" }!!,
                tokenTypeList.find { it.name == "PLUS" }!!
            )
        }
        return leftNode
    }

    fun parseExpression(): ExpressionNode {
        if (match(tokenTypeList.find { it.name == "VARIABLE" }!!) == null) {
            throw Error("Ожидается переменная на позиции $pos")
        }
        pos -= 1
        val variableNode = parseVariableOrNumber()
        val assignOperator = match(tokenTypeList.find { it.name == "ASSIGN" }!!)
        if (assignOperator != null) {
            val rightFormulaNode = parseFormula()
            return BinOperationNode(assignOperator, variableNode, rightFormulaNode)
        }
        throw Error("После переменной ожидается оператор присвоения на позиции $pos")
    }

    fun parseCode(): ExpressionNode {
        val root = StatementsNode()
        while (pos < tokens.size) {
            val token = tokens[pos]
            if (token.type.name == "SPACE") {
                pos++
                continue
            }
            val codeStringNode = parseExpression()
            root.addNode(codeStringNode)
        }
        return root
    }

    fun run(node: ExpressionNode): Any? {
        when (node) {
            is NumberNode -> return node.number.text.toInt()
            is BinOperationNode -> {
                when (node.operator.type.name) {
                    "PLUS" -> return (run(node.leftNode) as Int + (run(node.rightNode) as Int))
                    "MINUS" -> return (run(node.leftNode) as Int - (run(node.rightNode) as Int))
                    "ASSIGN" -> {
                        val result = run(node.rightNode)
                        val variableNode = node.leftNode as VariableNode
                        scope[variableNode.variable.text] = result!!
                        return result
                    }
                }
            }
            is VariableNode -> {
                return scope[node.variable.text] ?: println("Переменная с названием ${node.variable.text} не обнаружена")
            }
            is StatementsNode -> {
                node.codeStrings.forEach { run(it) }
            }
            else -> println("Ошибка!")
        }
        return null
    }
}