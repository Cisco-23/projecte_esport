package com.company.projecte_esport.controller;

import com.company.projecte_esport.dto.AuthResponseDTO;
import com.company.projecte_esport.dto.LoginDTO;
import com.company.projecte_esport.dto.UserDTO;
import com.company.projecte_esport.model.User;
import com.company.projecte_esport.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody User user) {
        return ResponseEntity.ok(userService.create(user));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginDTO loginDTO) {
        return ResponseEntity.ok(userService.login(loginDTO));
    }
}