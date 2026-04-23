package com.musica;

import com.musica.avl.ArbolAVL;
import com.musica.model.Cancion;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/*
 * Pruebas basicas del arbol AVL
 * Para correr: mvn test
 */
class ArbolAVLTest {

    @Test
    void testInsertarYBuscar() {
        ArbolAVL arbol = new ArbolAVL();
        arbol.insertar(new Cancion(1, "Imagine",          "John Lennon", "Imagine",    "", ""));
        arbol.insertar(new Cancion(2, "Bohemian Rhapsody","Queen",       "Opera",      "", ""));
        arbol.insertar(new Cancion(3, "Hotel California", "Eagles",      "Hotel Cal.", "", ""));

        List<Cancion> resultado = arbol.buscar("hotel");
        assertEquals(1, resultado.size());
        assertEquals("Hotel California", resultado.get(0).getNombre());
    }

    @Test
    void testInordenOrdenAlfabetico() {
        ArbolAVL arbol = new ArbolAVL();
        arbol.insertar(new Cancion(1, "Zebra Song", "A", "A", "", ""));
        arbol.insertar(new Cancion(2, "Apple Song", "B", "B", "", ""));
        arbol.insertar(new Cancion(3, "Mango Song", "C", "C", "", ""));

        List<Cancion> inorden = arbol.inorden();
        // El inorden de un AVL ordenado por nombre debe dar orden alfabetico
        assertEquals("Apple Song",  inorden.get(0).getNombre());
        assertEquals("Mango Song",  inorden.get(1).getNombre());
        assertEquals("Zebra Song",  inorden.get(2).getNombre());
    }

    @Test
    void testBusquedaParcial() {
        ArbolAVL arbol = new ArbolAVL();
        arbol.insertar(new Cancion(1, "Love Story",       "Taylor Swift", "Fearless", "", ""));
        arbol.insertar(new Cancion(2, "I Will Always Love", "Dolly",      "Album",    "", ""));
        arbol.insertar(new Cancion(3, "Purple Haze",      "Hendrix",      "Are You",  "", ""));

        // Buscar "love" debe encontrar las dos primeras
        List<Cancion> resultado = arbol.buscar("love");
        assertEquals(2, resultado.size());
    }
}
