package com.ubs.ubs.services;

import com.twilio.rest.microvisor.v1.App;
import com.ubs.ubs.dtos.AppointmentConcludeDTO;
import com.ubs.ubs.dtos.AppointmentGetDTO;
import com.ubs.ubs.dtos.AppointmentInsertDTO;
import com.ubs.ubs.dtos.AppointmentUpdateDTO;
import com.ubs.ubs.entities.*;
import com.ubs.ubs.entities.enums.AppointmentState;
import com.ubs.ubs.repositories.*;
import com.ubs.ubs.services.exceptions.CustomNotFoundException;
import com.ubs.ubs.services.exceptions.ForbiddenException;
import com.ubs.ubs.services.utils.ServiceErrorMessages;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
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

    @Autowired
    private HealthUnitRepository healthUnitRepository;

    private static final ZoneId DEFAULT_ZONE_ID = ZoneId.systemDefault();

    private static final ZoneId APP_ZONE_ID = ZoneId.of("America/Sao_Paulo"); // Ou ZoneId.systemDefault();

    // Formatadores para as variáveis [Data] e [Hora]
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private static final ZoneId SYSTEM_ZONE_ID = ZoneId.systemDefault();

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

        updateCoreFields(entity, dto);

        updateDoctor(entity, dto);
        updatePatient(entity, dto);

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

    @Scheduled(cron = "0 * * * * *") // A cada minuto
    @Transactional
    public void checkAndSendAppointmentReminders() {
        System.out.println("INICIANDO VERIFICAÇÃO DE LEMBRETES DE CONSULTA (" + LocalDateTime.now() + ")");

        List<HealthUnit> activeUnits = healthUnitRepository.findByReminderEnabledTrue();
        if (activeUnits.isEmpty()) {
            System.out.println("Nenhuma unidade de saúde com lembretes ativos.");
            return;
        }

        Instant jobExecutionInstant = Instant.now();
        Instant jobProcessingWindowEndInstant = jobExecutionInstant.plus(24, ChronoUnit.HOURS); // Cobre as próximas 24h

        for (HealthUnit unit : activeUnits) {
            if (unit.getReminderLeadTimeValue() == null || unit.getReminderLeadTimeUnit() == null) {
                System.out.println("Unidade '" + unit.getName() + "' não possui configuração de antecedência. Pulando.");
                continue;
            }

            System.out.println("Processando unidade: " + unit.getName());

            long leadValue = unit.getReminderLeadTimeValue();
            ChronoUnit leadChronoUnit = unit.getReminderLeadTimeUnit().toChronoUnit();

            // Calcular o intervalo de datas DAS CONSULTAS para buscar:
            // Data da consulta = (Momento do envio do lembrete) + Antecedência
            // Momento do envio do lembrete está entre [jobExecutionInstant, jobProcessingWindowEndInstant)
            Instant searchAppointmentsFromDate = jobExecutionInstant.plus(leadValue, leadChronoUnit);
            Instant searchAppointmentsToDate = jobProcessingWindowEndInstant.plus(leadValue, leadChronoUnit).minusSeconds(1); // Exclusivo no final

            System.out.printf("  Buscando consultas para %s entre %s e %s (data da consulta)\n",
                    unit.getName(),
                    LocalDateTime.ofInstant(searchAppointmentsFromDate, SYSTEM_ZONE_ID),
                    LocalDateTime.ofInstant(searchAppointmentsToDate, SYSTEM_ZONE_ID));

            List<Appointment> appointmentsToRemind = appointmentRepository.findAppointmentsForReminder(
                    unit, searchAppointmentsFromDate, searchAppointmentsToDate);

            System.out.println("  Consultas encontradas para " + unit.getName() + ": " + appointmentsToRemind.size());

            for (Appointment appointment : appointmentsToRemind) {
                Patient patient = appointment.getPatient();
                Doctor doctor = appointment.getDoctor(); // Já vem com HealthUnit via JOIN FETCH

                if (doctor.getHealthUnit() == null || !doctor.getHealthUnit().getId().equals(unit.getId())) {
                    System.err.println("  ERRO DE LÓGICA: Consulta " + appointment.getId() +
                            " não pertence à unidade esperada " + unit.getName() + ". Pulando.");
                    continue;
                }

                ZonedDateTime appointmentZonedDateTime = appointment.getDate().atZone(SYSTEM_ZONE_ID);

                Map<String, String> variables = new HashMap<>();
                variables.put("Paciente", patient.getName());
                variables.put("Data", appointmentZonedDateTime.format(DATE_FORMATTER));
                variables.put("Hora", appointmentZonedDateTime.format(TIME_FORMATTER));
                variables.put("Clínica", unit.getName());
                variables.put("Médico", doctor.getName());
                variables.put("Especialidade", doctor.getSpecialization() != null ? doctor.getSpecialization().getFormattedName() : "N/A");

                try {
                    if (unit.isUseCustomReminderTemplate() && unit.getCustomReminderTemplate() != null && !unit.getCustomReminderTemplate().isEmpty()) {
                        emailService.sendCustomAppointmentReminder(
                                patient.getEmail(),
                                unit.getCustomReminderTemplate(),
                                variables
                        );
                    } else {
                        emailService.sendAppointmentReminder(
                                patient.getEmail(),
                                variables // O método padrão também usará o mapa
                        );
                    }

                    appointment.setReminderSentAt(Instant.now());
                    appointmentRepository.save(appointment); // Marcar como enviado
                    System.out.println("  Lembrete enviado para: " + patient.getEmail() + " (Consulta ID: " + appointment.getId() + ")");

                } catch (Exception e) {
                    System.err.println("  FALHA AO ENVIAR LEMBRETE para consulta ID " + appointment.getId() +
                            " (Paciente: " + patient.getEmail() + "): " + e.getMessage());
                }
            }
        }
        System.out.println("VERIFICAÇÃO DE LEMBRETES CONCLUÍDA (" + LocalDateTime.now() + ")");
    }

    @Transactional(readOnly = true)
    public List<AppointmentGetDTO> findAllByHealthUnit() {
        User currentUser = userService.getCurrentUser(); // This should return the specific user type
        Long healthUnitIdToQuery = null;

        if (currentUser instanceof HealthUnit healthUnitUser) {
            healthUnitIdToQuery = healthUnitUser.getId();
        } else if (currentUser instanceof Staff staffUser) {
            if (staffUser.getHealthUnit() != null) {
                healthUnitIdToQuery = staffUser.getHealthUnit().getId();
            } else {
                System.err.println("Warning: Staff user " + staffUser.getUsername() + " is not associated with a Health Unit.");
                return Collections.emptyList();
            }
        } else {
            throw new AccessDeniedException("User type not authorized to retrieve appointments by health unit.");
        }

        if (healthUnitIdToQuery == null) {
            System.err.println("Error: Could not determine Health Unit ID for current user.");
            return Collections.emptyList();
        }

        return appointmentRepository.findByDoctorHealthUnitOrPatientHealthUnit(healthUnitIdToQuery)
                .stream()
                .map(AppointmentGetDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Map<String, Long> getAppointmentsCountForCurrentWeekByDay() {
        User currentUser = userService.getCurrentUser();
        Long healthUnitId;

        if (currentUser instanceof HealthUnit) {
            healthUnitId = ((HealthUnit) currentUser).getId();
        } else if (currentUser instanceof Staff) {
            Staff staffUser = (Staff) currentUser;
            if (staffUser.getHealthUnit() != null) {
                healthUnitId = staffUser.getHealthUnit().getId();
            } else {
                throw new IllegalStateException("Staff user " + staffUser.getUsername() + " is not associated with a Health Unit for this operation.");
            }
        } else {
            throw new AccessDeniedException("User type not authorized for weekly summary.");
        }

        LocalDate today = LocalDate.now(DEFAULT_ZONE_ID); // Usar a zona padrão
        LocalDate startOfWeekDate = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeekDate = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        // Converter LocalDate para Instant no início do dia para startOfWeekDate
        // e início do dia SEGUINTE a endOfWeekDate para o limite superior (exclusive)
        Instant startOfWeekInstant = startOfWeekDate.atStartOfDay(DEFAULT_ZONE_ID).toInstant();
        Instant endOfWeekExclusiveInstant = endOfWeekDate.plusDays(1).atStartOfDay(DEFAULT_ZONE_ID).toInstant();


        List<Appointment> appointmentsThisWeek = appointmentRepository
                .findByHealthUnitIdAndDateBetweenAndStatusNot(
                        healthUnitId,
                        startOfWeekInstant,
                        endOfWeekExclusiveInstant,
                        AppointmentState.CANCELADO
                );

        Map<DayOfWeek, Long> countsByDayOfWeek = appointmentsThisWeek.stream()
                .filter(app -> app.getDate() != null)
                .collect(Collectors.groupingBy(
                        // Converter o Instant do agendamento para LocalDate NA ZONA CORRETA para obter o DayOfWeek
                        app -> app.getDate().atZone(DEFAULT_ZONE_ID).toLocalDate().getDayOfWeek(),
                        Collectors.counting()
                ));

        Map<String, Long> result = new LinkedHashMap<>();
        DayOfWeek[] weekOrder = {
                DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
                DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY
        };
        String[] dayLabels = {"Seg", "Ter", "Qua", "Qui", "Sex", "Sáb", "Dom"};

        for (int i = 0; i < weekOrder.length; i++) {
            DayOfWeek day = weekOrder[i];
            String dayLabel = dayLabels[i];
            result.put(dayLabel, countsByDayOfWeek.getOrDefault(day, 0L));
        }
        return result;
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

    public List<AppointmentGetDTO> findAllByPatient() {
        User user = userService.getCurrentUser();

        if (!(user instanceof Patient)) {
            throw new ForbiddenException("Usuário não é um paciente");
        }

        Patient patient = (Patient) user;
        List<Appointment> appointments = appointmentRepository.findByPatientId(patient.getId());

        return appointments.stream()
                .map(AppointmentGetDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AppointmentGetDTO> getTodaysAppointmentsForCurrentUserUnit() {
        User currentUser = userService.getCurrentUser();
        Long healthUnitId;

        if (currentUser instanceof Staff) {
            Staff staffUser = (Staff) currentUser;
            if (staffUser.getHealthUnit() != null) {
                healthUnitId = staffUser.getHealthUnit().getId();
            } else {
                throw new IllegalStateException("Staff user " + staffUser.getUsername() + " não está associado a uma Unidade de Saúde.");
            }
        } else if (currentUser instanceof HealthUnit) { // Se a unidade logar, ela vê seus próprios agendamentos
            healthUnitId = ((HealthUnit) currentUser).getId();
        }
        else {
            // Poderia permitir para Admin ver de uma unidade específica, ou lançar exceção
            throw new AccessDeniedException("Tipo de usuário não autorizado para esta operação.");
        }

        LocalDate today = LocalDate.now(APP_ZONE_ID);
        Instant startOfDay = today.atStartOfDay(APP_ZONE_ID).toInstant();
        Instant endOfDay = today.atTime(LocalTime.MAX).atZone(APP_ZONE_ID).toInstant(); // Fim do dia (23:59:59.999...)
        // Alternativa para endOfDay se a query usa '<':
        // Instant endOfDay = today.plusDays(1).atStartOfDay(APP_ZONE_ID).toInstant();


        List<AppointmentState> relevantStatuses = Arrays.asList(
                AppointmentState.AGENDADO,
                AppointmentState.CANCELADO,
                AppointmentState.CONCLUIDO
        );

        List<Appointment> appointments = appointmentRepository
                .findByHealthUnitIdAndDayAndStatusesOrderByDate(
                        healthUnitId,
                        startOfDay,
                        endOfDay, // Se a query for a.date < endOfDayInstant, use today.plusDays(1).atStartOfDay...
                        relevantStatuses
                );

        return appointments.stream()
                .map(AppointmentGetDTO::new) // Certifique-se que o DTO é adequado
                .collect(Collectors.toList());
    }

    public AppointmentGetDTO cancelAppointment(Long id) {
        System.out.println("Entrou no cancel");
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found"));

        if (!appointment.getPatient().getId().equals(userService.getCurrentUser().getId())) {
            throw new AccessDeniedException("Doctor not authorized for this appointment");
        }

        appointment.setState(AppointmentState.CANCELADO);

        return new AppointmentGetDTO(appointmentRepository.save(appointment));
    }
}
