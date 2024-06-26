package org.api.service;

import static org.api.exception.ErrorCodes.DELETE_FOOD_DATA_FAILED;
import static org.api.exception.ErrorCodes.DELETE_HEALTH_DATA_FAILED;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.api.entity.FoodEntity;
import org.api.entity.HealthEntity;
import org.api.entity.LoginEntity;
import org.api.entity.UserEntity;
import org.api.exception.CustomException;
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
                        healthEntity.getHealthRecordId(),
                        healthEntity.getHealthDate(),
                        healthEntity.getAllergiesStatus(),
                        healthEntity.getConditionStatus(),
                        healthEntity.getWeight(),
                        healthEntity.getSleepTime(),
                        healthEntity.getHealthNotes(),
                        healthEntity.getPillsDtoList()
                ));
    }
    @Transactional(readOnly = true)
    public List<HealthEntity> getHealthDataByIds(List<Long> ids) {
        Optional<List<HealthEntity>> getHealthEntities = healthRepository.findByHealthRecordIdIn(ids);
        return getHealthEntities.orElse(null);

    }

    @Transactional
    public void putHealthData(HealthRequest healthRequest, UserEntity userEntity) {
        healthRepository.getHealthDataByHealthDateAndUserUserId(healthRequest.date(), userEntity.getUserId())
                .ifPresent(orgHealthEntity -> {
                    orgHealthEntity.healthEntityUpdate(healthRequest);
                    supplementRecordService.putSupplementData(orgHealthEntity, healthRequest.pills());
                });
    }

    @Transactional
    public void deleteHealthDataByDate(LocalDate date, UserEntity userEntity) {
        try {
            healthRepository.deleteHealthDataByHealthDateAndUserUserId(date, userEntity.getUserId());
        } catch (Exception e) {
            throw new CustomException(DELETE_HEALTH_DATA_FAILED);
        }
    }

}
