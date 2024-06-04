package com.ubs.ubs.dtos;

import com.ubs.ubs.entities.Address;

public class AddressGetDTO {
    private Long id;
    private String street;
    private Integer number;
    private String district;
    private String city;
    private String cep;


    public AddressGetDTO(Long id, String street, Integer number, String city, String district, String cep) {
        this.id = id;
        this.street = street;
        this.number = number;
        this.city = city;
        this.district = district;
        this.cep = cep;
    }

    public AddressGetDTO(Address entity){
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
