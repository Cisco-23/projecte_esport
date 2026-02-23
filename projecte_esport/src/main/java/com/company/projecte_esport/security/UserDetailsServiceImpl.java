package com.company.projecte_esport.security;

import com.company.projecte_esport.model.User;
import com.company.projecte_esport.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

/**
 * ESTA ES LA CLASE QUE ESTÁS PIDIENDO. 
 * Implementa la interfaz UserDetailsService de Spring Security.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 1. Buscamos el usuario en el  MongoDB Atlas usando el email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));

        // 2. Creamos el objeto User de Spring Security
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword()) 
                .authorities(user.getRoles().stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList()))
                .build();
    }
}