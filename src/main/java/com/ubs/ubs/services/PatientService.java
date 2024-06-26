package com.ubs.ubs.services;


import com.ubs.ubs.dtos.*;
import com.ubs.ubs.entities.*;
import com.ubs.ubs.repositories.*;
import com.ubs.ubs.services.exceptions.CustomNotFoundException;
import com.ubs.ubs.services.exceptions.CustomRepeatedException;
import com.ubs.ubs.services.exceptions.ForbiddenException;
import com.ubs.ubs.services.utils.ServiceErrorMessages;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private UserRepository userRepository;

    @Autowired
    private DependentRepository dependentRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private AddressRepository addressRepository;


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
            error.addError("email", ServiceErrorMessages.EMAIL_ALREADY_EXISTS);
        }

        if (repository.existsByCpf(dto.getCpf())) {
            error.addError("cpf", ServiceErrorMessages.CPF_ALREADY_EXISTS);
        }

        if(!error.getErrors().isEmpty()){
            throw error;
        }

        Patient user = new Patient();
        Role roleUser = roleRepository.findByAuthority("ROLE_CLIENT").orElse(roleService.createRole("ROLE_CLIENT"));

        copyDtoToEntity(dto, user);
        user.addRole(roleUser);
        user = repository.save(user);
        return new PatientGetDTO(user);
    }

    @Transactional
    public PatientGetDTO update(@RequestBody @Valid PatientUpdateDTO dto){
        CustomRepeatedException error = new CustomRepeatedException();
        Patient patient = getPatientOrForbidden(ServiceErrorMessages.USER_IS_NOT_PATIENT);

        if(userRepository.existsByEmail(dto.getEmail()) && !dto.getEmail().equals(patient.getEmail())){
            error.addError("email", ServiceErrorMessages.EMAIL_ALREADY_EXISTS);
        }

        if (repository.existsByCpf(dto.getCpf()) && !dto.getCpf().equals(patient.getCpf())) {
            error.addError("cpf", ServiceErrorMessages.CPF_ALREADY_EXISTS);
        }

        if (!error.getErrors().isEmpty()) {
            throw error;
        }

        copyDtoToEntity(dto, patient);
        return new PatientGetDTO(repository.save(patient));
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(){
        Patient patient = getPatientOrForbidden(ServiceErrorMessages.USER_IS_NOT_PATIENT);
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

    @Transactional(readOnly = true)
    public List<DependentGetDTO> findAllDependents() {
        Patient patient = getPatientOrForbidden("Usuário não possui dependentes");
        List<Dependent> dependents = dependentRepository.findByCompanion(patient.getId());
        return dependents.stream().map(DependentGetDTO::new).toList();
    }

    @Transactional(readOnly = true)
    public DependentGetDTO findDependentsById(Long id) {
        Dependent dependent = dependentRepository.findById(id).orElseThrow(() -> new CustomNotFoundException(ServiceErrorMessages.DEPENDENT_NOT_FOUND));
        User user = userService.getCurrentUser();

        userService.validateSelfOrAdmin(user.getId(), dependent.getCompanion().getId(), ServiceErrorMessages.USER_CANNOT_ACCESS_DEPENDENT);
        return new DependentGetDTO(dependent);
    }

    @Transactional
    public DependentGetDTO updateDependent(Long id, DependentInsertDTO dto){
        Dependent dependent = dependentRepository.findById(id).orElseThrow(() -> new CustomNotFoundException(ServiceErrorMessages.DEPENDENT_NOT_FOUND));
        User user = userService.getCurrentUser();

        userService.validateSelfOrAdmin(user.getId(), dependent.getCompanion().getId(), ServiceErrorMessages.USER_CANNOT_ACCESS_DEPENDENT);

        dependent.setName(dto.getName());
        dependent.setBirth_date(dto.getBirth_date());

        return new DependentGetDTO(dependentRepository.save(dependent));
    }

    public void deleteDependent(Long id){
        Dependent dependent = dependentRepository.findById(id).orElseThrow(() -> new CustomNotFoundException(ServiceErrorMessages.DEPENDENT_NOT_FOUND));

        User user = userService.getCurrentUser();
        userService.validateSelfOrAdmin(user.getId(), dependent.getCompanion().getId(), ServiceErrorMessages.USER_CANNOT_ACCESS_DEPENDENT);
        dependentRepository.delete(dependent);
    }


    private Patient getPatientOrForbidden(String msg){
        User user = userService.getCurrentUser();

        if (!(user instanceof Patient)){
            throw new ForbiddenException(msg);
        }

        return (Patient) user;
    }

    private void copyDtoToEntity(PatientInsertDTO dto, Patient entity){
        userService.copyDtoToEntity(dto, entity);
        entity.setCpf(dto.getCpf());
        entity.setBirth_date(dto.getBirth_date());
        Address address = new Address();

        if (dto.getAddress() == null){
            return;
        }

        address.setStreet(dto.getAddress().getStreet());
        address.setDistrict(dto.getAddress().getDistrict());
        address.setCep(dto.getAddress().getCep());
        address.setNumber(dto.getAddress().getNumber());
        address.setCity(dto.getAddress().getCity());

        addressRepository.save(address);
        entity.setAddress(address);
    }

    private void copyDtoToEntity(PatientUpdateDTO dto, Patient entity){
        userService.copyDtoToEntity(dto, entity);

        if (dto.getCpf() != null && !dto.getCpf().isEmpty()){
            entity.setCpf(dto.getCpf());
        }

        if (dto.getBirth_date() != null){
            entity.setBirth_date(dto.getBirth_date());
        }

        if (dto.getAddress() == null){
            return;
        }

        if (dto.getAddress().getCep() != null && !dto.getAddress().getCep().isEmpty()){
            entity.getAddress().setCep(dto.getAddress().getCep());
        }

        if (dto.getAddress().getCity() != null && !dto.getAddress().getCity().isEmpty()){
            entity.getAddress().setCity(dto.getAddress().getCity());
        }

        if (dto.getAddress().getDistrict() != null && !dto.getAddress().getDistrict().isEmpty()){
            entity.getAddress().setDistrict(dto.getAddress().getDistrict());
        }

        if (dto.getAddress().getNumber() != null){
            entity.getAddress().setNumber(dto.getAddress().getNumber());
        }

        if (dto.getAddress().getStreet() != null && !dto.getAddress().getStreet().isEmpty()){
            entity.getAddress().setStreet(dto.getAddress().getStreet());
        }

        addressRepository.save(entity.getAddress());
    }

}

