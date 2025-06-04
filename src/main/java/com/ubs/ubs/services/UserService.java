package com.ubs.ubs.services;

import com.ubs.ubs.dtos.*;
import com.ubs.ubs.entities.*;
import com.ubs.ubs.projections.UserDetailsProjection;
import com.ubs.ubs.repositories.PasswordRecoverRepository;
import com.ubs.ubs.repositories.UserRepository;
import com.ubs.ubs.services.exceptions.CustomNotFoundException;
import com.ubs.ubs.services.exceptions.ForbiddenException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import static com.ubs.ubs.services.utils.ServiceErrorMessages.USER_NOT_FOUND;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordRecoverRepository passwordRecoverRepository;

    @Value("${email.password-recover.token.minutes}")
    private Long tokenExpiration;

    @Value("${email.password-recover.uri}")
    private String recoverUri;

    @Autowired
    private EmailService emailService;


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

    @Transactional
    public UserFullDTO currentUserDTO(){
        User user = getCurrentUser();

        if (user instanceof Patient){
            return new UserFullDTO((Patient) user);
        }

        if (user instanceof Doctor){
            return new UserFullDTO((Doctor) user);
        }

        if(user instanceof HealthUnit){
            return new UserFullDTO((HealthUnit) user);
        }

        if(user instanceof Staff){
            return new UserFullDTO((Staff) user);
        }

        return new UserFullDTO(user);
    }

    @Transactional
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

    @Transactional
    public void createRecoverToken(EmailDTO body) {

        User user = userRepository.findByEmail(body.getEmail()).get();
        if (user == null){
            throw new ResourceNotFoundException("Email não encontrado.");
        }

        PasswordRecover entity = new PasswordRecover();
        String token = UUID.randomUUID().toString();

        entity.setEmail(body.getEmail());
        entity.setToken(token);
        entity.setExpiration(Instant.now().plus(tokenExpiration, ChronoUnit.MINUTES));
        entity = passwordRecoverRepository.save(entity);

        String message = "Prezado(a) " + user.getName() + ",\n\n"
                + "Para redefinir sua senha, clique no link abaixo:\n"
                + recoverUri + "?token=" + token + "\n\n"
                + "**Este link será válido por " + tokenExpiration + " minutos.**\n\n"
                + "Caso não tenha solicitado esta alteração, ignore este e-mail ou entre em contato com nosso suporte.\n\n"
                + "Atenciosamente,\n"
                + "Equipe MedPlus";

        emailService.sendEmail(body.getEmail(), "Medplus - redefinição de Senha: Link válido por " + tokenExpiration + " minutos", message);
    }

    @Transactional
    public void saveNewPassword(NewPasswordDTO body) {
        List<PasswordRecover> tokens = passwordRecoverRepository.searchValidTokens(body.getToken(), Instant.now());
        if(tokens.isEmpty()){
            throw new ResourceNotFoundException("Token inválido.");
        }

        String email = tokens.get(tokens.size() - 1).getEmail();
        User user = userRepository.findByEmail(email).get();
        user.setPassword(passwordEncoder.encode(body.getPassword()));

        userRepository.save(user);
    }
}
