package com.example.algorithminterpreter
import com.example.algorithminterpreter.ast.*
import java.lang.Exception

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
        val token = match(*expected) ?: throw Error("${expected[0].name} is expected in the position $pos")

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

        throw Error("Variable or number is expected in the position $pos")
    }

    private fun parseParentheses(): ExpressionNode
    {
        skipSpaces()
        
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

    private fun parsePriorities(): ExpressionNode
    {
        skipSpaces()
        var leftNode = parseParentheses()
        skipSpaces()

        var operator = match(
            tokenTypeList.find { it.name == "MULTI" }!!,
            tokenTypeList.find { it.name == "DIV" }!!,
            tokenTypeList.find { it.name == "MOD" }!!
        )

        while (operator != null)
        {
            skipSpaces()
            val rightNode = parseParentheses()

            leftNode = BinOperationNode(operator, leftNode, rightNode)
            skipSpaces()
            
            operator = match(
                tokenTypeList.find { it.name == "MULTI" }!!,
                tokenTypeList.find { it.name == "DIV" }!!,
                tokenTypeList.find { it.name == "MOD" }!!
            )
        }

        return leftNode
    }

    private fun parseFormula(): ExpressionNode
    {
        var leftNode = parsePriorities()
        skipSpaces()

        var operator = match(
            tokenTypeList.find { it.name == "PLUS" }!!,
            tokenTypeList.find { it.name == "MINUS" }!!
        )

        while (operator != null)
        {
            skipSpaces()
            val rightNode = parsePriorities()

            leftNode = BinOperationNode(operator, leftNode, rightNode)
            skipSpaces()

            operator = match(
                tokenTypeList.find { it.name == "PLUS" }!!,
                tokenTypeList.find { it.name == "MINUS" }!!
            )
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

        throw Error("The 'console.write' operator is expected in the position $pos")
    }

    private fun parseRead(): ExpressionNode
    {
        skipSpaces()
        val tokenRead = match(tokenTypeList.find {it.name == "READ"}!!)
        skipSpaces()

        if (tokenRead != null)
        {
            return UnarOperationNode(tokenRead, parseVariableOrNumber())
        }

        throw Error("The 'console.read' operator is expected in the position $pos")
    }

    private fun parseInitialization(): ExpressionNode
    {
        skipSpaces()
        val tokenInit = match(tokenTypeList.find {it.name == "INIT"}!!)
        skipSpaces()

        if (tokenInit != null)
        {
            return UnarOperationNode(tokenInit, parseVariableOrNumber())
        }

        throw Error("The 'int' operator is expected in the position $pos")
    }

    private fun parseExpression(): ExpressionNode
    {
        if (match(tokenTypeList.find { it.name == "VARIABLE" }!!) == null)
        {
            if (match(tokenTypeList.find { it.name == "IF" }!!) != null)
            {
                pos -= 1
                return parseIf()
            }
            if (match(tokenTypeList.find { it.name == "WRITE" }!!) != null)
            {
                pos -= 1

                val printNode = parsePrint()
                return printNode
            }

            else if (match(tokenTypeList.find { it.name == "READ" }!!) != null)
            {
                pos -= 1

                val readNode = parseRead()
                return readNode
            }

            else if (match(tokenTypeList.find { it.name == "INIT" }!!) != null)
            {
                pos -= 1

                val initNode = parseInitialization()
                return initNode
            }
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

        throw Error("The assign operator is expected in the position $pos")
    }

    private fun parseIf(): ExpressionNode
    {
        skipSpaces()
        require(tokenTypeList.find { it.name == "IF" }!!)
        skipSpaces()

        val condition = parseComparison()
        skipSpaces()

        val trueBranch = StatementsNode()
        val falseBranch = StatementsNode()

        while (true)
        {
            skipSpaces()
            if (match(tokenTypeList.find { it.name == "ELSE" }!!) != null) break
            if (match(tokenTypeList.find { it.name == "ENDIF" }!!) != null)
            {
                return IfNode(condition, trueBranch, null)
            }
            trueBranch.addNode(parseExpression())
        }

        while (true)
        {
            skipSpaces()
            if (match(tokenTypeList.find { it.name == "ENDIF" }!!) != null) break
            falseBranch.addNode(parseExpression())
        }

        return IfNode(condition, trueBranch, falseBranch)
    }

    private fun parseComparison(): ExpressionNode
    {
        val leftNode = parseFormula()
        skipSpaces()

        val operator = match(
            tokenTypeList.find { it.name == "EQUALITY" }!!,
            tokenTypeList.find { it.name == "UNEQUAL" }!!,
            tokenTypeList.find { it.name == "LESS" }!!,
            tokenTypeList.find { it.name == "MORE" }!!,
            tokenTypeList.find { it.name == "UM" }!!,
            tokenTypeList.find { it.name == "UL" }!!
        )

        return if (operator != null)
        {
            skipSpaces()
            val rightNode = parseFormula()
            BinOperationNode(operator, leftNode, rightNode)
        }
        else
        {
            leftNode
        }
    }

    fun parseCode(): ExpressionNode
    {
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
                    "EQUALITY" -> return run(node.leftNode) == run(node.rightNode)
                    "UNEQUAL" -> return run(node.leftNode) != run(node.rightNode)
                    "LESS" -> return (run(node.leftNode) as Long) < (run(node.rightNode) as Long)
                    "UL" -> return (run(node.leftNode) as Long) <= (run(node.rightNode) as Long)
                    "MORE" -> return (run(node.leftNode) as Long) > (run(node.rightNode) as Long)
                    "UM" -> return (run(node.leftNode) as Long) >= (run(node.rightNode) as Long)
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
                    "WRITE" -> {
                        print(run(node.operand))
                        print(' ')
                    }
                    "READ" -> {
                        val value = readln().toLongOrNull() ?: throw Error("Incorrect input")

                        val variableNode = node.operand as VariableNode

                        if (scope[variableNode.variable.text] == null)
                        {
                            println("The variable with name ${variableNode.variable.text} not found")
                        }
                        else
                        {
                            scope[variableNode.variable.text] = value
                        }
                    }
                    "INIT" -> {
                        val variableNode = node.operand as VariableNode
                        scope[variableNode.variable.text] = 0
                    }
                }
            }

            is VariableNode -> {
                return scope[node.variable.text] ?: "The variable with name ${node.variable.text} not found"
            }

            is StatementsNode -> {
                node.codeStrings.forEach { run(it) }
            }

            is IfNode -> {
                val result = run(node.condition)
                if (result is Boolean && result)
                {
                    run(node.trueBranch)
                }
                else
                {
                    node.falseBranch?.let { run(it) }
                }
            }

            else -> println("Error!")
        }
        return null
    }
}