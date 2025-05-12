package com.example.algorithminterpreter.ast

import com.example.algorithminterpreter.Token

class UnarOperationNode(
    val operator: Token,
    val operand: ExpressionNode
) : ExpressionNode