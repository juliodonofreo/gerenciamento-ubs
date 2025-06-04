package com.ubs.ubs.dtos;

import com.ubs.ubs.entities.Staff;

public class StaffUpdateDTO extends UserUpdateDTO{

    private String type;

    public StaffUpdateDTO(Staff entity) {
        super(entity);
        this.type = entity.getType();
    }

    public StaffUpdateDTO(String name, String email, String password, String type) {
        super(name, email, password);
        this.type = type;
    }

    public String getType() {
        return type;
    }
}