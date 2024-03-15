package org.api.service;

import java.time.LocalDate;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.api.entity.HealthEntity;
import org.api.entity.LoginEntity;
import org.api.repository.HealthRepository;
import org.core.dto.HealthDto;
import org.core.response.HealthResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HealthRecordService {
    private final HealthRepository healthRepository;
    private final LoginService loginService;
    private final SupplementRecordService supplementRecordService;


    @Transactional
    public void saveHealthData(HealthDto healthDto, String authorizationHeader) {
        LoginEntity loginEntity = loginService.validateLoginId(authorizationHeader);
        HealthEntity healthEntity = HealthEntity.of(loginEntity.getUser() ,healthDto);

        HealthEntity health = healthRepository.save(healthEntity);
        supplementRecordService.saveSupplementData(health, healthDto.pills());
    }


    @Transactional(readOnly = true)
    public Optional<HealthResponse> getHealthDataByDate(LocalDate date, String authorizationHeader) {
        LoginEntity loginEntity = loginService.validateLoginId(authorizationHeader);
        Optional<HealthEntity> getHealthEntity = healthRepository.getHealthDataByHealthDateAndUserUserId(date, loginEntity.getUser().getUserId());

        if(getHealthEntity.isEmpty()) {
            return Optional.empty();
        } else {
            HealthEntity healthEntity = getHealthEntity.get();

            HealthResponse healthResponse = HealthResponse.toResponse(healthEntity.getHealthDate(), healthEntity.getAllergiesStatus(),
                    healthEntity.getConditionStatus(), healthEntity.getWeight(),
                    healthEntity.getSleepTime(), healthEntity.getHealthNotes(), healthEntity.getPillsDtoList());

            return Optional.of(healthResponse);
        }
    }

    @Transactional
    public void putHealthData(HealthDto healthDto, String authorizationHeader) {
        LoginEntity loginEntity = loginService.validateLoginId(authorizationHeader);

        healthRepository.getHealthDataByHealthDateAndUserUserId(healthDto.date(),loginEntity.getUser().getUserId())
                .ifPresent(orgHealthEntity -> {
                    orgHealthEntity.healthEntityUpdate(healthDto);
                    supplementRecordService.putSupplementData(orgHealthEntity, healthDto.pills());
                });
    }
}
