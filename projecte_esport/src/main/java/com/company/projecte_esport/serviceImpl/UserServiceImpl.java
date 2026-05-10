package com.company.projecte_esport.serviceImpl;

/**
 *
 * @author Leomar
 */
import com.company.projecte_esport.dto.AuthResponseDTO;
import com.company.projecte_esport.dto.LoginDTO;
import com.company.projecte_esport.dto.UserDTO;
import com.company.projecte_esport.model.Role;
import com.company.projecte_esport.model.User;
import com.company.projecte_esport.repository.UserRepository;
import com.company.projecte_esport.service.UserService;
import com.company.projecte_esport.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

// serviceImpl/UserServiceImpl.java
@Override
public AuthResponseDTO login(LoginDTO loginDTO) {
    System.out.println("📥 UserServiceImpl.login() - Inicio");
    System.out.println("   Buscando usuario con email: " + loginDTO.getEmail());
    
    // Buscar usuario por email
    User user = userRepository.findByEmail(loginDTO.getEmail())
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + loginDTO.getEmail()));

    System.out.println("✅ Usuario encontrado: " + user.getEmail());
    System.out.println("   ID: " + user.getId());
    System.out.println("   Nombre: " + user.getName());
    System.out.println("   Rol: " + user.getRole());

    // Verificar contraseña
    if (passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
        System.out.println("✅ Contraseña correcta, generando token...");
        
        // Generar token
        String token = jwtUtils.generateToken(user.getEmail());
        System.out.println("✅ Token generado: " + token.substring(0, 30) + "...");
        
        // IMPORTANTE: Usar el constructor COMPLETO con todos los campos
        AuthResponseDTO response = new AuthResponseDTO(
            token,
            user.getEmail(),
            user.getName(),
            user.getRole().name()  // Convertir enum a String
        );
        
        System.out.println("✅ Login completado exitosamente");
        return response;
    } else {
        System.out.println("❌ Contraseña incorrecta");
        throw new RuntimeException("Credenciales incorrectas");
    }
}

    @Override
    public UserDTO create(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (user.getRole() == null) {
            user.setRole(Role.USER);
        }

        User savedUser = userRepository.save(user);
        return mapToDTO(savedUser);
    }

    @Override
    public Optional<UserDTO> readById(String id) {
        return userRepository.findById(id).map(this::mapToDTO);
    }

    @Override
    public Optional<UserDTO> readByEmail(String email) {
        return userRepository.findByEmail(email).map(this::mapToDTO);
    }

    @Override
    public void update(User user) {
        User existingUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        if (user.getName() != null) {
            existingUser.setName(user.getName());
        }
        
        if (user.getBirthDate() != null) {
            existingUser.setBirthDate(user.getBirthDate());
        }
        
        if (user.getGender() != null) {
            existingUser.setGender(user.getGender());
        }
        
        // Usamos el Enum Level
        if (user.getLevel() != null) {
            existingUser.setLevel(user.getLevel());
        }

        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        
        userRepository.save(existingUser);
    }

    @Override
    public void delete(String id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<UserDTO> getAll() {
        return userRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Metodo de apoyo para convertir de Modelo a DTO
    private UserDTO mapToDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getAge(),
                user.getGender(),
                user.getLevel(), // Ahora pasa el enum Level
                user.getRole(), // Ahora pasa el enum Role
                user.getBirthDate()
        );
    }
}
