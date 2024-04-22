package com.ubs.ubs.dtos;

import com.ubs.ubs.entities.User;

import java.util.List;

public class UserGetDTO {
    private Long id;
    private String name;
    private String email;

    public UserGetDTO(User entity) {
        id = entity.getId();
        name = entity.getName();
        email = entity.getEmail();
    }

    public UserGetDTO(){}

    public UserGetDTO(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
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

    @Override
    public String toString() {
        return "UserGetDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
