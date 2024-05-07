package com.ubs.ubs.dtos;

import com.ubs.ubs.entities.Role;

public class RoleDTO {
    private Long id;
    private String name;

    public RoleDTO() {
    }

    public RoleDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public RoleDTO(Role role) {
        this.id = role.getId();
        this.name = role.getAuthority();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
