package com.ubs.ubs.services;

import com.ubs.ubs.dtos.AppointmentConcludeDTO;
import com.ubs.ubs.dtos.AppointmentGetDTO;
import com.ubs.ubs.dtos.AppointmentInsertDTO;
import com.ubs.ubs.dtos.AppointmentUpdateDTO;
import com.ubs.ubs.entities.*;
import com.ubs.ubs.entities.enums.AppointmentState;
import com.ubs.ubs.repositories.AppointmentRepository;
import com.ubs.ubs.repositories.DoctorRepository;
import com.ubs.ubs.repositories.ExamRepository;
import com.ubs.ubs.repositories.PatientRepository;
import com.ubs.ubs.services.exceptions.CustomNotFoundException;
import com.ubs.ubs.services.utils.ServiceErrorMessages;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    private ExamRepository examRepository;



    public List<AppointmentGetDTO> findAll(){
        List<Appointment> entities = appointmentRepository.findAll();
        return entities.stream().map(AppointmentGetDTO::new).toList();
    }

    public AppointmentGetDTO insert(AppointmentInsertDTO dto) {
        User user = userService.getCurrentUser();
        Appointment appointment = new Appointment();

        // Configuração básica
        appointment.setDate(dto.getDate());
        appointment.setDiagnosis(dto.getDiagnosis());
        appointment.setState(dto.getState());
        appointment.setType(dto.getType());

        // Busca direta por IDs
        Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new CustomNotFoundException("Médico não encontrado com ID: " + dto.getDoctorId()));

        Patient patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new CustomNotFoundException("Paciente não encontrado com ID: " + dto.getPatientId()));

        // Validação de unidade
        if (!doctor.getHealthUnit().getId().equals(patient.getHealthUnit().getId())) {
            throw new IllegalArgumentException("Médico e paciente devem ser da mesma unidade de saúde");
        }

        // Lógica de permissões
        if (user instanceof Patient) {
            if (!patient.getId().equals(user.getId())) {
                throw new AccessDeniedException("Paciente só pode agendar consultas para si mesmo");
            }
        } else if (user instanceof Doctor) {
            if (!doctor.getId().equals(user.getId())) {
                throw new AccessDeniedException("Médico só pode criar consultas para si mesmo");
            }
        }

        appointment.setDoctor(doctor);
        appointment.setPatient(patient);

        return new AppointmentGetDTO(appointmentRepository.save(appointment));
    }

    private void validateDoctorUnit(Doctor doctor, HealthUnit patientUnit) {
        if (!doctor.getHealthUnit().equals(patientUnit)) {
            throw new IllegalArgumentException("Médico não pertence à mesma unidade de saúde do paciente");
        }
    }

    private void validatePatientUnit(Patient patient, HealthUnit doctorUnit) {
        if (!patient.getHealthUnit().equals(doctorUnit)) {
            throw new IllegalArgumentException("Paciente não pertence à mesma unidade de saúde do médico");
        }
    }

    private void validateUnitConsistency(Patient patient, Doctor doctor) {
        if (!patient.getHealthUnit().equals(doctor.getHealthUnit())) {
            throw new IllegalArgumentException("Paciente e médico devem pertencer à mesma unidade de saúde");
        }
    }

    public AppointmentGetDTO findById(Long id) {
        Appointment appointment = appointmentRepository.findById(id).orElseThrow(() -> new CustomNotFoundException(ServiceErrorMessages.APPOINTMENT_NOT_FOUND));
        return new AppointmentGetDTO(appointment);
    }

    public AppointmentGetDTO update(Long id, AppointmentUpdateDTO dto) {
        Appointment entity = appointmentRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException(ServiceErrorMessages.APPOINTMENT_NOT_FOUND));

        User currentUser = userService.getCurrentUser();
        validatePermissions(currentUser, entity);

        // Atualização básica dos campos
        updateCoreFields(entity, dto);

        // Atualização de médico e paciente com validações
        updateDoctor(entity, dto);
        updatePatient(entity, dto);

        // Validação final da unidade de saúde
        validateHealthUnitConsistency(entity);

        return new AppointmentGetDTO(appointmentRepository.save(entity));
    }

    private void validatePermissions(User currentUser, Appointment appointment) {
        boolean isAuthorized = false;

        if (currentUser instanceof Patient patient) {
            isAuthorized = patient.getId().equals(appointment.getPatient().getId());
        }
        else if (currentUser instanceof Doctor doctor) {
            isAuthorized = doctor.getId().equals(appointment.getDoctor().getId());
        }
        else if (currentUser instanceof Staff staff) {
            isAuthorized = staff.getHealthUnit().getId().equals(appointment.getDoctor().getHealthUnit().getId());
        }
        else if (currentUser instanceof HealthUnit unit) {
            isAuthorized = unit.getId().equals(appointment.getDoctor().getHealthUnit().getId());
        }

        if (!isAuthorized) {
            throw new AccessDeniedException("Usuário não tem permissão para atualizar esta consulta");
        }
    }

    private void updateCoreFields(Appointment entity, AppointmentUpdateDTO dto) {
        if (dto.getDiagnosis() != null) {
            entity.setDiagnosis(dto.getDiagnosis());
        }

        if (dto.getDate() != null) {
            entity.setDate(dto.getDate());
        }

        if (dto.getState() != null) {
            entity.setState(dto.getState());
        }

        if (dto.getType() != null) {
            entity.setType(dto.getType());
        }
    }

    private void updateDoctor(Appointment entity, AppointmentUpdateDTO dto) {
        if (dto.getDoctorId() != null) {
            Doctor newDoctor = doctorRepository.findById(dto.getDoctorId())
                    .orElseThrow(() -> new CustomNotFoundException(ServiceErrorMessages.DOCTOR_NOT_FOUND));

            if (!newDoctor.getHealthUnit().getId().equals(entity.getDoctor().getHealthUnit().getId())) {
                throw new IllegalArgumentException("Novo médico deve ser da mesma unidade de saúde");
            }

            entity.setDoctor(newDoctor);
        }
    }

    private void updatePatient(Appointment entity, AppointmentUpdateDTO dto) {
        if (dto.getPatientId() != null) {
            Patient newPatient = patientRepository.findById(dto.getPatientId())
                    .orElseThrow(() -> new CustomNotFoundException(ServiceErrorMessages.PATIENT_NOT_FOUND));

            if (!newPatient.getHealthUnit().getId().equals(entity.getPatient().getHealthUnit().getId())) {
                throw new IllegalArgumentException("Novo paciente deve ser da mesma unidade de saúde");
            }

            entity.setPatient(newPatient);
        }
    }

    private void validateHealthUnitConsistency(Appointment appointment) {
        Long doctorUnitId = appointment.getDoctor().getHealthUnit().getId();
        Long patientUnitId = appointment.getPatient().getHealthUnit().getId();

        if (!doctorUnitId.equals(patientUnitId)) {
            throw new IllegalStateException("Médico e paciente devem pertencer à mesma unidade de saúde");
        }
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

    public List<AppointmentGetDTO> findAllByHealthUnit() {
        HealthUnit healthUnit = (HealthUnit) userService.getCurrentUser();

        return appointmentRepository.findByDoctorHealthUnitOrPatientHealthUnit(healthUnit.getId())
                .stream()
                .map(AppointmentGetDTO::new)
                .toList();
    }

    public AppointmentGetDTO concludeAppointment(Long id, AppointmentConcludeDTO dto) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found"));

        if (!appointment.getDoctor().getId().equals(userService.getCurrentUser().getId())) {
            throw new AccessDeniedException("Doctor not authorized for this appointment");
        }

        appointment.setState(AppointmentState.CONCLUIDO);
        appointment.setDiagnosis(dto.observations());

        if (dto.examType() != null && !dto.examType().isEmpty()) {
            Exam exam = new Exam();
            exam.setType(dto.examType());
            exam.setExamDate(dto.examDate().atStartOfDay());
            exam.setAppointment(appointment);
            examRepository.save(exam);
        }

        return new AppointmentGetDTO(appointmentRepository.save(appointment));
    }

    public List<AppointmentGetDTO> findAllByDoctor() {
        User currentUser = userService.getCurrentUser(); // Assume-se que existe um método para obter o usuário autenticado
        List<Appointment> appointments = appointmentRepository.findByDoctorId(currentUser.getId());

        return appointments.stream()
                .map(AppointmentGetDTO::new)
                .collect(Collectors.toList());
    }
}
