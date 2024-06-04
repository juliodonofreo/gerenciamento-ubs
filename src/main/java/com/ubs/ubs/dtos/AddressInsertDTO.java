package com.ubs.ubs.dtos;

import com.ubs.ubs.dtos.utils.ValidationErrorMessages;
import com.ubs.ubs.entities.Address;
import jakarta.validation.constraints.*;

public class AddressInsertDTO {
    private Long id;

    @NotBlank
    private String street;

    @NotNull
    @Min(value = 0, message = ValidationErrorMessages.INVALID_HOUSE_NUMBER)
    @Max(value = 999, message = ValidationErrorMessages.INVALID_HOUSE_NUMBER)
    private Integer number;

    @NotBlank
    private String district;

    @NotBlank
    private String city;

    @NotBlank
    @Pattern(regexp = "\\d{5}-\\d{3}", message = ValidationErrorMessages.INVALID_CEP)
    private String cep;


    public AddressInsertDTO(Long id, String street, Integer number, String city, String district, String cep) {
        this.id = id;
        this.street = street;
        this.number = number;
        this.city = city;
        this.district = district;
        this.cep = cep;
    }

    public AddressInsertDTO(Address entity){
        this.id = entity.getId();
        this.street = entity.getStreet();
        this.number = entity.getNumber();
        this.city = entity.getCity();
        this.district = entity.getDistrict();
        this.cep = entity.getCep();
    }

    public Long getId() {
        return id;
    }

    public String getStreet() {
        return street;
    }

    public Integer getNumber() {
        return number;
    }

    public String getDistrict() {
        return district;
    }

    public String getCity() {
        return city;
    }

    public String getCep() {
        return cep;
    }
}
