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

    @Override
    public AuthResponseDTO login(LoginDTO loginDTO) {
        // Buscar usuario por email
        User user = userRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Verificar contraseña cifrada
        if (passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            // Generar token si es correcta
            String token = jwtUtils.generateToken(user.getEmail());
            return new AuthResponseDTO(token);
        } else {
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
