package com.ubs.ubs.services;


import com.ubs.ubs.dtos.DoctorInsertDTO;
import com.ubs.ubs.dtos.PatientGetDTO;
import com.ubs.ubs.dtos.PatientInsertDTO;
import com.ubs.ubs.dtos.UserGetDTO;
import com.ubs.ubs.entities.Doctor;
import com.ubs.ubs.entities.Patient;
import com.ubs.ubs.entities.Role;
import com.ubs.ubs.entities.User;
import com.ubs.ubs.projections.UserDetailsProjection;
import com.ubs.ubs.repositories.PatientRepository;
import com.ubs.ubs.repositories.RoleRepository;
import com.ubs.ubs.services.exceptions.CustomNotFoundException;
import com.ubs.ubs.services.exceptions.CustomRepeatedException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class PatientService{

    @Autowired
    private PatientRepository repository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)

    public Page<PatientGetDTO> findAll(Pageable pageable){
        Page<Patient> users = repository.findAll(pageable);
        return users.map(PatientGetDTO::new);
    }

    @Transactional(readOnly = true)
    public PatientGetDTO findById(Long id){
        Patient user = repository.findById(id).orElseThrow(() -> new CustomNotFoundException("User not found"));
        return new PatientGetDTO(user);
    }

    @Transactional(readOnly = true)
    public PatientGetDTO findByEmail(String email){
        Patient user = repository.findByEmail(email).orElseThrow(() -> new CustomNotFoundException("User not found"));
        return new PatientGetDTO(user);
    }


    @Transactional(propagation = Propagation.SUPPORTS)
    public PatientGetDTO insert(@Valid @RequestBody PatientInsertDTO dto){
        if(repository.findByEmail(dto.getEmail()).isPresent()){
            throw new CustomRepeatedException("Email já existente. ");
        }

        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        Patient user = new Patient(null, dto.getName(), dto.getEmail(), dto.getPassword(), dto.getCpf(), dto.getBirth_date());
        Role roleUser = roleRepository.findById(2L).get();
        user.addRole(roleUser);
        user = repository.save(user);
        return new PatientGetDTO(user);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public PatientGetDTO update(@Valid @RequestBody PatientInsertDTO dto, Authentication authentication){
        Jwt jwt = (Jwt) authentication.getPrincipal();

        if(repository.findByEmail(dto.getEmail()).isPresent() && !dto.getEmail().equals(jwt.getClaim("username"))){
            throw new CustomRepeatedException("Email já existente. ");
        }

        Patient patient = (Patient) repository.findByEmail(jwt.getClaim("username")).orElseThrow(() -> new CustomNotFoundException("User not found"));
        patient.setName(dto.getName());
        patient.setEmail(dto.getEmail());
        patient.setPassword(passwordEncoder.encode(dto.getPassword()));
        patient.setCpf(dto.getCpf());
        patient.setBirth_date(dto.getBirth_date());
        return new PatientGetDTO(repository.save(patient));
    }
}

