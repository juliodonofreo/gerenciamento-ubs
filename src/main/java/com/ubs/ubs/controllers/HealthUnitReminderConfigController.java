package com.ubs.ubs.controllers;

import com.ubs.ubs.dtos.ReminderConfigDto;
import com.ubs.ubs.repositories.HealthUnitRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/health-units/{unitId}/reminder-config")
public class HealthUnitReminderConfigController {

    private final HealthUnitRepository healthUnitRepository; // Ou um HealthUnitService

    public HealthUnitReminderConfigController(HealthUnitRepository healthUnitRepository) {
        this.healthUnitRepository = healthUnitRepository;
    }

    @GetMapping
    public ResponseEntity<ReminderConfigDto> getReminderConfig(@PathVariable Long unitId) {
        return healthUnitRepository.findById(unitId)
                .map(unit -> {
                    ReminderConfigDto dto = new ReminderConfigDto();
                    dto.setReminderEnabled(unit.isReminderEnabled());
                    dto.setReminderLeadTimeValue(unit.getReminderLeadTimeValue());
                    dto.setReminderLeadTimeUnit(unit.getReminderLeadTimeUnit());
                    dto.setUseCustomReminderTemplate(unit.isUseCustomReminderTemplate());
                    dto.setCustomReminderTemplate(unit.getCustomReminderTemplate());
                    return ResponseEntity.ok(dto);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping
    @Transactional // Garante a atomicidade da atualização
    public ResponseEntity<Void> updateReminderConfig(
            @PathVariable Long unitId,
            @RequestBody ReminderConfigDto configDto) {

        return healthUnitRepository.findById(unitId)
                .map(unit -> {
                    unit.setReminderEnabled(configDto.isReminderEnabled());
                    unit.setReminderLeadTimeValue(configDto.getReminderLeadTimeValue());
                    unit.setReminderLeadTimeUnit(configDto.getReminderLeadTimeUnit());
                    unit.setUseCustomReminderTemplate(configDto.isUseCustomReminderTemplate());
                    if (configDto.isUseCustomReminderTemplate()) {
                        unit.setCustomReminderTemplate(configDto.getCustomReminderTemplate());
                    } else {
                        unit.setCustomReminderTemplate(null); // Limpar se não for usar
                    }
                    healthUnitRepository.save(unit);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}