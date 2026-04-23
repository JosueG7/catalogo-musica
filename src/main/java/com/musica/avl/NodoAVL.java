package com.musica.avl;

import com.musica.model.Cancion;

/*
 * Nodo del arbol AVL
 * Guarda la cancion, los hijos izquierdo/derecho y la altura
 * La altura la usamos para calcular el factor de balance
 */
public class NodoAVL {

    Cancion cancion;
    NodoAVL izquierdo;
    NodoAVL derecho;
    int altura;

    public NodoAVL(Cancion cancion) {
        this.cancion   = cancion;
        this.izquierdo = null;
        this.derecho   = null;
        this.altura    = 1; // toda hoja nueva empieza con altura 1
    }
}
