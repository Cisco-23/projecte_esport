package com.company.projecte_esport.repository;
/**
 *
 * @author Leomar
 */ 

import com.company.projecte_esport.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

// Repositorio para la gestion de usuarios en MongoDB
public interface UserRepository extends MongoRepository<User, String> {
    // Buscar un usuario por su email (necesario para seguridad/login)
    Optional<User> findByEmail(String email);
}