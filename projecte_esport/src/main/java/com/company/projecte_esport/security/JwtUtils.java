package com.company.projecte_esport.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtils {

    // 1. CLAVE SECRETA FIJA
    private static final String SECRET_KEY_STRING = "EstaEsUnaClaveSuperSecretaYLoSuficientementeLargaParaJJWT1234567890=";

    // 2. OBTENER CLAVE FIRMADA
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY_STRING.getBytes());
    }

    // 3. Extraer el email (username)
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // 4. Validar el token
    public boolean validateToken(String token, String userDetailsUsername) {
        final String username = extractUsername(token);
        return (username.equals(userDetailsUsername) && !isTokenExpired(token));
    }

    // 5. Generar el token (cuando haces Login)
    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // Caduca en 10 horas
                .signWith(getSigningKey()) // FIRMAMOS CON LA CLAVE FIJA
                .compact();
    }

    // Métodos auxiliares
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey()) // LEEMOS CON LA MISMA CLAVE FIJA
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}