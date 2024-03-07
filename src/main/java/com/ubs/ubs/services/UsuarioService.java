package com.ubs.ubs.services;

import com.ubs.ubs.entities.Funcao;
import com.ubs.ubs.entities.Usuario;
import com.ubs.ubs.projections.UserDetailsProjection;
import com.ubs.ubs.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<UserDetailsProjection> list = repository.searchUserAndRolesByEmail(username);
        if (list.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        Usuario user = new Usuario();
        user.setEmail(username);
        user.setSenha(list.get(0).getPassword());
        for (UserDetailsProjection projection : list) {
            user.addRole(new Funcao(projection.getRoleId(), projection.getAuthority()));
        }

        return user;
    }
}
