package com.ubs.ubs.dtos;

import com.ubs.ubs.entities.Role;

public class FuncaoDTO {
    private Long id;
    private String authority;

    public FuncaoDTO(Role entity) {
        id = entity.getId();
        authority = entity.getAuthority();
    }

    public FuncaoDTO() {}

    public FuncaoDTO(Long id, String authority) {
        this.id = id;
        this.authority = authority;
    }

    public Long getId() {
        return id;
    }

    public String getAuthority() {
        return authority;
    }
}
