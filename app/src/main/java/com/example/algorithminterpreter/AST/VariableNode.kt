package com.example.algorithminterpreter.AST

import com.example.algorithminterpreter.Token

class VariableNode(
    val variable: Token
) : ExpressionNode