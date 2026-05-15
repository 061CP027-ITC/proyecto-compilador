package com.compiler.parser;

import com.compiler.lexer.Token;
import java.util.List;

public class Parser {

    private List<Token> tokens;
    private int posicion = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public Token actual() {
        if (posicion >= tokens.size()) {
            return new Token("EOF", "");
        }
        return tokens.get(posicion);
    }

    private void avanzar() {
        if (posicion < tokens.size()) {
            posicion++;
        }
    }

    private boolean matchTipo(String tipo) {
        if (actual().getTipo().equals(tipo)) {
            avanzar();
            return true;
        }
        return false;
    }

    private boolean matchValor(String valor) {
        if (actual().getValor().equals(valor)) {
            avanzar();
            return true;
        }
        return false;
    }

    // Método Parse
    public ASTNode parse() {
        ASTNode raiz = new ASTNode("PROGRAMA", "");

        while (!actual().getTipo().equals("EOF")) {
            raiz.agregarHijo(sentencia());
        }

        return raiz;
    }

    // Método Sentencia
    private ASTNode sentencia() {

        // int x = 10;
        // float y = 20;
        if (actual().getTipo().equals("PALABRA_RESERVADA") &&
                (actual().getValor().equals("int") ||
                 actual().getValor().equals("float"))) {

            return declaracion();
        }

        // x = 20;
        else if (actual().getTipo().equals("IDENTIFICADOR")) {
            return asignacion();
        }

        throw new RuntimeException(
                "Error sintáctico en token: " + actual().getValor()
        );
    }

    // Método Declaración
    private ASTNode declaracion() {
        Token tipo = actual();
        avanzar();

        Token id = actual();

        if (!matchTipo("IDENTIFICADOR")) {
            throw new RuntimeException("Se esperaba identificador");
        }

        if (!matchValor("=")) {
            throw new RuntimeException("Se esperaba =");
        }

        ASTNode expr = expresion();

        if (!matchValor(";")) {
            throw new RuntimeException("Se esperaba ;");
        }

        ASTNode nodo = new ASTNode("DECLARACION", id.getValor());

        nodo.agregarHijo(
                new ASTNode("TIPO", tipo.getValor())
        );

        nodo.agregarHijo(expr);

        return nodo;
    }

    // Método Asignación
    private ASTNode asignacion() {
        Token id = actual();
        avanzar();

        if (!matchValor("=")) {
            throw new RuntimeException("Se esperaba =");
        }

        ASTNode expr = expresion();

        if (!matchValor(";")) {
            throw new RuntimeException("Se esperaba ;");
        }

        ASTNode nodo = new ASTNode("ASIGNACION", id.getValor());
        nodo.agregarHijo(expr);

        return nodo;
    }

    // Método Expresión
    private ASTNode expresion() {
        ASTNode izquierda = termino();

        while (
                actual().getTipo().equals("OPERADOR") &&
                (
                        actual().getValor().equals("+") ||
                        actual().getValor().equals("-")
                )
        ) {
            Token operador = actual();
            avanzar();

            ASTNode derecha = termino();

            ASTNode nodoOperacion =
                    new ASTNode("OPERACION", operador.getValor());

            nodoOperacion.agregarHijo(izquierda);
            nodoOperacion.agregarHijo(derecha);

            izquierda = nodoOperacion;
        }

        return izquierda;
    }

    // Método Término
    private ASTNode termino() {
        ASTNode izquierda = factor();

        while (
                actual().getTipo().equals("OPERADOR") &&
                (
                        actual().getValor().equals("*") ||
                        actual().getValor().equals("/")
                )
        ) {
            Token operador = actual();
            avanzar();

            ASTNode derecha = factor();

            ASTNode nodoOperacion =
                    new ASTNode("OPERACION", operador.getValor());

            nodoOperacion.agregarHijo(izquierda);
            nodoOperacion.agregarHijo(derecha);

            izquierda = nodoOperacion;
        }

        return izquierda;
    }

    // Método Factor
    private ASTNode factor() {
        Token token = actual();

        if (matchTipo("NUMERO")) {
            return new ASTNode("NUMERO", token.getValor());
        }

        else if (matchTipo("IDENTIFICADOR")) {
            return new ASTNode("VARIABLE", token.getValor());
        }

        else if (matchValor("(")) {
            ASTNode nodoExpr = expresion();

            if (!matchValor(")")) {
                throw new RuntimeException("Se esperaba )");
            }

            return nodoExpr;
        }

        throw new RuntimeException(
                "Factor inválido: " + actual().getValor()
        );
    }
}