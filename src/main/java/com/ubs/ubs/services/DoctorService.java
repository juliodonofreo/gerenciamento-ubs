package com.ubs.ubs.services;

import com.ubs.ubs.dtos.DoctorGetDTO;
import com.ubs.ubs.dtos.DoctorInsertDTO;
import com.ubs.ubs.entities.Doctor;
import com.ubs.ubs.entities.Role;
import com.ubs.ubs.repositories.DoctorRepository;
import com.ubs.ubs.repositories.RoleRepository;
import com.ubs.ubs.services.exceptions.CustomNotFoundException;
import com.ubs.ubs.services.exceptions.CustomRepeatedException;
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

    @Transactional(readOnly = true)
    public Page<DoctorGetDTO> findAll(Pageable pageable){
        Page<Doctor> users = repository.findAll(pageable);
        return users.map(DoctorGetDTO::new);
    }

    @Transactional(readOnly = true)
    public DoctorGetDTO findByEmail(String email){
        Doctor user = repository.findByEmail(email).orElseThrow(() -> new CustomNotFoundException("User not found"));
        return new DoctorGetDTO(user);
    }


    @Transactional(propagation = Propagation.SUPPORTS)
    public DoctorGetDTO insert(DoctorInsertDTO dto){
        if(repository.findByEmail(dto.getEmail()).isPresent()){
            throw new CustomRepeatedException("Email já existente. ");
        }

        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        Doctor user = new Doctor(null, dto.getName(), dto.getEmail(), dto.getPassword(), dto.getSpecialization());

        Role roleAdmin = roleRepository.findById(1L).get();
        user.addRole(roleAdmin);
        Role roleUser = roleRepository.findById(2L).get();
        user.addRole(roleUser);

        user = repository.save(user);
        return new DoctorGetDTO(user);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public DoctorGetDTO update(DoctorInsertDTO dto, Authentication authentication){
        Jwt jwt = (Jwt) authentication.getPrincipal();

        if(repository.findByEmail(dto.getEmail()).isPresent() && !dto.getEmail().equals(jwt.getClaim("username"))){
            throw new CustomRepeatedException("Email já existente. ");
        }

        Doctor doctor = (Doctor) repository.findByEmail(jwt.getClaim("username")).orElseThrow(() -> new CustomNotFoundException("User not found"));
        doctor.setName(dto.getName());
        doctor.setEmail(dto.getEmail());
        doctor.setPassword(passwordEncoder.encode(dto.getPassword()));
        doctor.setSpecialization(dto.getSpecialization());
        return new DoctorGetDTO(repository.save(doctor));
    }

}
