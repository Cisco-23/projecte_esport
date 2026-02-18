package com.company.projecte_esport.serviceImpl;

/**
 *
 * @author Leomar
 */

import com.company.projecte_esport.dto.AuthResponseDTO;
import com.company.projecte_esport.dto.LoginDTO;
import com.company.projecte_esport.dto.UserDTO;
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
            // 3. Generar token si es correcta
            String token = jwtUtils.generateToken(user.getEmail());
            return new AuthResponseDTO(token);
        } else {
            throw new RuntimeException("Credenciales incorrectas");
        }
    }

    @Override
    public UserDTO create(User user) {
        // Cifrar la contraseña antes de guardar (Requisito de seguridad)
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
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
        // Verificar si existe antes de actualizar
        if (userRepository.existsById(user.getId())) {
            // Si el usuario cambia la contraseña se vuelve a cifrar
            // Se actualiza el perfil
            userRepository.save(user);
        }
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
            user.getName(), 
            user.getEmail(), 
            user.getAge(), 
            user.getGender(), 
            user.getLevel()
        );
    }
}
