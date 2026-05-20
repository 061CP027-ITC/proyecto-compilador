package com.compiler.semantic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SymbolTable {

    // Cambiamos el HashMap plano por una pila de HashMaps
    // Antes había un solo nivel y todas las variables vivían juntas
    // Ahora cada bloque { } abre su propio nivel; al cerrarlo,
    // sus variables desaparecen. La búsqueda sube por los niveles
    // desde el más interno hasta el global, igual que en Java real
    private final List<Map<String, String>> pilaScopes = new ArrayList<>();

    public SymbolTable() {
        entrarScope(); // scope global
    }

    public void entrarScope() {
        pilaScopes.add(new HashMap<>());
    }

    public void salirScope() {
        if (pilaScopes.size() > 1) { // nunca eliminar el scope global
            pilaScopes.remove(pilaScopes.size() - 1);
        }
    }

    public void declarar(String nombre, String tipo) {
        Map<String, String> scopeActual = pilaScopes.get(pilaScopes.size() - 1);
        if (scopeActual.containsKey(nombre)) {
            throw new RuntimeException(
                    "Error semantico: variable " + nombre + " ya fue declarada en este scope"
            );
        }
        scopeActual.put(nombre, tipo);
    }

    // Busca de adentro hacia afuera: primero el scope actual,
    // luego el padre, luego el abuelo, hasta el global
    public String obtenerTipo(String nombre) {
        for (int i = pilaScopes.size() - 1; i >= 0; i--) {
            if (pilaScopes.get(i).containsKey(nombre)) {
                return pilaScopes.get(i).get(nombre);
            }
        }
        throw new RuntimeException(
                "Error semantico: variable " + nombre + " no fue declarada"
        );
    }

    public boolean existe(String nombre) {
        for (int i = pilaScopes.size() - 1; i >= 0; i--) {
            if (pilaScopes.get(i).containsKey(nombre)) return true;
        }
        return false;
    }

    public void imprimir() {
        System.out.println("-- Tabla de Simbolos --");
        for (int i = 0; i < pilaScopes.size(); i++) {
            System.out.println("  Scope " + i + ":");
            for (Map.Entry<String, String> entrada : pilaScopes.get(i).entrySet()) {
                System.out.println("    " + entrada.getKey() + ": " + entrada.getValue());
            }
        }
        System.out.println("---------------------");
    }
}