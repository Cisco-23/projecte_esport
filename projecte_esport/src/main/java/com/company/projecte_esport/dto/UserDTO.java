package com.company.projecte_esport.dto;

/**
 *
 * @author Jesus
 */
import com.company.projecte_esport.model.Level;
import com.company.projecte_esport.model.Role;
import java.time.LocalDate;
import java.util.Set;

public class UserDTO {

    private String id;
    private String name;
    private String email;
    private int age;
    private LocalDate birthDate;
    private String gender;
    private Level level;
    private Role role;

    public UserDTO() {
    }

    public UserDTO(String id, String name, String email, int age, String gender, Level level, Role role, LocalDate birthDate) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
        this.gender = gender;
        this.level = level;
        this.role = role;
        this.birthDate = birthDate;
    }

    public UserDTO(String name, String email, int age, String gender, Level level) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.gender = gender;
        this.level = level;
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
