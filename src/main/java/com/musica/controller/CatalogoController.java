package com.musica.controller;

import com.musica.model.Cancion;
import com.musica.service.CatalogoService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class CatalogoController {

    private final CatalogoService catalogoService;

    public CatalogoController(CatalogoService catalogoService) {
        this.catalogoService = catalogoService;
    }

    // -------------------------------------------------------
    // API 1 — busqueda en el AVL
    // -------------------------------------------------------

    // Sin parametro devuelve todo en orden alfabetico
    // Con ?nombre=xxx hace busqueda parcial en el arbol
    @GetMapping("/canciones")
    public ResponseEntity<Map<String, Object>> getCanciones(
            @RequestParam(required = false) String nombre) {

        Map<String, Object> resp = new HashMap<>();
        try {
            List<Cancion> canciones = catalogoService.buscarCanciones(nombre);
            resp.put("exito",     true);
            resp.put("total",     canciones.size());
            resp.put("canciones", canciones);
            if (nombre != null && !nombre.isBlank()) {
                resp.put("busqueda", nombre);
                resp.put("mensaje",  "Resultados para: " + nombre);
            } else {
                resp.put("mensaje", "Catalogo completo");
            }
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            resp.put("exito", false);
            resp.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp);
        }
    }

    @GetMapping("/canciones/inorden")
    public ResponseEntity<Map<String, Object>> getInorden() {
        Map<String, Object> r = new HashMap<>();
        List<Cancion> lista   = catalogoService.getInorden();
        r.put("tipo", "inorden"); r.put("total", lista.size()); r.put("canciones", lista);
        return ResponseEntity.ok(r);
    }

    @GetMapping("/canciones/postorden")
    public ResponseEntity<Map<String, Object>> getPostorden() {
        Map<String, Object> r = new HashMap<>();
        List<Cancion> lista   = catalogoService.getPostorden();
        r.put("tipo", "postorden"); r.put("total", lista.size()); r.put("canciones", lista);
        return ResponseEntity.ok(r);
    }

    // -------------------------------------------------------
    // API 2 — diagrama del AVL
    // -------------------------------------------------------

    // Devuelve el codigo PlantUML y la urlImagen lista para abrir en el navegador
    @GetMapping("/avl/diagrama")
    public ResponseEntity<Map<String, Object>> getDiagrama() {
        Map<String, Object> resp = new HashMap<>();
        try {
            resp.put("exito",          true);
            resp.put("codigoPlantUML", catalogoService.getCodigoPlantUML());
            resp.put("urlImagen",      catalogoService.getUrlDiagrama());
            resp.put("mensaje",        "Abre urlImagen en el navegador para ver el diagrama");
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            resp.put("exito", false);
            resp.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp);
        }
    }

    // -------------------------------------------------------
    // API 3 — agregar cancion
    // -------------------------------------------------------

    @PostMapping("/canciones")
    public ResponseEntity<Map<String, Object>> agregarCancion(@RequestBody Cancion cancion) {
        Map<String, Object> resp = new HashMap<>();
        try {
            catalogoService.agregarCancion(cancion);
            resp.put("exito",   true);
            resp.put("mensaje", "Cancion '" + cancion.getNombre() + "' agregada");
            resp.put("cancion", cancion);
            return ResponseEntity.status(HttpStatus.CREATED).body(resp);
        } catch (IllegalArgumentException e) {
            resp.put("exito", false);
            resp.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);
        } catch (Exception e) {
            resp.put("exito", false);
            resp.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp);
        }
    }

    // -------------------------------------------------------
    // Audio — subir y descargar MP3
    // -------------------------------------------------------

    // POST /api/canciones/audio/{id}
    // En Postman: Body -> form-data -> key=archivo, type=File, value=tu .mp3
    @PostMapping("/canciones/audio/{id}")
    public ResponseEntity<Map<String, Object>> subirAudio(
            @PathVariable int id,
            @RequestParam("archivo") MultipartFile archivo) {

        Map<String, Object> resp = new HashMap<>();
        try {
            if (archivo.isEmpty()) {
                resp.put("exito", false);
                resp.put("error", "El archivo esta vacio");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);
            }
            catalogoService.guardarAudio(id, archivo.getBytes());
            resp.put("exito",   true);
            resp.put("mensaje", "Audio subido para id=" + id);
            resp.put("tamanio", archivo.getBytes().length + " bytes");
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            resp.put("exito", false);
            resp.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp);
        }
    }

    // GET /api/canciones/audio/{id}
    // El cliente recibe el MP3 directo y lo puede reproducir o guardar
    @GetMapping("/canciones/audio/{id}")
    public ResponseEntity<byte[]> descargarAudio(@PathVariable int id) {
        byte[] audio = catalogoService.getAudio(id);
        if (audio == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("audio/mpeg"));
        headers.setContentDispositionFormData("attachment", id + ".mp3");
        headers.setContentLength(audio.length);
        return ResponseEntity.ok().headers(headers).body(audio);
    }

    // -------------------------------------------------------
    // Imagen — subir y descargar
    // -------------------------------------------------------

    // POST /api/canciones/imagen/{id}
    // En Postman: Body -> form-data -> key=archivo, type=File, value=tu .jpg
    @PostMapping("/canciones/imagen/{id}")
    public ResponseEntity<Map<String, Object>> subirImagen(
            @PathVariable int id,
            @RequestParam("archivo") MultipartFile archivo) {

        Map<String, Object> resp = new HashMap<>();
        try {
            if (archivo.isEmpty()) {
                resp.put("exito", false);
                resp.put("error", "El archivo esta vacio");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);
            }
            catalogoService.guardarImagen(id, archivo.getBytes());
            resp.put("exito",   true);
            resp.put("mensaje", "Imagen subida para id=" + id);
            resp.put("tamanio", archivo.getBytes().length + " bytes");
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            resp.put("exito", false);
            resp.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp);
        }
    }

    // GET /api/canciones/imagen/{id}
    // Devuelve la imagen directo, se puede ver en el navegador o descargar
    @GetMapping("/canciones/imagen/{id}")
    public ResponseEntity<byte[]> descargarImagen(@PathVariable int id) {
        byte[] imagen = catalogoService.getImagen(id);
        if (imagen == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.setContentLength(imagen.length);
        return ResponseEntity.ok().headers(headers).body(imagen);
    }

    // -------------------------------------------------------
    // Playlist
    // -------------------------------------------------------

    @GetMapping("/playlist")
    public ResponseEntity<Map<String, Object>> getPlaylist() {
        Map<String, Object> r = new HashMap<>();
        r.put("canciones", catalogoService.getPlaylist());
        r.put("total",     catalogoService.getPlaylist().size());
        return ResponseEntity.ok(r);
    }

    @PostMapping("/playlist")
    public ResponseEntity<Map<String, Object>> agregarAPlaylist(@RequestBody Cancion cancion) {
        Map<String, Object> r = new HashMap<>();
        catalogoService.agregarAPlaylist(cancion);
        r.put("exito", true); r.put("mensaje", "Cancion agregada a la playlist");
        return ResponseEntity.ok(r);
    }
}
