package com.ubs.ubs.services;

import com.ubs.ubs.dtos.*;
import com.ubs.ubs.entities.Doctor;
import com.ubs.ubs.entities.Patient;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.ubs.ubs.services.utils.ServiceErrorMessages.USER_NOT_FOUND;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;



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

    public UserFullDTO currentUserDTO(){
        User user = getCurrentUser();

        if (user instanceof Patient){
            return new UserFullDTO((Patient) user);
        }

        if (user instanceof Doctor){
            return new UserFullDTO((Doctor) user);
        }

        return new UserFullDTO(user);
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

    public void copyDtoToEntity(UserInsertDTO dto, User entity){
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
        entity.setPassword(passwordEncoder.encode(dto.getPassword()));
    }

    public void copyDtoToEntity(UserUpdateDTO dto, User entity){
        if (dto.getName() != null && !dto.getName().isEmpty()){
            entity.setName(dto.getName());
        }

        if (dto.getEmail() != null && !dto.getEmail().isEmpty()) {
            entity.setEmail(dto.getEmail());
        }

        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
    }

}
