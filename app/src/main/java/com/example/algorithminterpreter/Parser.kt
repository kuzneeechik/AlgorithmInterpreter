package com.example.algorithminterpreter
import com.example.algorithminterpreter.ast.*

class BreakException : RuntimeException()
class ContinueException : RuntimeException()
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
            skipSpaces()

            if (match(tokenTypeList.find { it.name == "LEFT BRACKET" }!!) != null)
            {
                val index = parseFormula()
                require(tokenTypeList.find {it.name == "RIGHT BRACKET"}!!)

                return ArrayNode(variable, index)
            }

            return VariableNode(variable)
        }

        throw Error("Variable or number is expected in the position $pos")
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
        skipSpaces()
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
            val variable = match(tokenTypeList.find { it.name == "VARIABLE" }!!)
                ?: throw Error("Variable name expected after 'int'")

            if (match(tokenTypeList.find { it.name == "LEFT BRACKET"}!!) != null)
            {
                val size = parseFormula()
                require(tokenTypeList.find { it.name == "RIGHT BRACKET"}!!)

                return ArrayInitNode(variable, size)
            }

            pos -= 1

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

                val ifNode = parseIf()
                return ifNode
            }

            else if (match(tokenTypeList.find { it.name == "WRITE" }!!) != null)
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

            else if (match(tokenTypeList.find { it.name == "WHILE" }!!) != null)
            {
                pos -= 1

                val whileNode = parseWhile()
                return whileNode
            }

            else if (match(tokenTypeList.find { it.name == "BREAK" }!!) != null)
            {
                val breakNode = BreakNode()
                return breakNode
            }

            else if (match(tokenTypeList.find { it.name == "CONTINUE" }!!) != null)
            {
                val continueNode = ContinueNode()
                return continueNode
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

    private fun parseWhile(): ExpressionNode
    {
        skipSpaces()
        require(tokenTypeList.find { it.name == "WHILE" }!!)
        skipSpaces()

        val condition = parseComparison()
        skipSpaces()

        val body = StatementsNode()

        while (true)
        {
            skipSpaces()
            if (match(tokenTypeList.find { it.name == "ENDWHILE" }!!) != null) break
            body.addNode(parseExpression())
        }

        return WhileNode(condition, body)
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

                        when (val left = node.leftNode)
                        {
                            is VariableNode -> {
                                val variableNode = node.leftNode as VariableNode
                                scope[variableNode.variable.text] = result!!
                            }

                            is ArrayNode -> {
                                val array = (scope[left.array.text] as? ArrayValue)?.array
                                    ?: throw Error("Variable ${left.array.text} is not an array")

                                val index = run(left.index) as Long

                                if (index !in 0 until array.size)
                                {
                                    throw Error("Index $index out of range")
                                }

                                array[index.toInt()] = result as Long
                            }

                            else -> throw Error("Left side must be variable or array")
                        }

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

                        when (val currentNode = node.operand)
                        {
                            is VariableNode -> {
                                if (scope[currentNode.variable.text] == null)
                                {
                                    println("The variable with name ${currentNode.variable.text} not found")
                                }
                                else
                                {
                                    scope[currentNode.variable.text] = value
                                }
                            }

                            is ArrayNode -> {
                                val arrayData = (scope[currentNode.array.text] as? ArrayValue)?.array
                                    ?: throw Error("Variable ${currentNode.array.text} is not an array")

                                val index = run(currentNode.index) as Long

                                if (index !in 0 until arrayData.size) {
                                    throw Error("Index $index out of range")
                                }

                                arrayData[index.toInt()] = value
                            }

                            else -> throw Error("Operand for READ must be a variable or an array element")
                        }
                    }
                    "INIT" -> {
                        val variableNode = node.operand as VariableNode
                        scope[variableNode.variable.text] = 0
                    }
                }
            }

            is VariableNode -> {
                return scope[node.variable.text] ?:
                "The variable with name ${node.variable.text} not found"
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

            is ArrayInitNode -> {
                val size = run(node.size) as Long

                if (size <= 0)
                {
                    throw Error("Array size must be positive")
                }

                val array = MutableList(size.toInt()) { 0L }

                scope[node.array.text] = ArrayValue(array)
            }

            is ArrayNode -> {
                val array = (scope[node.array.text] as? ArrayValue)?.array
                    ?: throw Error("Variable ${node.array.text} is not an array")

                val index = run(node.index) as Long

                if (index !in 0 until array.size)
                {
                    throw Error("Index $index out of range")
                }

                return array[index.toInt()]
            }

            is WhileNode -> {
                while (true)
                {
                    val conditionResult = run(node.condition)

                    if (conditionResult is Boolean && conditionResult)
                    {
                        try {
                            run(node.body)
                        } catch (e: BreakException) {
                            break
                        } catch (e: ContinueException) {
                            continue
                        }
                    }
                    else
                    {
                        break
                    }
                }
            }

            is BreakNode -> throw BreakException()

            is ContinueNode -> throw ContinueException()

            else -> println("Error!")
        }
        return null
    }
}