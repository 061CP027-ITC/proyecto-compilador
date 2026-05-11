package com.compiler.lexer;

/**
 * Representación de un símbolo léxico.
 */
public class Token {
    private String tipo;
    private String valor;

    public Token(String tipo, String valor) {
        this.tipo = tipo;
        this.valor = valor;
    }

    public String getTipo() {
        return tipo;
    }

    public String getValor() {
        return valor;
    }

    @Override
    public String toString() {
        return tipo + " : " + valor;
    }
}
