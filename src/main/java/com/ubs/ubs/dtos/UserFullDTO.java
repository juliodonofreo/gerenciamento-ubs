package com.ubs.ubs.dtos;

import com.ubs.ubs.entities.Doctor;
import com.ubs.ubs.entities.Patient;
import com.ubs.ubs.entities.User;
import com.ubs.ubs.entities.enums.Specialization;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class UserFullDTO {
    private Long id;
    private String name;
    private String email;
    private String cpf;
    private Instant birth_date;
    private Specialization specialization;
    private String crm;
    private String phone;
    private String address;


    private final List<String> roles = new ArrayList<>();


    public UserFullDTO(Long id, String name, String email, Instant birth_date, String cpf, Specialization specialization, String crm, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.birth_date = birth_date;
        this.cpf = cpf;
        this.crm = crm;
        this.phone = phone;
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
        phone = entity.getPhone();
        address = entity.getAddress();

        entity.getRoles().forEach((x) -> roles.add(x.getAuthority()));
    }

    public UserFullDTO(Doctor entity){
        id = entity.getId();
        name = entity.getName();
        email = entity.getEmail();
        specialization = entity.getSpecialization();
        crm = entity.getCrm();

        entity.getRoles().forEach((x) -> roles.add(x.getAuthority()));
    }

    public UserFullDTO(User entity) {
        id = entity.getId();
        name = entity.getName();
        email = entity.getEmail();

        entity.getRoles().forEach((x) -> roles.add(x.getAuthority()));
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

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getCrm() {
        return crm;
    }

    public int getAge() {
        if (birth_date == null) {
            return 0;
        }
        LocalDate birthDate = birth_date.atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate currentDate = LocalDate.now();
        return Period.between(birthDate, currentDate).getYears();
    }

    public Instant getBirth_date() {
        return birth_date;
    }

    public Specialization getSpecialization() {
        return specialization;
    }

    public List<String> getRoles() {
        return roles;
    }
}
