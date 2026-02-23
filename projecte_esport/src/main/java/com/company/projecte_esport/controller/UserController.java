package com.company.projecte_esport.controller;

import com.company.projecte_esport.dto.UserDTO;
import com.company.projecte_esport.model.User;
import com.company.projecte_esport.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable String id) {
        return userService.readById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable String id, @RequestBody User user) {
        user.setId(id);
        userService.update(user);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}