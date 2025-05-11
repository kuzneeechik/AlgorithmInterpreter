package com.example.algorithminterpreter.AST

import com.example.algorithminterpreter.Token

class BinOperationNode(
    val operator: Token,
    val leftNode: ExpressionNode,
    val rightNode: ExpressionNode
) : ExpressionNode