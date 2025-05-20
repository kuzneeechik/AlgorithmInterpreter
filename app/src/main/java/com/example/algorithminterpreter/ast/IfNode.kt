package com.example.algorithminterpreter.ast

class IfNode(
    val condition: ExpressionNode,
    val trueBranch: StatementsNode,
    val falseBranch: StatementsNode? = null
) : ExpressionNode