package com.ubs.ubs.dtos;

import com.ubs.ubs.dtos.utils.ValidationErrorMessages;
import com.ubs.ubs.entities.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.UniqueElements;

public class UserInsertDTO {
    private Long id;

    @NotBlank(message = ValidationErrorMessages.MANDATORY_FIELD)
    @Size(min = 3, message = ValidationErrorMessages.INVALID_NAME_LENGTH)
    private String name;
    @Email(message = ValidationErrorMessages.INVALID_EMAIL)
    @NotBlank(message = ValidationErrorMessages.MANDATORY_FIELD)
    private String email;

    @NotBlank(message = ValidationErrorMessages.MANDATORY_FIELD)
    private String password;


    public UserInsertDTO(User entity) {
        id = entity.getId();
        name = entity.getName();
        email = entity.getEmail();
        password = entity.getPassword();
    }

    public UserInsertDTO(){}

    public UserInsertDTO(Long id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
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

    public String getPassword() {
        return password;
    }
}
