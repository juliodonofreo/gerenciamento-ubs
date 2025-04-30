package com.ubs.ubs.config;

import com.ubs.ubs.entities.Doctor;
import com.ubs.ubs.entities.Patient;
import com.ubs.ubs.entities.Role;
import com.ubs.ubs.entities.User;
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

        // Cria usuários de teste
        User patient = new Patient(
                null,
                "John Doe",
                "patient@test.com",
                passwordEncoder.encode("maria"),
                "123456789",
                Instant.parse("1990-05-15T00:00:00Z")
        );
        patient.addRole(rolePatient);

        User doctor = new Doctor(
                null,
                "Dr. Smith",
                "doctor@test.com",
                passwordEncoder.encode("maria"),
                Specialization.CARDIOLOGIA
        );
        doctor.addRole(roleDoctor);

        User admin = new User(
                null,
                "Admin User",
                "admin@admin.com",
                passwordEncoder.encode("maria")
        );
        admin.addRole(roleAdmin);

        userRepository.saveAll(Arrays.asList(patient, doctor, admin));
    }
}