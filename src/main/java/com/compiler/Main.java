package com.compiler;

import com.compiler.lexer.Lexer;
import com.compiler.lexer.Token;
import com.compiler.syntactic.Syntactic;
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

        Syntactic syntactic = new Syntactic(tokens);
        syntactic.analizar();
    }
}