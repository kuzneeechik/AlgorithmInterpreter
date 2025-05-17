package com.example.algorithminterpreter

class TokenType(val name: String, val regular: String)

val tokenTypeList: MutableList<TokenType> = mutableListOf(
    TokenType("WRITE", "^console\\.write"),
    TokenType("SPACE", "^[ \\n\\t\\r]+"),
    TokenType("INIT", "^int"),
    TokenType("IF", "^if"),
    TokenType("ELSE", "^else"),
    TokenType("ENDIF", "^endif"),
    TokenType("EQUALITY", "^=="),
    TokenType("UNEQUAL", "^!="),
    TokenType("UM", "^>="),
    TokenType("UL", "^<="),
    TokenType("ASSIGN", "^="),
    TokenType("MORE", "^>"),
    TokenType("LESS", "^<"),
    TokenType("PLUS", "^\\+"),
    TokenType("MINUS", "^-"),
    TokenType("MULTI", "^\\*"),
    TokenType("DIV", "^/"),
    TokenType("MOD", "^%"),
    TokenType("LEFT PAR", "^\\("),
    TokenType("RIGHT PAR", "^\\)"),
    TokenType("NUMBER", "^[0-9]+"),
    TokenType("VARIABLE", "^[A-Za-z_][A-Za-z0-9_]*"))