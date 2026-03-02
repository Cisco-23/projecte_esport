package com.company.projecte_esport.model;

/**
 *
 * @author Leomar
 */
import java.time.LocalDate;
import java.time.Period;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import java.util.Set;
import org.springframework.data.annotation.Transient;

@Document(collection = "users")
public class User {

    @Id
    private String id;
    private String name;
    @Indexed(unique = true)
    private String email;
    private String password;
    private LocalDate birthDate;
    private String gender;
    private Level level; // basic, medium, high 
    private Role role; // ROLE_USER, ROLE_ADMIN

    //  Constructores
    public User() {
    }

    public User(String name, String email, String password, LocalDate birthDate, String gender, Level level, Role role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.birthDate = birthDate;
        this.gender = gender;
        this.level = level;
        this.role = role;
    }
    
    @Transient 
    public int getAge() {
        if (this.birthDate != null) {
            return Period.between(this.birthDate, LocalDate.now()).getYears();
        }
        return 0;
    }


    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    @Override
    public String toString() {
        return "User{id='" + id + "', name='" + name + "', email='" + email + "'}";
    }
}
