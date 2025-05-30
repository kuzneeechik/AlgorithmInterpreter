package com.example.algorithminterpreter.ast

import com.example.algorithminterpreter.Token

class ArrayInitNode(
    val array: Token,
    val size: ExpressionNode
) : ExpressionNode