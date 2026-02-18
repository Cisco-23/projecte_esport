/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.company.projecte_esport.security;

/**
 *
 * @author Jesus
 */
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtils {


        // CLAVE SECRETA: Debe ser la misma que usaste para generar el token
        private final String SECRET_KEY = "miSecretoSuperSeguroParaSquashProject";

        // 1. EXTRAER EL USUARIO (EMAIL) DEL TOKEN
        // Este método lo usará el filtro para saber quién intenta entrar
        public String extractUsername(String token) {
            return extractClaim(token, Claims::getSubject);
        }

        // 2. EXTRAER LA FECHA DE CADUCIDAD
        public Date extractExpiration(String token) {
            return extractClaim(token, Claims::getExpiration);
        }

        // 3. VALIDAR EL TOKEN
        // Comprueba si el token pertenece al usuario y si no ha caducado
        public Boolean validateToken(String token, String email) {
            final String username = extractUsername(token);
            return (username.equals(email) && !isTokenExpired(token));
        }

        // --- MÉTODOS AUXILIARES (PRIVADOS O DE APOYO) ---
        public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
            final Claims claims = extractAllClaims(token);
            return claimsResolver.apply(claims);
        }
        
        private javax.crypto.SecretKey getSigningKey() {
    byte[] keyBytes = java.nio.charset.StandardCharsets.UTF_8.encode(SECRET_KEY).array();
    return io.jsonwebtoken.security.Keys.hmacShaKeyFor(keyBytes);
}

        // Aquí es donde usamos la SECRET_KEY para leer el token. 
        // Si la firma no coincide, esto lanzará una excepción.
       private Claims extractAllClaims(String token) {
    return Jwts.parser()
            .verifyWith(getSigningKey()) // Usa la clave secreta preparada para 0.12.x
            .build()
            .parseSignedClaims(token)    // Reemplaza a parseClaimsJws
            .getPayload();               // Reemplaza a getBody()
}

        private Boolean isTokenExpired(String token) {
            return extractExpiration(token).before(new Date());
        }

        // --- EL MÉTODO QUE YA TENÍAS ---
        public String generateToken(String email) {
            // 1 día de duración (86400000 milisegundos)
            return Jwts.builder()
                    .setSubject(email)
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                    .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                    .compact();
        }
    }

