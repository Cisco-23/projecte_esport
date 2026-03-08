package com.company.projecte_esport.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        // ignoramos la ruta del swagger
        return path.startsWith("/api/auth/") || 
               path.startsWith("/swagger-ui/") || 
               path.startsWith("/v3/api-docs/");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        
        String path = request.getRequestURI();
    
    // Si la ruta es publica (Login, Registro o Swagger), la dejamos pasar
    
    // Dejamos pasar el Login, Swagger, la documentación y los archivos web de la interfaz
    // Si la ruta CONTIENE alguna de estas palabras, la dejamos pasar sin pedir token
    if (path.contains("/api/auth") || path.contains("/swagger-ui") || path.contains("/v3/api-docs")) {
        chain.doFilter(request, response);
        return; // Detenemos el filtro aquí
    }

        System.out.println("=== NUEVA PETICION A: " + request.getRequestURI() + " ===");

        final String authHeader = request.getHeader("Authorization");
        String username = null;
        String jwt = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            System.out.println("1. Token recibido: " + jwt.substring(0, 15) + "...");
            
            try {
                username = jwtUtils.extractUsername(jwt);
                System.out.println("2. Usuario cogido del token: " + username);
            } catch (Exception e) {
                System.out.println("ERROR AL EXTRAER USUARIO: " + e.getMessage());
            }
        } else {
            System.out.println("AVISO: No hay token o no empieza por 'Bearer '");
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            System.out.println("3. Buscando usuario en la base de datos...");
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            System.out.println("4. Usuario encontrado en BD. Roles: " + userDetails.getAuthorities());

         
            boolean isTokenValid = false;
            try {
     
                isTokenValid = jwtUtils.validateToken(jwt, userDetails.getUsername()); 
            } catch (Exception e) {
                System.out.println("ERROR AL VALIDAR TOKEN: " + e.getMessage());
            }

            System.out.println("5. ¿Es el token valido?: " + isTokenValid);

            if (isTokenValid) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                System.out.println("6. Acceso concedido! Dejando pasar a " + username);
            } else {
                System.out.println("ERROR: El token no es valido o ha caducado");
            }
        }
        System.out.println("==========================================");
        chain.doFilter(request, response);
    }
}