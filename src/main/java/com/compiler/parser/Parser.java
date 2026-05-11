package com.compiler.parser;

import com.compiler.lexer.Token;
import java.util.List;

/**
 * Encargado de construir el Árbol de Sintaxis Abstracta (AST).
 */
public class Parser {
    private List<Token> tokens;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public ASTNode parse() {
        return null; // Implementación pendiente
    }
}
