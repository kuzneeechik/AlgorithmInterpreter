package com.example.algorithminterpreter.ast

class StatementsNode : ExpressionNode {
    val codeStrings = mutableListOf<ExpressionNode>()

    fun addNode(node: ExpressionNode) {
        codeStrings.add(node)
    }
}