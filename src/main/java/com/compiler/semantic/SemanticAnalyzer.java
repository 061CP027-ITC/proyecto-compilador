package com.compiler.semantic;

import com.compiler.parser.ASTNode;

/**
 * Encargado de validaciones de tipos, alcance y coherencia.
 */
public class SemanticAnalyzer {
    private final SymbolTable tablaSimbolos = new SymbolTable();

    /**
     * Tomamos la raiz del AST y lo iteramos sobre todas las sentencias hijas
     */
    public void analyze(ASTNode ast) {
        for (ASTNode sentencia: ast.getHijos()) {
            analizarNodo(sentencia);
        }
        tablaSimbolos.imprimir();
    }

    /**
     * Acomodamos cada nodo al metodo correcto segun el tipo que sea
     */
    private void analizarNodo(ASTNode nodo) {
        switch (nodo.getTipo()) {
            case "DECLARACION":
                analizarDeclaracion(nodo);
                break;
            case "ASIGNACION":
                analizarAsignacion(nodo);
                break;
            case "IF":
                analizarIf(nodo);
                break;
            // Agregar WHILE
            // Sin este caso, un nodo WHILE producido por el nuevo parser
            // caía en el default y lanzaba "nodo desconocido"
            case "WHILE":
                analizarWhile(nodo);
                break;
            case "PRINT":
                System.out.println("Print valido: " + nodo.getHijos().get(0).getValor());
                break;
            default:
                throw new RuntimeException(
                        "Error semantico: nodo desconocido " + nodo.getTipo()
                );
        }
    }

    /**
     * Validamos una declaracion
     */
    private void analizarDeclaracion(ASTNode nodo) {
        String nombreVariable = nodo.getValor();
        String tipoDeclarado = nodo.getHijos().get(0).getValor();
        ASTNode expresion = nodo.getHijos().get(1);

        // Hacemos los registros en la tabla y si ya existe te avienta error
        tablaSimbolos.declarar(nombreVariable, tipoDeclarado);

        String tipoExpresion = inferirTipo(expresion);

        if (!sonCompatibles(tipoDeclarado, tipoExpresion)) {
            throw new RuntimeException(
                    "Error de tipos: no se puede asignar " + tipoExpresion + " a variable de tipo " + tipoDeclarado +
                            " (variable: " + nombreVariable + ")"
            );
        }
        System.out.println("Declaracion valida: " + tipoDeclarado + " " + nombreVariable);
    }

    /**
     * Validamos una asignacion
     */
    private void analizarAsignacion(ASTNode nodo) {
        String nombreVariable = nodo.getValor();

        String tipoDeclarado = tablaSimbolos.obtenerTipo(nombreVariable);

        ASTNode expresion = nodo.getHijos().get(0);
        String tipoExpresion = inferirTipo(expresion);

        if (!sonCompatibles(tipoDeclarado, tipoExpresion)) {
            throw new RuntimeException(
                    "Error de tipos: no se puede asignar " + tipoExpresion + " a " + nombreVariable + " que es de tipo "
                    + tipoDeclarado
            );
        }
        System.out.println("Asignacion valida: " + nombreVariable + " que es de tipo " + tipoDeclarado);
    }

    /**
     * Validamos if
     */
    private void analizarIf(ASTNode nodo) {
        ASTNode condicion = nodo.getHijos().get(0);
        ASTNode cuerpo    = nodo.getHijos().get(1);

        inferirTipo(condicion.getHijos().get(0));
        inferirTipo(condicion.getHijos().get(1));
        System.out.println("Condicion if valida: " + condicion.getValor());

        // EntrarScope / salirScope en bloques
        // Antes el cuerpo del if se analizaba sin abrir un nuevo nivel,
        // así que sus variables quedaban en la tabla global para siempre
        // y eran visibles fuera del bloque
        tablaSimbolos.entrarScope();
        for (ASTNode sentencia : cuerpo.getHijos()) {
            analizarNodo(sentencia);
        }
        tablaSimbolos.salirScope();

        // Analizar el else si existe (tercer hijo)
        // Antes analizarIf() ignoraba el tercer hijo por completo,
        // así que el cuerpo del else nunca se validaba semánticamente
        if (nodo.getHijos().size() > 2) {
            tablaSimbolos.entrarScope();
            for (ASTNode sentencia : nodo.getHijos().get(2).getHijos()) {
                analizarNodo(sentencia);
            }
            tablaSimbolos.salirScope();
        }
    }

    /**
     * Validamos while
     */
    private void analizarWhile(ASTNode nodo) {
        ASTNode condicion = nodo.getHijos().get(0);
        ASTNode cuerpo    = nodo.getHijos().get(1);

        inferirTipo(condicion.getHijos().get(0));
        inferirTipo(condicion.getHijos().get(1));
        System.out.println("Condicion while valida: " + condicion.getValor());

        tablaSimbolos.entrarScope();
        for (ASTNode sentencia : cuerpo.getHijos()) {
            analizarNodo(sentencia);
        }
        tablaSimbolos.salirScope();
    }

    /**
     * Inferimos el tipo de la expresion recorriendo el subarbol
     */
    private String inferirTipo(ASTNode nodo) {
        switch (nodo.getTipo()) {
            case "NUMERO":
                return nodo.getValor().contains(".") ? "float": "int";

            case "VARIABLE":
                return tablaSimbolos.obtenerTipo(nodo.getValor());

            case "OPERACION":
                String tipoIzquierda = inferirTipo(nodo.getHijos().get(0));
                String tipoDerecha = inferirTipo(nodo.getHijos().get(1));

                if (tipoIzquierda.equals("float") || tipoDerecha.equals("float")) {
                    return "float";
                }
                return "int";

            default:
                throw new RuntimeException(
                        "Error semantico: no se puede inferir tipo de nodo "
                        + nodo.getTipo()
                );
        }
    }

    /**
     * Compatibilidad de tipos
     */
    private boolean sonCompatibles(String tipoDestino, String tipoOrigen) {
        if (tipoDestino.equals(tipoOrigen)) return true;
        if (tipoDestino.equals("float") && tipoOrigen.equals("int")) return true;
        return false;
    }
}
