package com.compiler.parser;

import java.util.List;

/**
 * Nodo que representa un bloque de código entre llaves.
 * Contiene una lista de sentencias o nodos AST dentro del bloque.
 */
public class BlockNode extends ASTNode {
    // Sentencias que forman el bloque: declaraciones, condicionales, llamadas, etc.
    private List<ASTNode> statements;

    public BlockNode(List<ASTNode> statements){ this.statements = statements; }

    public List<ASTNode> getStatements(){ return statements; }

    @Override
    public String toString(){ return "Block" + statements; }
}