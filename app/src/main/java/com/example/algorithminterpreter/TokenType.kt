package com.example.algorithminterpreter

class TokenType(val name: String, val regular: String)

val tokenTypeList: MutableList<TokenType> = mutableListOf(
    TokenType("NUMBER", "^[0-9]+"),
    TokenType("WRITE", "^console\\.write"),
    TokenType("READ", "^console.read"),
    TokenType("INIT", "^int"),
    TokenType("VARIABLE", "^[A-Za-z]+"),
    TokenType("ASSIGN", "^="),
    TokenType("PLUS", "^\\+"),
    TokenType("MINUS", "^-"),
    TokenType ("DIV", "^\\/"),
    TokenType("MULTI", "^\\*"),
    TokenType("MOD", "^%"),
    TokenType("LEFT PAR", "^\\("),
    TokenType("RIGHT PAR", "^\\)"),
    TokenType("SPACE", "^[ \\n\\t\\r]"))
