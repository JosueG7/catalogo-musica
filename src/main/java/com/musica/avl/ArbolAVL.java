package com.musica.avl;

import com.musica.model.Cancion;
import java.util.ArrayList;
import java.util.List;

/*
 * Implementacion manual del Arbol AVL
 * El AVL es un arbol binario de busqueda que se autobalancea.
 * Despues de cada insercion verifica el factor de balance de cada
 */
public class ArbolAVL {

    private NodoAVL raiz;

    public ArbolAVL() {
        this.raiz = null;
    }

    // =========================================================
    //  ALTURA Y FACTOR DE BALANCE
    // =========================================================

    // Retorna la altura del nodo, 0 si es null (evita NullPointerException)
    private int altura(NodoAVL n) {
        return n == null ? 0 : n.altura;
    }

    // Factor de balance: diferencia entre altura izquierda y derecha
    // Si da > 1 -> arbol cargado a la izquierda
    // Si da < -1 -> arbol cargado a la derecha
    private int factorBalance(NodoAVL n) {
        return n == null ? 0 : altura(n.izquierdo) - altura(n.derecho);
    }

    // Recalcula la altura de un nodo basandose en sus hijos
    private void actualizarAltura(NodoAVL n) {
        n.altura = 1 + Math.max(altura(n.izquierdo), altura(n.derecho));
    }

    // =========================================================
    //  ROTACIONES
    // =========================================================

    /*
     * Rotacion a la derecha - caso LL (izquierda-izquierda)
     */
    private NodoAVL rotarDerecha(NodoAVL y) {
        NodoAVL x   = y.izquierdo;
        NodoAVL T2  = x.derecho;

        // hacemos la rotacion
        x.derecho   = y;
        y.izquierdo = T2;

        // actualizamos alturas (primero y porque ahora es hijo de x)
        actualizarAltura(y);
        actualizarAltura(x);

        return x; // x es la nueva raiz del subarbol
    }

    /*
     * Rotacion a la izquierda - caso RR (derecha-derecha)
     */
    private NodoAVL rotarIzquierda(NodoAVL x) {
        NodoAVL y   = x.derecho;
        NodoAVL T2  = y.izquierdo;

        // hacemos la rotacion
        y.izquierdo = x;
        x.derecho   = T2;

        // actualizamos alturas
        actualizarAltura(x);
        actualizarAltura(y);

        return y; // y es la nueva raiz del subarbol
    }

    // =========================================================
    //  INSERCION
    // =========================================================

    // Metodo publico - la gente de afuera solo llama este
    public void insertar(Cancion cancion) {
        raiz = insertarRec(raiz, cancion);
    }

    // Metodo recursivo que inserta y balancea
    private NodoAVL insertarRec(NodoAVL nodo, Cancion cancion) {

        // --- paso 1: insercion normal de BST ---
        if (nodo == null) return new NodoAVL(cancion);

        int cmp = cancion.getNombre().toLowerCase()
                .compareTo(nodo.cancion.getNombre().toLowerCase());

        if (cmp < 0) {
            nodo.izquierdo = insertarRec(nodo.izquierdo, cancion);
        } else if (cmp > 0) {
            nodo.derecho = insertarRec(nodo.derecho, cancion);
        } else {
            // nombre duplicado, no insertamos
            System.out.println("[AVL] Ya existe: " + cancion.getNombre());
            return nodo;
        }

        // --- paso 2: actualizar altura ---
        actualizarAltura(nodo);

        // --- paso 3: verificar balance y aplicar rotaciones ---
        int bal = factorBalance(nodo);

        // Caso LL: subarbol izquierdo pesado, nuevo nodo fue a la izquierda del hijo izq
        if (bal > 1 && cancion.getNombre().toLowerCase()
                .compareTo(nodo.izquierdo.cancion.getNombre().toLowerCase()) < 0)
            return rotarDerecha(nodo);

        // Caso RR: subarbol derecho pesado, nuevo nodo fue a la derecha del hijo der
        if (bal < -1 && cancion.getNombre().toLowerCase()
                .compareTo(nodo.derecho.cancion.getNombre().toLowerCase()) > 0)
            return rotarIzquierda(nodo);

        // Caso LR: subarbol izquierdo pesado, nuevo nodo fue a la derecha del hijo izq
        // -> doble rotacion: primero izq del hijo, luego der del nodo
        if (bal > 1 && cancion.getNombre().toLowerCase()
                .compareTo(nodo.izquierdo.cancion.getNombre().toLowerCase()) > 0) {
            nodo.izquierdo = rotarIzquierda(nodo.izquierdo);
            return rotarDerecha(nodo);
        }

        // Caso RL: subarbol derecho pesado, nuevo nodo fue a la izquierda del hijo der
        // -> doble rotacion: primero der del hijo, luego izq del nodo
        if (bal < -1 && cancion.getNombre().toLowerCase()
                .compareTo(nodo.derecho.cancion.getNombre().toLowerCase()) < 0) {
            nodo.derecho = rotarDerecha(nodo.derecho);
            return rotarIzquierda(nodo);
        }

        return nodo; // ya estaba balanceado
    }

