package com.company.projecte_esport.service;

import com.company.projecte_esport.dto.UserDTO;
import com.company.projecte_esport.model.User;
import java.util.List;
import java.util.Optional;

public interface UserService {

    // En UserService.java
    AuthResponseDTO login(LoginDTO loginDTO);

    // Crear un nou usuari (Retornem DTO per seguretat)
    UserDTO create(User user);

    // Llegir per ID (MongoDB usa String)
    Optional<UserDTO> readById(String id);

    // Llegir per Email 
    Optional<UserDTO> readByEmail(String email);

    // Actualitzar perfil
    void update(User user);

    // Esborrar soci
    void delete(String id);

    // Llistar tots els socis
    List<UserDTO> getAll();
}
