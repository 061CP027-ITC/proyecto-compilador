package com.compiler.lexer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
/**
 * Encargado de convertir el flujo de caracteres en tokens.
 */
public class Lexer {
    private final List<String> reservadas = Arrays.asList(
            "int","float","if","else","while","public","class","void","String"
    );

    private final List<String> operadores = Arrays.asList(
            "+", "-", "*", "/", "=", ">", "<"
    );

    private final List<String> simbolos = Arrays.asList(
            "(", ")", "{", "}", ";", ","
    );

    public List<Token> analizarArchivo(String rutaArchivo) {
        List<Token> tokens = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;

            while ((linea = br.readLine()) != null) {

                String lineaMasticada = masticarLinea(linea);

                String[] pedazos = lineaMasticada.split(" ");

                for (String pedazo : pedazos) {
                    pedazo = pedazo.trim();

                    if (pedazo.isEmpty()) continue;

                    tokens.add(clasificarToken(pedazo));
                }
            }

        } catch (IOException e) {
            System.out.println("Error leyendo archivo: " + e.getMessage());
        }

        return tokens;
    }

    private String masticarLinea(String linea) {
        String lineaMasticada = linea;

        for (String operador : operadores) {
            lineaMasticada = lineaMasticada.replace(operador, " " + operador + " ");
        }

        for (String simbolo : simbolos) {
            lineaMasticada = lineaMasticada.replace(simbolo, " " + simbolo + " ");
        }

        return lineaMasticada;
    }

    private Token clasificarToken(String pedazo) {
        if (reservadas.contains(pedazo)) {
            return new Token("PALABRA_RESERVADA", pedazo);
        } else if (operadores.contains(pedazo)) {
            return new Token("OPERADOR", pedazo);
        } else if (simbolos.contains(pedazo)) {
            return new Token("SIMBOLO", pedazo);
        } else if (esNumero(pedazo)) {
            return new Token("NUMERO", pedazo);
        } else {
            return new Token("IDENTIFICADOR", pedazo);
        }
    }

    private boolean esNumero(String texto) {
        try {
            Integer.parseInt(texto);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
