package com.ubs.ubs.services;

import com.ubs.ubs.entities.Role;
import com.ubs.ubs.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    protected Role createRole(String authority){
        if(!roleRepository.existsByAuthority(authority)){
            Role role = new Role();
            role.setAuthority(authority);
            return roleRepository.save(role);
        }

        return null;
    }

}
