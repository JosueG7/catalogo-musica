package com.musica.avl;

import com.musica.model.Cancion;
import java.util.ArrayList;
import java.util.List;

/*
 * Lista doblemente enlazada para manejar playlists
 * Cada nodo apunta al siguiente Y al anterior
 * Esto permite navegar en ambas direcciones (siguiente/anterior cancion)
 */
public class ListaDoble {

    // Nodo interno de la lista
    private static class Nodo {
        Cancion cancion;
        Nodo siguiente;
        Nodo anterior;

        Nodo(Cancion c) {
            this.cancion   = c;
            this.siguiente = null;
            this.anterior  = null;
        }
    }

    private Nodo cabeza;   // primer elemento
    private Nodo cola;     // ultimo elemento
    private int  tamanio;

    public ListaDoble() {
        cabeza  = null;
        cola    = null;
        tamanio = 0;
    }

    // Agrega una cancion al final de la playlist
    public void agregar(Cancion cancion) {
        Nodo nuevo = new Nodo(cancion);
        if (cabeza == null) {
            // lista vacia, el nodo nuevo es cabeza y cola a la vez
            cabeza = nuevo;
            cola   = nuevo;
        } else {
            // enlazamos el nuevo nodo al final
            nuevo.anterior = cola;
            cola.siguiente = nuevo;
            cola = nuevo;
        }
        tamanio++;
    }

    // Convierte la lista a un List normal para poder enviarlo como JSON
    public List<Cancion> aLista() {
        List<Cancion> resultado = new ArrayList<>();
        Nodo actual = cabeza;
        while (actual != null) {
            resultado.add(actual.cancion);
            actual = actual.siguiente;
        }
        return resultado;
    }

    public int getTamanio() { return tamanio; }

    // Genera el codigo PlantUML para visualizar la lista doble
    public String generarPlantUML() {
        StringBuilder sb = new StringBuilder();
        sb.append("@startuml\n");
        sb.append("digraph ListaDoble {\n");
        sb.append("  rankdir=LR\n"); // de izquierda a derecha
        sb.append("  node [shape=record, style=filled, fillcolor=lightgreen]\n");

        if (cabeza == null) {
            sb.append("  vacio [label=\"Lista vacia\", fillcolor=lightyellow]\n");
        } else {
            Nodo actual = cabeza;
            int i = 0;
            while (actual != null) {
                String nombre = actual.cancion.getNombre().replace("\"", "'");
                sb.append("  n").append(i)
                  .append(" [label=\"{<prev> |").append(nombre).append("| <next>}\"]\n");
                if (actual.siguiente != null) {
                    // flecha hacia adelante y hacia atras (doble enlace)
                    sb.append("  n").append(i).append(":next -> n").append(i + 1).append(":prev\n");
                    sb.append("  n").append(i + 1).append(":prev -> n").append(i).append(":next\n");
                }
                actual = actual.siguiente;
                i++;
            }
        }

        sb.append("}\n");
        sb.append("@enduml\n");
        return sb.toString();
    }
}
