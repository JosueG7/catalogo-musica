# Proyecto Programación III – Fase 1
## Catálogo Musical con Árbol AVL

---

## Requisitos

- **Java JDK 17 o superior** → https://adoptium.net
- **Maven** → https://maven.apache.org  
  *(o usa el wrapper `mvnw` que viene incluido en IntelliJ/VS Code)*

---

## Cómo correr el proyecto

### En VS Code
1. Instala la extensión **"Extension Pack for Java"**
2. Abre la carpeta del proyecto
3. Abre la terminal integrada (Ctrl + `)
4. Ejecuta:
```bash
mvn spring-boot:run
```

### Verificar que funciona
Abre el navegador en:
```
http://localhost:8080/api/canciones
```

---

## Endpoints

### API 1 – Catálogo / Búsqueda
```
GET  http://localhost:8080/api/canciones
GET  http://localhost:8080/api/canciones?nombre=hotel
GET  http://localhost:8080/api/canciones/inorden
GET  http://localhost:8080/api/canciones/postorden
```

### API 2 – Diagrama del AVL
```
GET  http://localhost:8080/api/avl/diagrama
```
La respuesta incluye `urlImagen` — ábrela en el navegador para ver el árbol graficado.

### API 3 – Agregar canción
```
POST http://localhost:8080/api/canciones
Content-Type: application/json

{
  "id": 101,
  "nombre": "Nombre de la cancion",
  "artista": "Nombre del artista",
  "album": "Nombre del album",
  "imagen": "https://url-de-imagen.jpg",
  "audio": "https://url-del-audio.mp3"
}
```

### Extras
```
POST http://localhost:8080/api/canciones/cargar?termino=queen
GET  http://localhost:8080/api/playlist
POST http://localhost:8080/api/playlist
```

---

## Estructura del proyecto

```
src/
└── main/
│   ├── java/com/musica/
│   │   ├── CatalogoMusicaApplication.java   ← clase main
│   │   ├── model/
│   │   │   └── Cancion.java                 ← modelo de datos
│   │   ├── avl/
│   │   │   ├── NodoAVL.java                 ← nodo del árbol
│   │   │   ├── ArbolAVL.java                ← árbol AVL (manual, sin librerías)
│   │   │   └── ListaDoble.java              ← lista doblemente enlazada
│   │   ├── service/
│   │   │   └── CatalogoService.java         ← lógica del negocio
│   │   └── controller/
│   │       └── CatalogoController.java      ← endpoints REST
│   └── resources/
│       └── application.properties
└── test/
    └── java/com/musica/
        └── ArbolAVLTest.java                ← pruebas del AVL
```

---

## Notas importantes

- Sin base de datos
- El árbol AVL fue implementado manualmente sin librerías externas 
- Clave de ordenamiento: **nombre de canción** (case-insensitive)
- Al iniciar se cargan 7 canciones de ejemplo automáticamente
- Timeout de 10s y 3 reintentos en conexión con API externa 
