package com.ubs.ubs.config;

import com.ubs.ubs.entities.*;
import com.ubs.ubs.entities.enums.Specialization;
import com.ubs.ubs.repositories.RoleRepository;
import com.ubs.ubs.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.Arrays;

@Configuration
@Profile("test")
public class TestConfig {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        roleRepository.deleteAll();
        userRepository.deleteAll();

        // Cria todos os perfis em inglês
        Role roleAdmin = new Role(null, "ROLE_ADMIN");
        Role roleStaff = new Role(null, "ROLE_STAFF");
        Role roleDoctor = new Role(null, "ROLE_DOCTOR");
        Role rolePatient = new Role(null, "ROLE_PATIENT");
        Role roleUnit = new Role(null, "ROLE_UNIT");

        roleRepository.saveAll(Arrays.asList(
                roleAdmin,
                roleStaff,
                roleDoctor,
                rolePatient,
                roleUnit
        ));

        User healthUnit = new HealthUnit(
                null,
                "UBS Central",
                "ubs@test.com",
                passwordEncoder.encode("maria"),
                "1133344444",
                "Av. Principal, 1000"
        );
        healthUnit.addRole(roleUnit);

        userRepository.save(healthUnit);

        HealthUnit healthUnitInstance =  new HealthUnit(healthUnit.getId(),
                healthUnit.getName(),
                healthUnit.getEmail(),
                healthUnit.getPassword(),
                null, null);

        // Cria usuários de teste
        Patient patient = new Patient(
                null,
                "Julio Donofreo Morais",
                "julio.bibico.jdm@gmail.com",
                passwordEncoder.encode("maria"),
                "02224849001",
                Instant.parse("1990-05-15T00:00:00Z"),
                healthUnitInstance,
                "Jardim das flores, 137",
                "(28) 2616-8855"
        );
        patient.addRole(rolePatient);



        Doctor doctor = new Doctor(
                null,
                "Dr. Smith",
                "doctor@test.com",
                passwordEncoder.encode("maria"),
                Specialization.CARDIOLOGIA,
                healthUnitInstance,
                "122256486"
        );
        doctor.addRole(roleDoctor);

        User admin = new User(
                null,
                "Admin User",
                "admin@admin.com",
                passwordEncoder.encode("maria")
        );
        admin.addRole(roleAdmin);

        User staff = new Staff(
                null,
                "Enfermeira Ana Silva",
                "ana.silva@ubs.com",
                passwordEncoder.encode("maria"),
               healthUnitInstance
        );
        staff.addRole(roleStaff);

        userRepository.saveAll(Arrays.asList(patient, doctor, admin, staff));
    }
}