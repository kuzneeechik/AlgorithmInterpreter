package com.example.algorithminterpreter.AST

class StatementsNode : ExpressionNode {
    val codeStrings = mutableListOf<ExpressionNode>()

    fun addNode(node: ExpressionNode) {
        codeStrings.add(node)
    }
}