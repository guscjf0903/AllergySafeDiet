package org.api.service;

import java.time.LocalDate;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.api.entity.HealthEntity;
import org.api.repository.HealthRepository;
import org.core.dto.HealthDto;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HealthRecordService {
    private final HealthRepository healthRepository;
    public Optional<HealthDto> getHealthDataByDate(LocalDate date) {
        HealthEntity healthEntity = healthRepository.getHealthDataByDate(date);
        HealthDto healthDto = setHealthDto(healthEntity);

        return Optional.of(healthDto);
    }

    private HealthDto setHealthDto(HealthEntity healthEntity) {
        HealthDto healthDto = new HealthDto();
        healthDto.setDate(healthEntity.getDate());
        healthDto.setAllergiesStatus(healthEntity.getAllergiesStatus());
        healthDto.setConditionStatus(healthEntity.getConditionStatus());
        healthDto.setWeight(healthEntity.getWeight());
        healthDto.setSleepTime(healthEntity.getSleepTime());
        healthDto.setHealthNotes(healthEntity.getNotes());

        return healthDto;
    }
}
