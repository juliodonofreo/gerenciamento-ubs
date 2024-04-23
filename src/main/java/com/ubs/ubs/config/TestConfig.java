package com.ubs.ubs.config;

import com.ubs.ubs.entities.Doctor;
import com.ubs.ubs.entities.Patient;
import com.ubs.ubs.entities.Role;
import com.ubs.ubs.entities.User;
import com.ubs.ubs.enums.Specialization;
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

        User vitor = new Patient(null, "Vitor", "vitor@gmail.com", passwordEncoder.encode("1234"), "123456789", Instant.parse("1999-12-12T00:00:00Z"));
        User pingola = new Doctor(null, "pingoleta", "pingola@gmail.com", passwordEncoder.encode("1234"), Specialization.ENFERMEIRO);

        Role roleAdmin = new Role(null, "ROLE_ADMIN");
        Role roleClient = new Role(null, "ROLE_CLIENT");


        vitor.addRole(roleClient);
        pingola.addRole(roleAdmin);

        roleRepository.saveAll(Arrays.asList(roleAdmin, roleClient));
        userRepository.save(vitor);
        userRepository.save(pingola);
    }
}
