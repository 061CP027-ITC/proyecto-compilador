package com.compiler;

import com.compiler.lexer.Lexer;
import com.compiler.lexer.Token;
import com.compiler.parser.ASTNode;
import com.compiler.parser.Parser;
import com.compiler.semantic.SemanticAnalyzer;
import java.util.List;

/**
 * Punto de entrada del compilador.
 */
public class Main {
    public static void main(String[] args) {

        // Usa ruta relativa (MUY importante)
        String ruta = "inputs/test_source.txt";

        // -- Fase 1: Análisis léxico --
        Lexer lexer = new Lexer();
        List<Token> tokens = lexer.analizarArchivo(ruta);

        System.out.println("-- TOKENS --");
        for (Token token : tokens) {
            System.out.println(token);
        }

        try {
            // -- Fase 2: Análisis sintáctico --
            Parser parser = new Parser(tokens);
            ASTNode ast = parser.parse();

            System.out.println("\n-- AST --");
            ast.imprimir("");

            // -- Fase 3: Análisis semántico --
            System.out.println("\n-- ANALISIS SEMANTICO --");
            SemanticAnalyzer semantico = new SemanticAnalyzer();
            semantico.analyze(ast);

        } catch (RuntimeException e) {
            System.err.println("\nError de compilacion: " + e.getMessage());
        }
    }
}