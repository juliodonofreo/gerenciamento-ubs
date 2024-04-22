package com.ubs.ubs.services;

import com.ubs.ubs.dtos.AppointmentGetDTO;
import com.ubs.ubs.dtos.AppointmentInsertDTO;
import com.ubs.ubs.entities.Appointment;
import com.ubs.ubs.entities.Doctor;
import com.ubs.ubs.entities.Patient;
import com.ubs.ubs.entities.User;
import com.ubs.ubs.repositories.AppointmentRepository;
import com.ubs.ubs.repositories.DoctorRepository;
import com.ubs.ubs.repositories.PatientRepository;
import com.ubs.ubs.repositories.UserRepository;
import com.ubs.ubs.services.exceptions.CustomNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private UserService userService;

    public List<AppointmentGetDTO> findAll(){
        List<Appointment> entities = appointmentRepository.findAll();
        return entities.stream().map(AppointmentGetDTO::new).toList();
    }

    public AppointmentGetDTO insert(AppointmentInsertDTO dto) {
        User user = userService.getCurrentUser();
        Appointment appointment = new Appointment();

        appointment.setDate(dto.getDate());
        appointment.setDiagnosis(dto.getDiagnosis());
        appointment.setState(dto.getState());

        if(user instanceof Patient){
            Patient patient = (Patient) user;
            appointment.setPatient(patient);
            System.out.println(dto.getDoctor());

            Doctor doctor = doctorRepository.getReferenceById(dto.getDoctor().getId());
            appointment.setDoctor(doctor);
        }

        if (user instanceof Doctor){
            Doctor doctor = (Doctor) user;
            appointment.setDoctor(doctor);

            Patient patient = patientRepository.getReferenceById(dto.getPatient().getId());
            appointment.setPatient(patient);
        }

        appointment = appointmentRepository.save(appointment);
        return new AppointmentGetDTO(appointment);
    }

    public AppointmentGetDTO findById(Long id) {
        Appointment appointment = appointmentRepository.findById(id).orElseThrow(() -> new CustomNotFoundException("Consulta não encontrada"));
        return new AppointmentGetDTO(appointment);
    }
}
