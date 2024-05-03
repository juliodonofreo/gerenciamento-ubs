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
import com.ubs.ubs.services.exceptions.CustomNotFoundException;
import com.ubs.ubs.services.exceptions.ForbiddenException;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final String DOCTOR_NOT_FOUND = "Médico não encontrado.";
    private final String PATIENT_NOT_FOUND = "Paciente não encontrado.";
    private final String APPOINTMENT_NOT_FOUND = "Consulta não encontrada.";

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

        if(user instanceof Patient patient){
            appointment.setPatient(patient);

            Doctor doctor = doctorRepository.findByEmail(dto.getDoctor().getEmail()).orElseThrow(() -> new CustomNotFoundException(DOCTOR_NOT_FOUND));
            appointment.setDoctor(doctor);
        }

        if (user instanceof Doctor doctor){
            appointment.setDoctor(doctor);

            Patient patient = patientRepository.getPatientByCpf(dto.getPatient().getCpf()).orElseThrow(() -> new CustomNotFoundException(PATIENT_NOT_FOUND));
            appointment.setPatient(patient);
        }

        appointment = appointmentRepository.save(appointment);
        return new AppointmentGetDTO(appointment);
    }

    public AppointmentGetDTO findById(Long id) {
        Appointment appointment = appointmentRepository.findById(id).orElseThrow(() -> new CustomNotFoundException(APPOINTMENT_NOT_FOUND));
        return new AppointmentGetDTO(appointment);
    }

    public AppointmentGetDTO update(Long id, AppointmentInsertDTO dto) {
        Appointment entity = appointmentRepository.findById(id).orElseThrow(() -> new CustomNotFoundException(APPOINTMENT_NOT_FOUND));
        User currentUser = userService.getCurrentUser();

        try {
            userService.validateSelfOrAdmin(currentUser.getId(), entity.getDoctor().getId(), "Usuário não pode editar este appointment.");
        }
        catch (ForbiddenException e){
            userService.validateSelfOrAdmin(currentUser.getId(), entity.getPatient().getId(), "Usuário não pode editar este appointment.");
        }

        if (dto.getDiagnosis() != null){
            entity.setDiagnosis(dto.getDiagnosis());
        }

        entity.setDate(dto.getDate());
        entity.setState(dto.getState());

        if (dto.getDoctor() != null && dto.getDoctor().getEmail() != null){
            Doctor doctor = doctorRepository.findByEmail(dto.getDoctor().getEmail()).orElseThrow(()-> new CustomNotFoundException(DOCTOR_NOT_FOUND));
            entity.setDoctor(doctor);
        }

        if (dto.getPatient() != null && dto.getPatient().getCpf() != null){
            Patient patient = patientRepository.getPatientByCpf(dto.getPatient().getCpf()).orElseThrow(()-> new CustomNotFoundException(PATIENT_NOT_FOUND));
            entity.setPatient(patient);
        }

        entity = appointmentRepository.save(entity);

        return new AppointmentGetDTO(entity);
    }
}
