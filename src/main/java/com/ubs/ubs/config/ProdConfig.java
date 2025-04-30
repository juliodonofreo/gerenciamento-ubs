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

@Configuration
@Profile("prod")
public class ProdConfig {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        // Cria roles se não existirem
        Role roleAdmin = createRoleIfNotExists("ROLE_ADMIN");
        Role roleStaff = createRoleIfNotExists("ROLE_STAFF");
        Role roleDoctor = createRoleIfNotExists("ROLE_DOCTOR");
        Role rolePatient = createRoleIfNotExists("ROLE_PATIENT");
        Role roleUnit = createRoleIfNotExists("ROLE_UNIT");

        // Cria usuários idênticos ao TestConfig se não existirem
        createPatientIfNotExists(rolePatient);
        createDoctorIfNotExists(roleDoctor);
        createAdminIfNotExists(roleAdmin);
        createStaffIfNotExists(roleStaff);
        createUnitIfNotExists(roleUnit);
    }

    private Role createRoleIfNotExists(String authority) {
        Role role = roleRepository.findByAuthority(authority).get();
        if (role == null) {
            role = new Role(null, authority);
            roleRepository.save(role);
        }
        return role;
    }

    private void createPatientIfNotExists(Role role) {
        if (userRepository.findByEmail("patient@test.com").isEmpty()) {
            Patient patient = new Patient(
                    null,
                    "John Doe",
                    "patient@test.com",
                    passwordEncoder.encode("1234"),
                    "123456789",
                    Instant.parse("1990-05-15T00:00:00Z")
            );
            patient.addRole(role);
            userRepository.save(patient);
        }
    }

    private void createDoctorIfNotExists(Role role) {
        if (userRepository.findByEmail("doctor@test.com").isEmpty()) {
            Doctor doctor = new Doctor(
                    null,
                    "Dr. Smith",
                    "doctor@test.com",
                    passwordEncoder.encode("1234"),
                    Specialization.CARDIOLOGIA
            );
            doctor.addRole(role);
            userRepository.save(doctor);
        }
    }

    private void createAdminIfNotExists(Role role) {
        if (userRepository.findByEmail("admin@test.com").isEmpty()) {
            User admin = new User(
                    null,
                    "Admin User",
                    "admin@test.com",
                    passwordEncoder.encode("securePassword")
            );
            admin.addRole(role);
            userRepository.save(admin);
        }
    }

    private void createStaffIfNotExists(Role role) {
        if (userRepository.findByEmail("staff@test.com").isEmpty()) {
            User staff = new User(
                    null,
                    "Staff User",
                    "staff@test.com",
                    passwordEncoder.encode("1234")
            );
            staff.addRole(role);
            userRepository.save(staff);
        }
    }

    private void createUnitIfNotExists(Role role) {
        if (userRepository.findByEmail("unit@test.com").isEmpty()) {
            User unit = new User(
                    null,
                    "Health Unit",
                    "unit@test.com",
                    passwordEncoder.encode("1234")
            );
            unit.addRole(role);
            userRepository.save(unit);
        }
    }
}