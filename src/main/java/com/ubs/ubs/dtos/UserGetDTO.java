package com.ubs.ubs.dtos;

import com.ubs.ubs.entities.User;

import java.util.List;

public class UserGetDTO {
    private String name;
    private String email;


    public UserGetDTO(User entity) {
        name = entity.getName();
        email = entity.getEmail();
    }

    public UserGetDTO(){}

    public UserGetDTO(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

}
