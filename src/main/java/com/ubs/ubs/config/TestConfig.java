package com.ubs.ubs.config;

import com.ubs.ubs.entities.Role;
import com.ubs.ubs.entities.User;
import com.ubs.ubs.repositories.RoleRepository;
import com.ubs.ubs.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

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
        userRepository.deleteAll();
        roleRepository.deleteAll();

        User vitor = new User(null, "Vitor", "vitormatheusfv@gmail.com", passwordEncoder.encode("pass123456"));
        Role roleAdmin = new Role(null, "ROLE_ADMIN");
        Role roleClient = new Role(null, "ROLE_CLIENT");
        vitor.addRole(roleAdmin);
        vitor.addRole(roleClient);

        roleRepository.saveAll(Arrays.asList(roleAdmin, roleClient));
        userRepository.save(vitor);

    }
}