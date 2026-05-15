package com.compiler;

import com.compiler.lexer.Lexer;
import com.compiler.lexer.Token;
import com.compiler.parser.ASTNode;
import com.compiler.parser.Parser;
import java.util.List;

/**
 * Punto de entrada del compilador.
 */
public class Main {
    public static void main(String[] args) {
        Lexer lexer = new Lexer();

        // Usa ruta relativa (MUY importante)
        String ruta = "inputs/test_source.txt";

        List<Token> tokens = lexer.analizarArchivo(ruta);

        for (Token token : tokens) {
            System.out.println(token);
        }

        try {
            //Ejecutar el Analizador Sintáctico
            Parser parser = new Parser(tokens);

            ASTNode arbol = parser.parse();

            System.out.println("\nÁrbol de Sintaxis Abstracta:");
            arbol.imprimir("");

        } catch (RuntimeException e) {
            System.err.println("Error de compilación: " + e.getMessage());
        }
    }
}