package com.example.algorithminterpreter.AST

import com.example.algorithminterpreter.Token

class UnarOperationNode(
    val operator: Token,
    val operand: ExpressionNode
) : ExpressionNode