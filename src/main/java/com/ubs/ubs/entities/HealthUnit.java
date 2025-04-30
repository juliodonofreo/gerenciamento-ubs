package com.ubs.ubs.entities;

import com.ubs.ubs.entities.User;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@DiscriminatorValue("UNIT")
public class HealthUnit extends User {

    @NotBlank
    @Pattern(regexp = "\\d{10,11}")
    private String phone;

    @NotBlank
    @Size(max = 200)
    private String address;

    public HealthUnit() {
    }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}