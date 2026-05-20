package com.compiler.parser;

/**
 * Nodo para declaraciones de variables.
 */
public class VariableDeclarationNode extends ASTNode {

    private String tipo;
    private String nombre;
    private String valor;

    public VariableDeclarationNode(String tipo, String nombre, String valor) {
        this.tipo = tipo;
        this.nombre = nombre;
        this.valor = valor;
    }

    @Override
    public String toString() {
        return "DeclaracionVariable {" +
                "tipo='" + tipo + '\'' +
                ", nombre='" + nombre + '\'' +
                ", valor='" + valor + '\'' +
                '}';
    }
}

