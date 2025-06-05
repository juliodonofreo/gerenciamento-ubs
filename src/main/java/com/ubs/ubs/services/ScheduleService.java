package com.ubs.ubs.services;

import com.ubs.ubs.dtos.ScheduleRequestDTO;
import com.ubs.ubs.dtos.ScheduleResponseDTO;
import com.ubs.ubs.entities.Doctor;
import com.ubs.ubs.entities.Schedule;
import com.ubs.ubs.repositories.DoctorRepository;
import com.ubs.ubs.repositories.ScheduleRepository;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final DoctorRepository doctorRepository; // Para buscar o médico

    public ScheduleService(ScheduleRepository scheduleRepository, DoctorRepository doctorRepository) {
        this.scheduleRepository = scheduleRepository;
        this.doctorRepository = doctorRepository;
    }

    @Transactional
    public ScheduleResponseDTO createSchedule(ScheduleRequestDTO requestDTO) {
        Doctor doctor = doctorRepository.findById(requestDTO.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + requestDTO.getDoctorId()));

        Schedule schedule = new Schedule();
        schedule.setDoctor(doctor);
        schedule.setDayOfWeek(requestDTO.getDayOfWeek());
        schedule.setStartTime(requestDTO.getStartTime());
        schedule.setEndTime(requestDTO.getEndTime());

        // Validação: startTime deve ser antes de endTime
        if (schedule.getStartTime().isAfter(schedule.getEndTime()) || schedule.getStartTime().equals(schedule.getEndTime())) {
            throw new IllegalArgumentException("Start time must be before end time.");
        }

        Schedule savedSchedule = scheduleRepository.save(schedule);
        return mapToResponseDTO(savedSchedule);
    }

    @Transactional(readOnly = true)
    public List<ScheduleResponseDTO> getSchedulesByDoctorId(Long doctorId) {
        if (!doctorRepository.existsById(doctorId)) {
            throw new ResourceNotFoundException("Doctor not found with id: " + doctorId);
        }
        return scheduleRepository.findByDoctorId(doctorId).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ScheduleResponseDTO getScheduleById(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found with id: " + scheduleId));
        return mapToResponseDTO(schedule);
    }

    @Transactional
    public ScheduleResponseDTO updateSchedule(Long scheduleId, ScheduleRequestDTO requestDTO) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found with id: " + scheduleId));

        Doctor doctor = doctorRepository.findById(requestDTO.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + requestDTO.getDoctorId()));

        schedule.setDoctor(doctor); // Permite mudar o médico, se necessário
        schedule.setDayOfWeek(requestDTO.getDayOfWeek());
        schedule.setStartTime(requestDTO.getStartTime());
        schedule.setEndTime(requestDTO.getEndTime());

        if (schedule.getStartTime().isAfter(schedule.getEndTime()) || schedule.getStartTime().equals(schedule.getEndTime())) {
            throw new IllegalArgumentException("Start time must be before end time.");
        }

        Schedule updatedSchedule = scheduleRepository.save(schedule);
        return mapToResponseDTO(updatedSchedule);
    }

    @Transactional
    public void deleteSchedule(Long scheduleId) {
        if (!scheduleRepository.existsById(scheduleId)) {
            throw new ResourceNotFoundException("Schedule not found with id: " + scheduleId);
        }
        scheduleRepository.deleteById(scheduleId);
    }

    private ScheduleResponseDTO mapToResponseDTO(Schedule schedule) {
        return new ScheduleResponseDTO(
                schedule.getId(),
                schedule.getDoctor().getId(),
                schedule.getDoctor().getName(), // Assumindo que Doctor tem getName()
                schedule.getDayOfWeek(),
                schedule.getStartTime(),
                schedule.getEndTime()
        );
    }
}