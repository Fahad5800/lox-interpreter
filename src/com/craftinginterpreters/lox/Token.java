package com.craftinginterpreters.lox;

public class Token {
    final TokenType type;
    final String lexeme;
    final Object literal;
    final int line;

    Token(TokenType type, String lexeme, Object literal, int line) {
        this.type = type;       // categorizes the tokens into types
        this.lexeme = lexeme;   // exact text from source code, for error handling
        this.literal = literal; // interpreted value for tokens
        this.line = line;       // for error detection
    }

    public String toString(){
        return type + " " + lexeme + " " + literal;
    }
}
