package com.musica;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
 * Clase principal del proyecto
 * Para correr: mvn spring-boot:run
 * El servidor queda en http://localhost:8080
 */
@SpringBootApplication
public class CatalogoMusicaApplication {

    public static void main(String[] args) {
        SpringApplication.run(CatalogoMusicaApplication.class, args);

        System.out.println("\n==========================================");
        System.out.println("  Servidor corriendo!");
        System.out.println("  http://localhost:8080");
        System.out.println("==========================================");
        System.out.println("  GET  /api/canciones");
        System.out.println("  GET  /api/canciones?nombre=xxx");
        System.out.println("  GET  /api/canciones/inorden");
        System.out.println("  GET  /api/canciones/postorden");
        System.out.println("  POST /api/canciones");
        System.out.println("  GET  /api/avl/diagrama");
        System.out.println("==========================================\n");
    }
}
