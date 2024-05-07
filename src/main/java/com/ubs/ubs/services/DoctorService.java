package com.ubs.ubs.services;

import com.ubs.ubs.dtos.DoctorGetDTO;
import com.ubs.ubs.dtos.DoctorInsertDTO;
import com.ubs.ubs.entities.Doctor;
import com.ubs.ubs.entities.Patient;
import com.ubs.ubs.entities.Role;
import com.ubs.ubs.entities.User;
import com.ubs.ubs.repositories.DoctorRepository;
import com.ubs.ubs.repositories.RoleRepository;
import com.ubs.ubs.repositories.UserRepository;
import com.ubs.ubs.services.exceptions.CustomNotFoundException;
import com.ubs.ubs.services.exceptions.CustomRepeatedException;
import com.ubs.ubs.services.exceptions.ForbiddenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DoctorService {
    @Autowired
    private DoctorRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    private final String EMAIL_ALREADY_EXISTS = "Email já existente.";
    private final String USER_NOT_FOUND = "Usuário não encontrado.";
    private final String USER_IS_NOT_DOCTOR = "Usuário não é um médico.";

    @Transactional(readOnly = true)
    public Page<DoctorGetDTO> findAll(Pageable pageable){
        Page<Doctor> users = repository.findAll(pageable);
        return users.map(DoctorGetDTO::new);
    }

    @Transactional(readOnly = true)
    public DoctorGetDTO findByEmail(String email){
        Doctor user = repository.findByEmail(email).orElseThrow(() -> new CustomNotFoundException(USER_NOT_FOUND));
        return new DoctorGetDTO(user);
    }


    @Transactional()
    public DoctorGetDTO insert(DoctorInsertDTO dto){
        CustomRepeatedException error = new CustomRepeatedException();
        if(userRepository.existsByEmail(dto.getEmail())){
            error.addError("email", EMAIL_ALREADY_EXISTS);
        }

        if(!error.getErrors().isEmpty()){
            throw error;
        }

        Doctor user = new Doctor();
        copyDtoToEntity(dto, user);

        Role roleAdmin = roleRepository.findByAuthority("ROLE_ADMIN").orElse(roleService.createRole("ROLE_ADMIN"));
        user.addRole(roleAdmin);
        Role roleUser = roleRepository.findByAuthority("ROLE_CLIENT").orElse(roleService.createRole("ROLE_CLIENT"));
        user.addRole(roleUser);

        user = repository.save(user);
        return new DoctorGetDTO(user);
    }

    @Transactional()
    public DoctorGetDTO update(DoctorInsertDTO dto, Authentication authentication){
        Jwt jwt = (Jwt) authentication.getPrincipal();
        CustomRepeatedException error = new CustomRepeatedException();

        if(userRepository.existsByEmail(dto.getEmail()) && !dto.getEmail().equals(jwt.getClaim("username"))){
            error.addError("email", EMAIL_ALREADY_EXISTS);
        }

        if(!error.getErrors().isEmpty()){
            throw error;
        }

        Doctor doctor = getDoctorOrForbidden(USER_IS_NOT_DOCTOR);
        copyDtoToEntity(dto, doctor);
        return new DoctorGetDTO(repository.save(doctor));
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(){
        Doctor doctor = getDoctorOrForbidden(USER_IS_NOT_DOCTOR);
        repository.delete(doctor);
    }

    private Doctor getDoctorOrForbidden(String msg){
        User user = userService.getCurrentUser();

        if (!(user instanceof Doctor)){
            throw new ForbiddenException(msg);
        }

        return (Doctor) user;
    }

    private void copyDtoToEntity(DoctorInsertDTO dto, Doctor user) {
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setSpecialization(dto.getSpecialization());
    }
}
