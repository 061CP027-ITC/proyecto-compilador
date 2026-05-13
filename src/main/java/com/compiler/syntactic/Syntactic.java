//Analizador sintáctico (11/05/2026)

package com.compiler.syntactic;

import com.compiler.lexer.Token;
import java.util.List;

public class Syntactic {

    private final List<Token> tokens;
    private int posicion = 0;

    public Syntactic(List<Token> tokens) {
        this.tokens = tokens;
    }

    public boolean analizar() {
        try {
            while (posicion < tokens.size()) {
                sentencia();
            }
                System.out.println("Análisis sintáctico completado sin errores.");
                return true;

        } catch (RuntimeException e) {
            System.err.println("Error en el análisis sintáctico: " + e.getMessage());
            return false;
        }
        
    }

    private void declaracion() {
        consumirTipo("PALABRA_RESERVADA");
        consumirTipo("IDENTIFICADOR");
        consumirValor("=");
        expresion();
        consumirValor(";");
    }

    private void expresion() {
        termino();
        while (verificarValor("+") || verificarValor("-")) {
            avanzar();
            termino();
        }
    }

    private void termino() {
        factor();
        while (verificarValor("*") || verificarValor("/")) {
            avanzar();
            factor();
        }
    }

    private void factor() {
        if (verificarTipo("NUMERO") || verificarTipo("IDENTIFICADOR")) {
            avanzar();
        } else {
            throw new RuntimeException("Se esperaba número o identificador");
        }
    }

    private void consumirTipo(String tipoEsperado) {
        if (verificarTipo(tipoEsperado)) {
            avanzar();
            } else {
                throw new RuntimeException(
                    "Se esperaba tipo: " + tipoEsperado
                );
            }
    }

    private void consumirValor(String valorEsperado) {
        if (verificarValor(valorEsperado)) {
            avanzar();
            } else {
                throw new RuntimeException(
                    "Se esperaba: " + valorEsperado
                );
            }
    }

    private boolean verificarTipo(String tipo) {
        return posicion < tokens.size() && tokens.get(posicion).getTipo().equals(tipo);
    }

    private boolean verificarValor(String valor) {
        return posicion < tokens.size() && tokens.get(posicion).getValor().equals(valor);
    }

    private void avanzar() {
    posicion++;
    }

    // Soporte para sentencias if (ejemplo de estructura de control)
    private void sentenciaIf() {
        consumirValor("if");
        consumirValor("(");
        expresion();
        consumirValor(")");
        consumirValor("{");

        while (!verificarValor("}")) {
            sentencia();
        }
        consumirValor("}");
    }

    // Soporte para sentencias Print (ejemplo de función)
    private void sentenciaPrint() {
        consumirValor("print");
        consumirValor("(");
        expresion();
        consumirValor(")");
        consumirValor(";");
    }

    //Enrutamiento para sentencias (ejemplo de estructura de control)
    private void sentencia() {
        if (verificarValor("if")) {
            sentenciaIf();

        } else if (verificarValor("print")) {
            sentenciaPrint();

        } else if (verificarTipo("PALABRA_RESERVADA")) {
            declaracion();

        } else {
            throw new RuntimeException(
            "Sentencia no reconocida: " + tokens.get(posicion).getValor()
            );
        }
    }

}