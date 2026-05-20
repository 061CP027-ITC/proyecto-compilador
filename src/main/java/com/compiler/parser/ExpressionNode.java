package com.compiler.parser;

/**
 * Nodo que representa una expresión simple dentro del AST.
 * Puede ser un identificador, un número, una cadena o un valor literal.
 */
public class ExpressionNode extends ASTNode {
    // Valor textual de la expresión, por ejemplo "x", "5" o "Hola Mundo"
    private String value;

    public ExpressionNode(String value) { this.value = value; }
    public String getValue() { return value; }
    public void setValue(String v) { this.value = v; }

    @Override
    public String toString() { return "Expr(" + value + ")"; }
}