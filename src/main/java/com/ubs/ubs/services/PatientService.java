package com.ubs.ubs.services;


import com.ubs.ubs.dtos.DependentGetDTO;
import com.ubs.ubs.dtos.DependentInsertDTO;
import com.ubs.ubs.dtos.PatientGetDTO;
import com.ubs.ubs.dtos.PatientInsertDTO;
import com.ubs.ubs.entities.Dependent;
import com.ubs.ubs.entities.Patient;
import com.ubs.ubs.entities.Role;
import com.ubs.ubs.entities.User;
import com.ubs.ubs.repositories.DependentRepository;
import com.ubs.ubs.repositories.PatientRepository;
import com.ubs.ubs.repositories.RoleRepository;
import com.ubs.ubs.repositories.UserRepository;
import com.ubs.ubs.services.exceptions.CustomNotFoundException;
import com.ubs.ubs.services.exceptions.CustomRepeatedException;
import com.ubs.ubs.services.exceptions.ForbiddenException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DependentRepository dependentRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    private final String EMAIL_ALREADY_EXISTS = "Email já existente.";
    private final String CPF_ALREADY_EXISTS = "CPF já existente.";
    private final String USER_IS_NOT_PATIENT = "Usuário não é um paciente.";
    private final String DEPENDENT_NOT_FOUND = "Dependente não encontrado.";
    private final String USER_CANNOT_ACCESS_DEPENDENT = "Usuário não pode acessar este dependente.";

    @Transactional(readOnly = true)
    public Page<PatientGetDTO> findAll(Pageable pageable){
        Page<Patient> users = repository.findAll(pageable);
        return users.map(PatientGetDTO::new);
    }

    @Transactional(readOnly = true)
    public PatientGetDTO findByCpf(String cpf){
        Patient user = repository.getPatientByCpf(cpf).orElseThrow(() -> new CustomNotFoundException("CPF não cadastrado"));
        return new PatientGetDTO(user);
    }

    @Transactional
    public PatientGetDTO insert(@RequestBody PatientInsertDTO dto){
        CustomRepeatedException error = new CustomRepeatedException();
        if(userRepository.existsByEmail(dto.getEmail())){
            error.addError("email", EMAIL_ALREADY_EXISTS);
        }

        if (repository.existsByCpf(dto.getCpf())) {
            error.addError("cpf", CPF_ALREADY_EXISTS);
        }

        if(!error.getErrors().isEmpty()){
            throw error;
        }

        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        Patient user = new Patient(null, dto.getName(), dto.getEmail(), dto.getPassword(), dto.getCpf(), dto.getBirth_date());
        Role roleUser = roleRepository.findByAuthority("ROLE_CLIENT").orElse(roleService.createRole("ROLE_CLIENT"));
        user.addRole(roleUser);
        user = repository.save(user);
        return new PatientGetDTO(user);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public PatientGetDTO update(@Valid @RequestBody PatientInsertDTO dto){
        CustomRepeatedException error = new CustomRepeatedException();
        Patient patient = getPatientOrForbidden(USER_IS_NOT_PATIENT);

        if(userRepository.existsByEmail(dto.getEmail()) && !dto.getEmail().equals(patient.getEmail())){
            error.addError("email", EMAIL_ALREADY_EXISTS);
        }

        if (repository.existsByCpf(dto.getCpf()) && !dto.getCpf().equals(patient.getCpf())) {
            error.addError("cpf", CPF_ALREADY_EXISTS);
        }

        if (!error.getErrors().isEmpty()) {
            throw error;
        }

        patient.setName(dto.getName());
        patient.setEmail(dto.getEmail());
        patient.setPassword(passwordEncoder.encode(dto.getPassword()));
        patient.setCpf(dto.getCpf());
        patient.setBirth_date(dto.getBirth_date());
        return new PatientGetDTO(repository.save(patient));
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(){
        Patient patient = getPatientOrForbidden(USER_IS_NOT_PATIENT);
        repository.delete(patient);
    }

    @Transactional
    public DependentGetDTO addDependent(DependentInsertDTO dto){
        Patient patient = getPatientOrForbidden("Usuário não pode inserir dependentes. ");
        Dependent dependent = new Dependent();

        dependent.setBirth_date(dto.getBirth_date());
        dependent.setName(dto.getName());
        dependent.setCompanion(patient);

        dependent = dependentRepository.save(dependent);
        patient.getDependents().add(dependent);

        dependent = dependentRepository.save(dependent);
        repository.save(patient);
        return new DependentGetDTO(dependent);
    }

    public List<DependentGetDTO> findAllDependents() {
        Patient patient = getPatientOrForbidden("Usuário não possui dependentes");
        List<Dependent> dependents = dependentRepository.findByCompanion(patient.getId());
        return dependents.stream().map(DependentGetDTO::new).toList();
    }

    public DependentGetDTO findDependentsById(Long id) {
        Dependent dependent = dependentRepository.findById(id).orElseThrow(() -> new CustomNotFoundException(DEPENDENT_NOT_FOUND));
        User user = userService.getCurrentUser();

        userService.validateSelfOrAdmin(user.getId(), dependent.getCompanion().getId(), USER_CANNOT_ACCESS_DEPENDENT);
        return new DependentGetDTO(dependent);
    }

    public DependentGetDTO updateDependent(Long id, DependentInsertDTO dto){
        Dependent dependent = dependentRepository.findById(id).orElseThrow(() -> new CustomNotFoundException(DEPENDENT_NOT_FOUND));
        User user = userService.getCurrentUser();

        userService.validateSelfOrAdmin(user.getId(), dependent.getCompanion().getId(), USER_CANNOT_ACCESS_DEPENDENT);

        dependent.setName(dto.getName());
        dependent.setBirth_date(dto.getBirth_date());
        return new DependentGetDTO(dependentRepository.save(dependent));
    }

    public void deleteDependent(Long id){
        Dependent dependent = dependentRepository.findById(id).orElseThrow(() -> new CustomNotFoundException(DEPENDENT_NOT_FOUND));

        User user = userService.getCurrentUser();
        userService.validateSelfOrAdmin(user.getId(), dependent.getCompanion().getId(), USER_CANNOT_ACCESS_DEPENDENT);
        dependentRepository.delete(dependent);
    }


    private Patient getPatientOrForbidden(String msg){
        User user = userService.getCurrentUser();

        if (!(user instanceof Patient)){
            throw new ForbiddenException(msg);
        }

        return (Patient) user;
    }
}

