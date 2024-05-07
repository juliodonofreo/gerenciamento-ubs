package com.ubs.ubs.dtos;

import com.ubs.ubs.entities.Doctor;
import com.ubs.ubs.entities.Patient;
import com.ubs.ubs.entities.enums.Specialization;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class UserFullDTO {
    private Long id;
    private String name;
    private String email;
    private String cpf;
    private Instant birth_date;
    private Specialization specialization;

    private final List<RoleDTO> roles = new ArrayList<>();


    public UserFullDTO(Long id, String name, String email, Instant birth_date, String cpf, Specialization specialization) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.birth_date = birth_date;
        this.cpf = cpf;
    }

    public UserFullDTO(Long id, String name, String email, Specialization specialization){
        this.id = id;
        this.name = name;
        this.email = email;
        this.specialization = specialization;
    }

    public UserFullDTO(Patient entity){
        id = entity.getId();
        name = entity.getName();
        email = entity.getEmail();
        birth_date = entity.getBirth_date();
        cpf = entity.getCpf();

        entity.getRoles().forEach((x) -> roles.add(new RoleDTO(x)));
    }

    public UserFullDTO(Doctor entity){
        id = entity.getId();
        name = entity.getName();
        email = entity.getEmail();
        specialization = entity.getSpecialization();

        entity.getRoles().forEach((x) -> roles.add(new RoleDTO(x)));
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

    public String getCpf() {
        return cpf;
    }

    public Instant getBirth_date() {
        return birth_date;
    }

    public Specialization getSpecialization() {
        return specialization;
    }

    public List<RoleDTO> getRoles() {
        return roles;
    }
}
