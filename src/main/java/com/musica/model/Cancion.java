package com.musica.model;

/*
 * Clase que representa una cancion en el sistema
 * Contiene los campos minimos que pide el proyecto
 */
public class Cancion {

    private int id;
    private String nombre;
    private String artista;
    private String album;
    private String imagen;   // URL o referencia de la portada
    private String audio;    // URL o ruta del archivo de audio

    // Constructor vacio - necesario para que Spring deserialice el JSON del POST
    public Cancion() {}

    // Constructor completo para crear canciones de ejemplo al iniciar
    public Cancion(int id, String nombre, String artista,
                   String album, String imagen, String audio) {
        this.id      = id;
        this.nombre  = nombre;
        this.artista = artista;
        this.album   = album;
        this.imagen  = imagen;
        this.audio   = audio;
    }

    // --- Getters y Setters ---
    // Spring los necesita para convertir automaticamente entre JSON y objeto

    public int getId()              { return id; }
    public void setId(int id)       { this.id = id; }

    public String getNombre()           { return nombre; }
    public void setNombre(String n)     { this.nombre = n; }

    public String getArtista()          { return artista; }
    public void setArtista(String a)    { this.artista = a; }

    public String getAlbum()            { return album; }
    public void setAlbum(String a)      { this.album = a; }

    public String getImagen()           { return imagen; }
    public void setImagen(String i)     { this.imagen = i; }

    public String getAudio()            { return audio; }
    public void setAudio(String a)      { this.audio = a; }

    @Override
    public String toString() {
        return "Cancion{id=" + id + ", nombre='" + nombre + "', artista='" + artista + "'}";
    }
}
