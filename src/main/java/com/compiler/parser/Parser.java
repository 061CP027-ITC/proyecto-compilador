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

    // Metodo Parse
    public ASTNode parse() {
        ASTNode raiz = new ASTNode("PROGRAMA", "");

        while (!actual().getTipo().equals("EOF")) {
            raiz.agregarHijo(sentencia());
        }

        return raiz;
    }

    // Metodo Sentencia
    private ASTNode sentencia() {

        // int x = 10;
        // float y = 20;
        if (actual().getTipo().equals("PALABRA_RESERVADA") &&
                (actual().getValor().equals("int") ||
                 actual().getValor().equals("float"))) {
            return declaracion();
        }
        // Agregamos el caso "if" ya que antes al llegar al if lanzaba error
        else if (actual().getTipo().equals("PALABRA_RESERVADA") &&
                actual().getValor().equals("if")) {
            return sentenciaIf();
        }
        // Agregamos el caso "print, print("Hola Mundo") no es una asignación ni declaración
        // Necesita su propio metodo
        else if (actual().getTipo().equals("PALABRA_RESERVADA") &&
                actual().getValor().equals("print")) {
            return sentenciaPrint();
        }

        else if (actual().getTipo().equals("PALABRA_RESERVADA") &&
                actual().getValor().equals("while")) {
            return sentenciaWhile();
        }

        // x = 20;
        else if (actual().getTipo().equals("IDENTIFICADOR")) {
            return asignacion();
        }

        throw new RuntimeException(
                "Error sintáctico en token: " + actual().getValor()
        );
    }

    // Metodo Declaración
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

    // Metodo Asignación
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

    // Metodo nuevo sentencia if
    private ASTNode sentenciaIf() {
        avanzar(); // consume "if"

        if (!matchValor("(")) throw new RuntimeException("Se esperaba ( después de if");
        ASTNode condicion = condicion();
        if (!matchValor(")")) throw new RuntimeException("Se esperaba ) después de condición");
        if (!matchValor("{")) throw new RuntimeException("Se esperaba { para abrir bloque if");

        ASTNode cuerpo = new ASTNode("CUERPO", "");
        while (!actual().getValor().equals("}") && !actual().getTipo().equals("EOF")) {
            cuerpo.agregarHijo(sentencia());
        }
        if (!matchValor("}")) throw new RuntimeException("Se esperaba } para cerrar bloque if");

        ASTNode nodoIf = new ASTNode("IF", "");
        nodoIf.agregarHijo(condicion);
        nodoIf.agregarHijo(cuerpo);

        // Capturar else si viene inmediatamente después.
        // Antes el parser terminaba aquí y al encontrar "else" en la
        // siguiente iteración de parse(), lo mandaba a sentencia() donde
        // no había caso para él y lanzaba RuntimeException
        if (actual().getTipo().equals("PALABRA_RESERVADA") &&
                actual().getValor().equals("else")) {
            avanzar(); // consume "else"

            if (!matchValor("{")) throw new RuntimeException("Se esperaba { después de else");

            ASTNode cuerpoElse = new ASTNode("CUERPO_ELSE", "");
            while (!actual().getValor().equals("}") && !actual().getTipo().equals("EOF")) {
                cuerpoElse.agregarHijo(sentencia());
            }
            if (!matchValor("}")) throw new RuntimeException("Se esperaba } para cerrar else");

            nodoIf.agregarHijo(cuerpoElse); // tercer hijo, opcional
        }

        return nodoIf;
    }

    // Metodo nuevo condicion
    private ASTNode condicion() {
        ASTNode izquierda = factor(); // reutilizamos factor() para leer x o un número

        if (!actual().getTipo().equals("OPERADOR")) {
            throw new RuntimeException("Se esperaba operador relacional en condición");
        }

        Token operador = actual();
        avanzar();

        ASTNode derecha = factor();

        ASTNode nodoCondicion = new ASTNode("CONDICION", operador.getValor());
        nodoCondicion.agregarHijo(izquierda);
        nodoCondicion.agregarHijo(derecha);

        return nodoCondicion;
    }

    // Metodo nuevo sentencia print
    private ASTNode sentenciaPrint() {
        avanzar(); // consume "print"

        if (!matchValor("(")) {
            throw new RuntimeException("Se esperaba ( después de print");
        }

        // No usar factor() aquí.
        // factor() solo maneja NUMERO, IDENTIFICADOR y expresiones entre
        // paréntesis. Un STRING no encaja en ninguno de esos casos.
        // Leemos el argumento directamente: sea STRING, NUMERO o IDENTIFICADOR.
        Token argumento = actual();
        if (!argumento.getTipo().equals("STRING") &&
                !argumento.getTipo().equals("NUMERO") &&
                !argumento.getTipo().equals("IDENTIFICADOR")) {
            throw new RuntimeException(
                    "Se esperaba argumento en print, se encontró: " + argumento.getValor()
            );
        }
        avanzar(); // consumimos el argumento

        if (!matchValor(")")) {
            throw new RuntimeException("Se esperaba ) después del argumento");
        }

        if (!matchValor(";")) {
            throw new RuntimeException("Se esperaba ; después de print(...)");
        }

        ASTNode nodoPrint = new ASTNode("PRINT", "");
        nodoPrint.agregarHijo(new ASTNode("ARGUMENTO", argumento.getValor()));

        return nodoPrint;
    }

    // Metodo nuevo sentencia while
    private ASTNode sentenciaWhile() {
        avanzar(); // consume "while"

        if (!matchValor("(")) throw new RuntimeException("Se esperaba ( después de while");
        ASTNode condicion = condicion();
        if (!matchValor(")")) throw new RuntimeException("Se esperaba ) después de condición");
        if (!matchValor("{")) throw new RuntimeException("Se esperaba { para abrir bloque while");

        ASTNode cuerpo = new ASTNode("CUERPO", "");
        while (!actual().getValor().equals("}") && !actual().getTipo().equals("EOF")) {
            cuerpo.agregarHijo(sentencia());
        }
        if (!matchValor("}")) throw new RuntimeException("Se esperaba } para cerrar bloque while");

        ASTNode nodoWhile = new ASTNode("WHILE", "");
        nodoWhile.agregarHijo(condicion);
        nodoWhile.agregarHijo(cuerpo);

        return nodoWhile;
    }

    // Metodo Expresión
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

    // Metodo Término
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

    // Metodo Factor
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