    // =========================================================
    //  BUSQUEDA
    // =========================================================

    // Busca canciones cuyo nombre contenga el texto (parcial o completo)
    // Recorre todo el arbol porque la busqueda parcial no se puede optimizar con BST
    public List<Cancion> buscar(String texto) {
        List<Cancion> resultados = new ArrayList<>();
        buscarRec(raiz, texto.toLowerCase(), resultados);
        return resultados;
    }

    private void buscarRec(NodoAVL nodo, String texto, List<Cancion> resultados) {
        if (nodo == null) return;
        if (nodo.cancion.getNombre().toLowerCase().contains(texto))
            resultados.add(nodo.cancion);
        buscarRec(nodo.izquierdo, texto, resultados);
        buscarRec(nodo.derecho,   texto, resultados);
    }

    // =========================================================
    //  RECORRIDOS
    // =========================================================

    // Inorden: izquierdo -> raiz -> derecho
    // Resultado: canciones en orden alfabetico
    public List<Cancion> inorden() {
        List<Cancion> lista = new ArrayList<>();
        inordenRec(raiz, lista);
        return lista;
    }

    private void inordenRec(NodoAVL nodo, List<Cancion> lista) {
        if (nodo == null) return;
        inordenRec(nodo.izquierdo, lista);
        lista.add(nodo.cancion);
        inordenRec(nodo.derecho, lista);
    }

    // Postorden: izquierdo -> derecho -> raiz
    public List<Cancion> postorden() {
        List<Cancion> lista = new ArrayList<>();
        postordenRec(raiz, lista);
        return lista;
    }

    private void postordenRec(NodoAVL nodo, List<Cancion> lista) {
        if (nodo == null) return;
        postordenRec(nodo.izquierdo, lista);
        postordenRec(nodo.derecho, lista);
        lista.add(nodo.cancion);
    }

    // =========================================================
    //  GENERACION DE PLANTUML
    // =========================================================

    // Genera el codigo PlantUML para visualizar el arbol graficamente
    // Los nodos se colorean segun su nivel en el arbol
    public String generarPlantUML() {
    StringBuilder sb = new StringBuilder();
    sb.append("@startuml\n");
    sb.append("hide circle\n");
    sb.append("hide empty members\n");
    sb.append("skinparam backgroundColor #FAFAFA\n");
    sb.append("skinparam defaultFontName Arial\n");
    sb.append("skinparam defaultFontSize 12\n");
    sb.append("skinparam class {\n");
    sb.append("  BackgroundColor #D1C4F5\n");
    sb.append("  BorderColor #7C4DFF\n");
    sb.append("  FontColor #4527A0\n");
    sb.append("  BorderThickness 2\n");
    sb.append("  RoundCorner 10\n");
    sb.append("}\n");

    if (raiz == null) {
        sb.append("class \"Arbol vacio\" as vacio #FFF9C4\n");
    } else {
        plantUMLNodos(raiz, sb, 0);
        plantUMLConexiones(raiz, sb);
    }

    sb.append("@enduml\n");
    return sb.toString();
}

    private static final String[] FILL_COLORS = {"#D1C4F5", "#B2DFDB", "#FFCCBC", "#C8E6C9"};

    private void plantUMLNodos(NodoAVL nodo, StringBuilder sb, int nivel) {
        if (nodo == null) return;
        String fill   = FILL_COLORS[Math.min(nivel, FILL_COLORS.length - 1)];
        String nombre  = nodo.cancion.getNombre().replace("\"", "'");
        String artista = nodo.cancion.getArtista().replace("\"", "'");
        String idNodo  = "n" + nodo.cancion.getId();
        sb.append("class \"").append(nombre).append("\\n")
        .append(artista).append("\" as ").append(idNodo)
        .append(" ").append(fill).append("\n");
        plantUMLNodos(nodo.izquierdo, sb, nivel + 1);
        plantUMLNodos(nodo.derecho,   sb, nivel + 1);
    }

    private void plantUMLConexiones(NodoAVL nodo, StringBuilder sb) {
        if (nodo == null) return;
        String idNodo = "n" + nodo.cancion.getId();
        if (nodo.izquierdo != null) {
            sb.append(idNodo).append(" --> n")
            .append(nodo.izquierdo.cancion.getId()).append(" : izq\n");
            plantUMLConexiones(nodo.izquierdo, sb);
        }
        if (nodo.derecho != null) {
            sb.append(idNodo).append(" --> n")
            .append(nodo.derecho.cancion.getId()).append(" : der\n");
            plantUMLConexiones(nodo.derecho, sb);
        }
    }

    public boolean estaVacio() { return raiz == null; }
    public NodoAVL getRaiz()   { return raiz; }
}
