package com.ubs.ubs.services;

import com.ubs.ubs.dtos.DoctorDTO;
import com.ubs.ubs.dtos.PatientDTO;
import com.ubs.ubs.dtos.UserDTO;
import com.ubs.ubs.entities.Doctor;
import com.ubs.ubs.entities.Patient;
import com.ubs.ubs.entities.Role;
import com.ubs.ubs.entities.User;
import com.ubs.ubs.projections.UserDetailsProjection;
import com.ubs.ubs.repositories.RoleRepository;
import com.ubs.ubs.repositories.UserRepository;
import com.ubs.ubs.services.exceptions.CustomNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<UserDetailsProjection> list = repository.searchUserAndRolesByEmail(username);
        if (list.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        User user = new User();
        user.setEmail(username);
        user.setPassword(list.get(0).getPassword());
        for (UserDetailsProjection projection : list) {
            user.addRole(new Role(projection.getRoleId(), projection.getAuthority()));
        }

        return user;
    }

    public Page<UserDTO> findAll(Pageable pageable){
        Page<User> users = repository.findAll(pageable);
        return users.map(UserDTO::new);
    }

    public UserDTO findById(Long id){
        User user = repository.findById(id).orElseThrow(() -> new CustomNotFoundException("User not found"));
        if(user instanceof Patient){
            return new PatientDTO((Patient) user);
        }

        if (user instanceof Doctor){
            return new DoctorDTO((Doctor) user);
        }

        return new UserDTO(user);
    }

    public UserDTO findByEmail(String email){
        User user = repository.findByEmail(email).orElseThrow(() -> new CustomNotFoundException("User not found"));
        if(user instanceof Patient){
            return new PatientDTO((Patient) user);
        }

        if (user instanceof Doctor){
            return new DoctorDTO((Doctor) user);
        }

        return new UserDTO(user);
    }

    public PatientDTO insertPatient(PatientDTO dto){
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        Patient user = new Patient(null, dto.getName(), dto.getEmail(), dto.getPassword(), dto.getCpf(), dto.getBirth_date());
        Role roleUser = roleRepository.findById(2L).get();
        user.addRole(roleUser);
        user = repository.save(user);
        return new PatientDTO(user);
    }

    public DoctorDTO insertDoctor(DoctorDTO dto){
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        Doctor user = new Doctor(null, dto.getName(), dto.getEmail(), dto.getPassword(), dto.getSpecialization());

        Role roleAdmin = roleRepository.findById(1L).get();
        user.addRole(roleAdmin);
        Role roleUser = roleRepository.findById(2L).get();
        user.addRole(roleUser);

        user = repository.save(user);
        return new DoctorDTO(user);
    }

    public UserDTO update(UserDTO dto, Authentication authentication){
        User user = (User) authentication.getPrincipal();
        System.out.println(user.getEmail());
//        user.setName(dto.getName());
//        user.setEmail(dto.getEmail());
//        user.setPassword(passwordEncoder.encode(dto.getPassword()));
//        user = repository.save(user);
//        return new UserDTO(user);

        return null;
    }
}
