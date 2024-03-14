package com.ubs.ubs.dtos;

import com.ubs.ubs.entities.User;
import jakarta.validation.constraints.Email;

public class UserInsertDTO {
    private Long id;
    private String name;
    @Email(message = "Please enter a valid email")
    private String email;

    private String password;


    public UserInsertDTO(User entity) {
        id = entity.getId();
        name = entity.getName();
        email = entity.getEmail();
        password = entity.getPassword();
    }

    public UserInsertDTO(){}

    public UserInsertDTO(Long id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
