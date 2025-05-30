package com.example.algorithminterpreter.ast

import com.example.algorithminterpreter.Token

class ArrayNode(
    val array: Token,
    val index: ExpressionNode
) : ExpressionNode