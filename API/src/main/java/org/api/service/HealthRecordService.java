package org.api.service;

import java.time.LocalDate;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.api.entity.HealthEntity;
import org.api.entity.LoginEntity;
import org.api.entity.UserEntity;
import org.api.repository.HealthRepository;
import org.core.request.HealthRequest;
import org.core.response.HealthResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HealthRecordService {
    private final HealthRepository healthRepository;
    private final SupplementRecordService supplementRecordService;

    @Transactional
    public void saveHealthData(HealthRequest healthRequest, UserEntity userEntity) {
        HealthEntity healthEntity = HealthEntity.of(userEntity, healthRequest);
        HealthEntity savedHealth = healthRepository.save(healthEntity);
        supplementRecordService.saveSupplementData(savedHealth, healthRequest.pills());
    }

    @Transactional(readOnly = true)
    public Optional<HealthResponse> getHealthDataByDate(LocalDate date, UserEntity userEntity) {
        return healthRepository.getHealthDataByHealthDateAndUserUserId(date,
                        userEntity.getUserId())
                .map(healthEntity -> new HealthResponse(
                        healthEntity.getHealthDate(),
                        healthEntity.getAllergiesStatus(),
                        healthEntity.getConditionStatus(),
                        healthEntity.getWeight(),
                        healthEntity.getSleepTime(),
                        healthEntity.getHealthNotes(),
                        healthEntity.getPillsDtoList()
                ));
    }

    @Transactional
    public void putHealthData(HealthRequest healthRequest, UserEntity userEntity) {
        healthRepository.getHealthDataByHealthDateAndUserUserId(healthRequest.date(), userEntity.getUserId())
                .ifPresent(orgHealthEntity -> {
                    orgHealthEntity.healthEntityUpdate(healthRequest);
                    supplementRecordService.putSupplementData(orgHealthEntity, healthRequest.pills());
                });
    }
}
