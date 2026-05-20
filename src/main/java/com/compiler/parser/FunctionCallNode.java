package com.compiler.parser;

import java.util.List;

/**
 * Nodo que representa una llamada a función o un statement de invocación:
 * print("Hola Mundo");
 */
public class FunctionCallNode extends ASTNode {
    // Nombre de la función invocada, por ejemplo "print"
    private String functionName;

    // Argumentos pasados a la función, cada uno como ExpressionNode
    private List<ExpressionNode> arguments;

    public FunctionCallNode(String functionName, List<ExpressionNode> arguments) {
        this.functionName = functionName;
        this.arguments = arguments;
    }

    public String getFunctionName() {
        return functionName;
    }

    public List<ExpressionNode> getArguments() {
        return arguments;
    }

    @Override
    public String toString() {
        return "FunctionCall{" +
                "name='" + functionName + '\'' +
                ", args=" + arguments +
                '}';
    }
}
