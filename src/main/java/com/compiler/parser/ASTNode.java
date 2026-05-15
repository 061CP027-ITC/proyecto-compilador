package com.compiler.parser;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase base para los nodos del Árbol de Sintaxis Abstracta.
 */
public class ASTNode {

    // Definición base de nodos
    private String tipo;
    private String valor;
    private List<ASTNode> hijos;

    public ASTNode(String tipo, String valor){
        this.tipo = tipo;
        this.valor = valor;
        this.hijos = new ArrayList<>();
    }

    public void agregarHijo(ASTNode hijo){
        hijos.add(hijo);
    }

    public String getTipo() {
        return tipo;
    }

    public String getValor() {
        return valor;
    }

    public List<ASTNode> getHijos() {
        return hijos;
    }

    public void imprimir(String prefijo) {
        System.out.println(prefijo + tipo + (valor != null ? " : " + valor : ""));
        for (ASTNode hijo : hijos) {
            hijo.imprimir(prefijo + "  ");
        }
    }

}