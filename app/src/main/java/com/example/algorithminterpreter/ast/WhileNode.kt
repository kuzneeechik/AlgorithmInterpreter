package com.example.algorithminterpreter.ast

class WhileNode (
    val condition: ExpressionNode,
    val body: StatementsNode
) : ExpressionNode