package com.example.algorithminterpreter.ast

import com.example.algorithminterpreter.Token

class VariableNode(
    val variable: Token
) : ExpressionNode