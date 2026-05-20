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
            "int","float","if","else","while","public","class","void","String","print"
            // Agregamos print como palabra reservada para que funcione con test_source.txt
    );

    private final List<String> operadores = Arrays.asList(
            "+", "-", "*", "/", "=", ">", "<"
    );

    // Agregamos Operadores dobles
    private final List<String> operadoresDobles = Arrays.asList(
            "==", "!=", ">=", "<="
    );

    private final List<String> simbolos = Arrays.asList(
            "(", ")", "{", "}", ";", ","
    );

    public List<Token> analizarArchivo(String rutaArchivo) {
        List<Token> tokens = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;

            while ((linea = br.readLine()) != null) {

                 // Vamos a ignorar los comentarios de linea //
                 // El metodo trim() elimina los espacios antes de checar si empieza con // para que no haya tokens
                 // que no nos sirven como un IDENTIFICADOR: //
                linea = linea.trim();
                if (linea.startsWith("//")) continue;

                String lineaMasticada = masticarLinea(linea);
                // En lugar de split(" ") usamos un tokenizador
                // propio que respeta los strings entre comillas
                // split(" ") parte "Hola Mundo" en dos pedazos porque ve
                // el espacio interior igual que cualquier otro separador
                // El nuevo metodo los mantiene juntos
                List<String> pedazos = dividirRespetandoStrings(lineaMasticada);

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
        // Operadores dobles ANTES que simples
        // Si procesamos "=" primero, "==" se convierte en " =  = " (dos tokens)
        // Al procesar "==" primero, queda " == " y luego el "=" simple
        // ya no encuentra ningún "=" suelto que partir
        for (String opDoble : operadoresDobles) {
            linea = linea.replace(opDoble, " " + opDoble + " ");
        }

        // Respetar strings entre comillas
        // El replace() original no distingue si está dentro de "..." o no,
        // así que "Hola Mundo" con un espacio quedaría partido en dos tokens
        // La nueva lógica copia los caracteres uno a uno: dentro de un string
        // los copia sin tocar, fuera aplica el espaciado normal
        StringBuilder resultado = new StringBuilder();
        boolean dentroDeString = false;

        for (int i = 0; i < linea.length(); i++) {
            char c = linea.charAt(i);

            if (c == '"') {
                dentroDeString = !dentroDeString;
                resultado.append(c);
                continue;
            }

            if (dentroDeString) {
                resultado.append(c); // dentro del string: copiar sin modificar
                continue;
            }

            // Fuera del string: comportamiento original
            String cs = String.valueOf(c);
            if (operadores.contains(cs) || simbolos.contains(cs)) {
                resultado.append(' ').append(c).append(' ');
            } else {
                resultado.append(c);
            }
        }
        return resultado.toString();
    }

    // Metodo nuevo divide por espacios pero mantiene juntos los strings
    private List<String> dividirRespetandoStrings(String linea) {
        List<String> pedazos = new ArrayList<>();
        StringBuilder actual = new StringBuilder();
        boolean dentroDeString = false;

        for (int i = 0; i < linea.length(); i++) {
            char c = linea.charAt(i);

            if (c == '"') {
                dentroDeString = !dentroDeString;
                actual.append(c);
                continue;
            }

            // Si estamos dentro de un string, acumulamos sin importar espacios
            if (dentroDeString) {
                actual.append(c);
                continue;
            }

            // Fuera del string: el espacio es separador
            if (c == ' ') {
                if (actual.length() > 0) {
                    pedazos.add(actual.toString());
                    actual.setLength(0); // limpiar el buffer
                }
            } else {
                actual.append(c);
            }
        }

        // El ultimo pedazo si quedo algo en el buffer
        if (actual.length() > 0) {
            pedazos.add(actual.toString());
        }

        return pedazos;
    }

    private Token clasificarToken(String pedazo) {
        if (reservadas.contains(pedazo)) {
            return new Token("PALABRA_RESERVADA", pedazo);

            // Operadores dobles antes que simples
            // Si checamos operadores primero con "=", el pedazo "==" nunca
            // llegaría aquí porque ya fue separado, pero si en algún caso
            // llegara, queremos que sea reconocido correctamente
        } else if (operadoresDobles.contains(pedazo)) {
            return new Token("OPERADOR", pedazo);

        } else if (operadores.contains(pedazo)) {
            return new Token("OPERADOR", pedazo);

        } else if (simbolos.contains(pedazo)) {
            return new Token("SIMBOLO", pedazo);

        } else if (esNumero(pedazo)) {
            return new Token("NUMERO", pedazo);

            // Reconocer strings literales
            // Despues del nuevo masticarLinea(), "Hola Mundo" llega intacto
            // con sus comillas. Sin este caso caería en IDENTIFICADOR, que
            // haria que el semántico intentara buscarlo en la tabla de simbolos
        } else if (pedazo.startsWith("\"") && pedazo.endsWith("\"")) {
            return new Token("STRING", pedazo);

        } else {
            return new Token("IDENTIFICADOR", pedazo);
        }
    }

    private boolean esNumero(String texto) {
        // Usar Double en lugar de Integer
        // Integer.parseInt() rechaza "3.14" con NumberFormatException,
        // así que el Lexer lo clasificaba como IDENTIFICADOR
        // Double.parseDouble() acepta tanto "10" como "3.14" y "0.5"
        try {
            Double.parseDouble(texto);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
