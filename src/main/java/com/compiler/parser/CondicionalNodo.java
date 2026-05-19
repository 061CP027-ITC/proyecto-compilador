package com.compiler.parser;

/**
 * Nodo que representa una estructura condicional en el AST.
 * Ejemplo: if (x > 5) { ... } else { ... }
 */
public class CondicionalNodo extends ASTNode {
    // Tipo de condicional: "if", "while", "for", etc.
    private String tipoCondicional;

    // lado izquierdo de la condición: x en "x > 5"
    private ExpressionNode left;

    // operador lógico o relacional: >, <, ==, !=, etc.
    private String operadorLogico;

    // lado derecho de la condición: 5 en "x > 5"
    private ExpressionNode right;

    // bloque que se ejecuta cuando la condición es verdadera
    private BlockNode thenBlock;

    // bloque opcional que se ejecuta cuando la condición es falsa
    private BlockNode elseBlock;

    public CondicionalNodo(String tipoCondicional,
                           ExpressionNode left,
                           String operadorLogico,
                           ExpressionNode right,
                           BlockNode thenBlock,
                           BlockNode elseBlock) {
        this.tipoCondicional = tipoCondicional;
        this.left = left;
        this.operadorLogico = operadorLogico;
        this.right = right;
        this.thenBlock = thenBlock;
        this.elseBlock = elseBlock;
    }

    public String getTipoCondicional() { return tipoCondicional; }
    public ExpressionNode getLeft() { return left; }
    public String getOperadorLogico() { return operadorLogico; }
    public ExpressionNode getRight() { return right; }
    public BlockNode getThenBlock() { return thenBlock; }
    public BlockNode getElseBlock() { return elseBlock; }

    @Override
    public String toString() {
        return "Condicional{" +
               "tipo='" + tipoCondicional + '\'' +
               ", left=" + left +
               ", op='" + operadorLogico + '\'' +
               ", right=" + right +
               ", then=" + thenBlock +
               (elseBlock != null ? ", else=" + elseBlock : "") +
               '}';
    }
}