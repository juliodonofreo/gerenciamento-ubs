package com.ubs.ubs.dtos;

import com.ubs.ubs.entities.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public class UserUpdateDTO {

    @Size(min = 3, message = "O nome deve ter pelo menos 3 caracteres.")
    private String name;

    @Email(message = "Email inválido.")
    private String email;

    private String password;

    public UserUpdateDTO(User entity) {
        name = entity.getName();
        email = entity.getEmail();
        password = entity.getPassword();
    }

    public UserUpdateDTO(){}

    public UserUpdateDTO(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
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
