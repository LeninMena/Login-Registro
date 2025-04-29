package com.example.demo;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;

//"Implementación de utilidades para la creación y validación de JWT (JSON Web Tokens) para manejo de sesiones."
public class JwtUtil {

    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256); // Llave secreta automática

    // Método para generar un token
    public static String generateToken(String username) {
        long expirationTimeMillis = 1000 * 60 * 60; // 1 hora
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTimeMillis))
                .signWith(SECRET_KEY)
                .compact();
    }

}
