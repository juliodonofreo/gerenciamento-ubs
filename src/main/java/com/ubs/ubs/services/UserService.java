package com.ubs.ubs.services;

import com.ubs.ubs.entities.Role;
import com.ubs.ubs.entities.User;
import com.ubs.ubs.projections.UserDetailsProjection;
import com.ubs.ubs.repositories.UserRepository;
import com.ubs.ubs.services.exceptions.CustomNotFoundException;
import com.ubs.ubs.services.exceptions.ForbiddenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    private final String USER_NOT_FOUND = "Usuário não encontrado.";

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<UserDetailsProjection> list = repository.searchUserAndRolesByEmail(username);
        if (list.isEmpty()) {
            throw new UsernameNotFoundException(USER_NOT_FOUND);
        }

        User user = new User();
        user.setEmail(username);
        user.setPassword(list.get(0).getPassword());
        for (UserDetailsProjection projection : list) {
            user.addRole(new Role(projection.getRoleId(), projection.getAuthority()));
        }

        return user;
    }

    public User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getPrincipal().equals("anonymousUser")){
            throw new ForbiddenException("Usuário precisa estar logado.");
        }

        Jwt jwt = (Jwt) authentication.getPrincipal();
        String username = jwt.getClaim("username");

        return repository.findByEmail(username).orElseThrow(() -> new CustomNotFoundException(USER_NOT_FOUND));
    }

    public void validateSelfOrAdmin(Long userId, Long toCheckId, String msg){
        if (!userId.equals(toCheckId) && !getCurrentUser().hasRole("ROLE_ADMIN")){
            throw new ForbiddenException(msg);
        }
    }

}
