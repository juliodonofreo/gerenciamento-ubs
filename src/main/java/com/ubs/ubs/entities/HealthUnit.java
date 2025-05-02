package com.ubs.ubs.entities;

import com.ubs.ubs.entities.User;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("UNIT")
public class HealthUnit extends User {

    @NotBlank
    @Pattern(regexp = "\\d{10,11}")
    private String phone;

    @NotBlank
    @Size(max = 200)
    private String address;

    @OneToMany(mappedBy = "healthUnit")
    private List<Staff> staff = new ArrayList<>();

    public HealthUnit() {
    }

    public HealthUnit(Long id, String name, String email, String password, String phone, String address) {
        super(id, name, email, password);
        this.phone = phone;
        this.address = address;
    }



    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public List<Staff> getStaff() {
        return staff;
    }
}