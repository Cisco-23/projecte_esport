package com.company.projecte_esport.dto;

/**
 *
 * @author Jesus
 */
import java.util.Set;

public class UserDTO {

    private String id;
    private String name;
    private String email;
    private int age;
    private String gender;
    private String level;
    private Set<String> roles;

    public UserDTO() {
    }

    public UserDTO(String id, String name, String email, int age, String gender, String level, Set<String> roles) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
        this.gender = gender;
        this.level = level;
        this.roles = roles;
    }

    public UserDTO(String name, String email, int age, String gender, String level) {
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

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}
