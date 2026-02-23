package com.company.projecte_esport.service;

import com.company.projecte_esport.dto.AuthResponseDTO;
import com.company.projecte_esport.dto.LoginDTO;
import com.company.projecte_esport.dto.UserDTO;
import com.company.projecte_esport.model.User;
import java.util.List;
import java.util.Optional;

public interface UserService {

    // En UserService.java
    AuthResponseDTO login(LoginDTO loginDTO);

    // Crear un nuevo usuario (Retornamos un DTO por seguridad)
    UserDTO create(User user);

    // Leer por ID Mongo DB 
    Optional<UserDTO> readById(String id);

    // Leer por email
    Optional<UserDTO> readByEmail(String email);

    // Actualizar perfil
    void update(User user);

    // Borrar el socio
    void delete(String id);

    // Lista de todos los socios
    List<UserDTO> getAll();
}
