package com.company.projecte_esport.controller;

import com.company.projecte_esport.dto.AuthResponseDTO;
import com.company.projecte_esport.dto.LoginDTO;
import com.company.projecte_esport.dto.UserDTO;
import com.company.projecte_esport.model.User;
import com.company.projecte_esport.repository.UserRepository;
import com.company.projecte_esport.security.JwtUtils;
import com.company.projecte_esport.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtils jwtUtils;

    // ===== ENDPOINTS ORIGINALES =====
    
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            System.out.println("📝 Registrando usuario: " + user.getEmail());
            UserDTO registeredUser = userService.create(user);
            System.out.println("✅ Usuario registrado exitosamente");
            return ResponseEntity.ok(registeredUser);
        } catch (Exception e) {
            System.err.println("❌ Error en registro: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error al registrar: " + e.getMessage());
        }
    }

    // controller/AuthController.java
@PostMapping("/login")
public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
    try {
        System.out.println("🔑 Login: " + loginDTO.getEmail());
        
        // Llamar al servicio
        AuthResponseDTO response = userService.login(loginDTO);
        
        System.out.println("✅ Login exitoso. Devolviendo respuesta...");
        System.out.println("   Token: " + response.getToken().substring(0, 30) + "...");
        System.out.println("   Email: " + response.getEmail());
        System.out.println("   Name: " + response.getName());
        System.out.println("   Role: " + response.getRole());
        
        return ResponseEntity.ok(response);
        
    } catch (RuntimeException e) {
        System.err.println("❌ Error login: " + e.getMessage());
        return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
        System.err.println("💥 Error inesperado:");
        e.printStackTrace();
        return ResponseEntity.status(500).body("Error interno");
    }
}

    // ===== ENDPOINTS DE PRUEBA =====
    
    @GetMapping("/health")
    public ResponseEntity<?> healthCheck() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            System.out.println("🏥 HEALTH CHECK INICIADO...");
            
            // 1. Verificar conexión a BD
            long userCount = userRepository.count();
            response.put("database", "OK - Users: " + userCount);
            System.out.println("✅ BD conectada. Usuarios: " + userCount);
            
            // 2. Verificar passwordEncoder
            String testPassword = "test123";
            String testHash = passwordEncoder.encode(testPassword);
            boolean testMatch = passwordEncoder.matches(testPassword, testHash);
            response.put("passwordEncoder", "OK - Match: " + testMatch);
            System.out.println("✅ PasswordEncoder funciona");
            
            // 3. Verificar JWT
            String testToken = jwtUtils.generateToken("test@test.com");
            response.put("jwt", "OK - Token: " + testToken.substring(0, 20) + "...");
            System.out.println("✅ JWT funciona");
            
            // 4. Verificar usuario concreto
            Optional<User> testUser = userRepository.findByEmail("juan.garcia@test.com");
            if (testUser.isPresent()) {
                response.put("testUser", "Existe - " + testUser.get().getEmail());
                System.out.println("✅ Usuario de prueba encontrado");
            } else {
                response.put("testUser", "NO ENCONTRADO");
                System.out.println("⚠️ Usuario de prueba NO encontrado");
                
                // Listar todos los usuarios
                java.util.List<User> allUsers = userRepository.findAll();
                response.put("allUsers", allUsers.size());
                System.out.println("   Usuarios totales en BD: " + allUsers.size());
                for (User u : allUsers) {
                    System.out.println("   - " + u.getEmail());
                }
            }
            
            response.put("status", "SUCCESS");
            System.out.println("✅ HEALTH CHECK COMPLETADO");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("❌ HEALTH CHECK FALLIDO:");
            e.printStackTrace();
            
            response.put("status", "ERROR");
            response.put("error", e.getMessage());
            response.put("errorType", e.getClass().getName());
            
            return ResponseEntity.status(500).body(response);
        }
    }
    
    @GetMapping("/test-login-simple")
    public ResponseEntity<?> testLoginSimple(
            @RequestParam String email, 
            @RequestParam String password) {
        try {
            System.out.println("🧪 TEST LOGIN SIMPLE");
            System.out.println("Email: " + email);
            
            // Paso 1: Buscar usuario
            Optional<User> userOpt = userRepository.findByEmail(email);
            
            if (userOpt.isEmpty()) {
                System.out.println("❌ Usuario no encontrado");
                return ResponseEntity.badRequest().body("Usuario no encontrado: " + email);
            }
            
            User user = userOpt.get();
            System.out.println("✅ Usuario encontrado: " + user.getEmail());
            System.out.println("   Password hash: " + user.getPassword().substring(0, 20) + "...");
            
            // Paso 2: Verificar contraseña
            boolean passwordOk = passwordEncoder.matches(password, user.getPassword());
            System.out.println("   ¿Password correcto?: " + passwordOk);
            
            if (!passwordOk) {
                return ResponseEntity.badRequest().body("Contraseña incorrecta");
            }
            
            // Paso 3: Generar token
            String token = jwtUtils.generateToken(user.getEmail());
            System.out.println("✅ Token generado: " + token.substring(0, 30) + "...");
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("token", token);
            result.put("email", user.getEmail());
            result.put("name", user.getName());
            
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            System.err.println("💥 ERROR EN TEST LOGIN:");
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
}