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
import com.ubs.ubs.services.utils.ServiceErrorMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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

    @Autowired
    private EmailService emailService;


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

            Doctor doctor = doctorRepository.findByEmail(dto.getDoctor().getEmail()).orElseThrow(() -> new CustomNotFoundException(ServiceErrorMessages.DOCTOR_NOT_FOUND));
            appointment.setDoctor(doctor);
        }

        if (user instanceof Doctor doctor){
            appointment.setDoctor(doctor);

            Patient patient = patientRepository.getPatientByCpf(dto.getPatient().getCpf()).orElseThrow(() -> new CustomNotFoundException(ServiceErrorMessages.PATIENT_NOT_FOUND));
            appointment.setPatient(patient);
        }

        appointment = appointmentRepository.save(appointment);
        return new AppointmentGetDTO(appointment);
    }

    public AppointmentGetDTO findById(Long id) {
        Appointment appointment = appointmentRepository.findById(id).orElseThrow(() -> new CustomNotFoundException(ServiceErrorMessages.APPOINTMENT_NOT_FOUND));
        return new AppointmentGetDTO(appointment);
    }

    public AppointmentGetDTO update(Long id, AppointmentInsertDTO dto) {
        Appointment entity = appointmentRepository.findById(id).orElseThrow(() -> new CustomNotFoundException(ServiceErrorMessages.APPOINTMENT_NOT_FOUND));
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
            Doctor doctor = doctorRepository.findByEmail(dto.getDoctor().getEmail()).orElseThrow(()-> new CustomNotFoundException(ServiceErrorMessages.DOCTOR_NOT_FOUND));
            entity.setDoctor(doctor);
        }

        if (dto.getPatient() != null && dto.getPatient().getCpf() != null){
            Patient patient = patientRepository.getPatientByCpf(dto.getPatient().getCpf()).orElseThrow(()-> new CustomNotFoundException(ServiceErrorMessages.PATIENT_NOT_FOUND));
            entity.setPatient(patient);
        }

        entity = appointmentRepository.save(entity);

        return new AppointmentGetDTO(entity);
    }

    @Scheduled(cron = "0 0 8 * * *")
    public void checkAndSendAppointmentReminders() {
        System.out.println("ENVIANDO EMAIL");
        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
        Instant start = LocalDateTime.now().plusDays(1).withHour(0).withMinute(0)
                .atZone(ZoneId.systemDefault()).toInstant();

        Instant end = LocalDateTime.now().plusDays(1).withHour(23).withMinute(59)
                .atZone(ZoneId.systemDefault()).toInstant();

        List<Appointment> appointments = appointmentRepository.findByDateBetween(start, end);
        System.out.println("Consultas: " + appointments);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (Appointment appointment : appointments) {
            Patient patient = appointment.getPatient();
            Doctor doctor = appointment.getDoctor();
            ZonedDateTime zonedDateTime = appointment.getDate().atZone(ZoneId.systemDefault());
            String formattedDate = zonedDateTime.format(formatter);

            // Enviar email
            emailService.sendAppointmentReminder(
                    patient.getEmail(),
                    patient.getName(),
                    doctor.getName(),
                    formattedDate
            );
        }
    }
}
