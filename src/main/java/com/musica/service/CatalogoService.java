package com.musica.service;

import com.musica.avl.ArbolAVL;
import com.musica.avl.ListaDoble;
import com.musica.model.Cancion;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CatalogoService {

    private final ArbolAVL arbol;
    private final ListaDoble playlist;

    // Guardo los archivos en memoria porque en Render no hay disco persistente
    // La clave es el id de la cancion
    private final Map<Integer, byte[]> audios   = new HashMap<>();
    private final Map<Integer, byte[]> imagenes = new HashMap<>();

    public CatalogoService() {
        this.arbol    = new ArbolAVL();
        this.playlist = new ListaDoble();
        cargarEjemplos();
    }

    private void cargarEjemplos() {
        arbol.insertar(new Cancion(1, "Bohemian Rhapsody",      "Queen",        "A Night at the Opera", "https://i.imgur.com/1.jpg", ""));
        arbol.insertar(new Cancion(2, "Hotel California",       "Eagles",       "Hotel California",     "https://i.imgur.com/2.jpg", ""));
        arbol.insertar(new Cancion(3, "Stairway to Heaven",     "Led Zeppelin", "Led Zeppelin IV",      "https://i.imgur.com/3.jpg", ""));
        arbol.insertar(new Cancion(4, "Imagine",                "John Lennon",  "Imagine",              "https://i.imgur.com/4.jpg", ""));
        arbol.insertar(new Cancion(5, "Smells Like Teen Spirit","Nirvana",      "Nevermind",            "https://i.imgur.com/5.jpg", ""));
        arbol.insertar(new Cancion(6, "Like a Rolling Stone",   "Bob Dylan",    "Highway 61 Revisited", "https://i.imgur.com/6.jpg", ""));
        arbol.insertar(new Cancion(7, "Purple Haze",            "Jimi Hendrix", "Are You Experienced",  "https://i.imgur.com/7.jpg", ""));
        arbol.insertar(new Cancion(8, "Amar Sin Ser Amado",     "Jose Jose",    "Jose Jose",            "", ""));
        System.out.println("[AVL] Canciones de ejemplo cargadas.");
    }

    // --- API 1 ---

    public List<Cancion> buscarCanciones(String nombre) {
        if (nombre == null || nombre.isBlank()) return arbol.inorden();
        return arbol.buscar(nombre);
    }

    public List<Cancion> getInorden()   { return arbol.inorden(); }
    public List<Cancion> getPostorden() { return arbol.postorden(); }

    // --- API 2 ---

    public String getCodigoPlantUML() { return arbol.generarPlantUML(); }

    public String getUrlDiagrama() {
        try {
            byte[] comprimido = deflate(arbol.generarPlantUML().getBytes("UTF-8"));
            return "https://www.plantuml.com/plantuml/png/" + encode64PlantUML(comprimido);
        } catch (Exception e) {
            System.err.println("Error generando URL PlantUML: " + e.getMessage());
            return "";
        }
    }

    // PlantUML necesita Deflate sin header y su propio Base64 — no el estandar
    private byte[] deflate(byte[] data) throws Exception {
        java.util.zip.Deflater d = new java.util.zip.Deflater(java.util.zip.Deflater.BEST_COMPRESSION, true);
        d.setInput(data);
        d.finish();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        while (!d.finished()) { int n = d.deflate(buf); baos.write(buf, 0, n); }
        return baos.toByteArray();
    }

    private static final String TABLA = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz-_";

    private String encode64PlantUML(byte[] data) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < data.length) {
            int b0 = data[i++] & 0xFF;
            int b1 = i < data.length ? data[i++] & 0xFF : 0;
            int b2 = i < data.length ? data[i++] & 0xFF : 0;
            sb.append(TABLA.charAt((b0 >> 2) & 0x3F));
            sb.append(TABLA.charAt(((b0 & 0x3) << 4) | (b1 >> 4)));
            sb.append(TABLA.charAt(((b1 & 0xF) << 2) | (b2 >> 6)));
            sb.append(TABLA.charAt(b2 & 0x3F));
        }
        return sb.toString();
    }

    // --- API 3 ---

    public void agregarCancion(Cancion cancion) {
        if (cancion.getNombre() == null || cancion.getNombre().isBlank())
            throw new IllegalArgumentException("El nombre es obligatorio");
        if (cancion.getArtista() == null || cancion.getArtista().isBlank())
            throw new IllegalArgumentException("El artista es obligatorio");
        arbol.insertar(cancion);
        System.out.println("[AVL] Cancion agregada: " + cancion.getNombre());
    }

    // --- Audio e Imagen ---

    public void guardarAudio(int id, byte[] bytes) {
        audios.put(id, bytes);
        System.out.println("[Audio] Guardado para id=" + id + " (" + bytes.length + " bytes)");
    }

    public byte[] getAudio(int id) {
        return audios.get(id);
    }

    public void guardarImagen(int id, byte[] bytes) {
        imagenes.put(id, bytes);
        System.out.println("[Imagen] Guardada para id=" + id + " (" + bytes.length + " bytes)");
    }

    public byte[] getImagen(int id) {
        return imagenes.get(id);
    }

    // --- Playlist ---

    public void agregarAPlaylist(Cancion c) { playlist.agregar(c); }
    public List<Cancion> getPlaylist()       { return playlist.aLista(); }
    public String getPlantUMLPlaylist()      { return playlist.generarPlantUML(); }
}
