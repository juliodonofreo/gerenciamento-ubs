package com.ubs.ubs.config;

import com.ubs.ubs.entities.Role;
import com.ubs.ubs.entities.User;
import com.ubs.ubs.repositories.RoleRepository;
import com.ubs.ubs.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;

@Configuration
@Profile("prod")
public class ProdConfig {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${adminEmail}")
    private String adminEmail;

    @Value("${adminPassword}")
    private String adminPassword;

    @PostConstruct
    public void init() {
        createRolesIfNotExist();
        createAdminIfNotExist();
    }

    private void createRolesIfNotExist() {
        List<String> requiredRoles = Arrays.asList(
                "ROLE_ADMIN",
                "ROLE_STAFF",
                "ROLE_DOCTOR",
                "ROLE_PATIENT",
                "ROLE_UNIT"
        );

        requiredRoles.forEach(roleName -> {
            roleRepository.findByAuthority(roleName).orElseGet(() -> {
                Role newRole = new Role(null, roleName);
                return roleRepository.save(newRole);
            });
        });
    }

    private void createAdminIfNotExist() {
        if (adminEmail == null || adminEmail.isEmpty()) {
            throw new IllegalStateException("Variável de ambiente adminEmail não configurada");
        }

        if (adminPassword == null || adminPassword.isEmpty()) {
            throw new IllegalStateException("Variável de ambiente adminPassword não configurada");
        }

        userRepository.findByEmail(adminEmail).orElseGet(() -> {
            User admin = new User();
            admin.setName("Administrador");
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode(adminPassword));

            Role adminRole = roleRepository.findByAuthority("ROLE_ADMIN")
                    .orElseThrow(() -> new IllegalStateException("Role ROLE_ADMIN não encontrada"));

            admin.addRole(adminRole);

            return userRepository.save(admin);
        });
    }
}