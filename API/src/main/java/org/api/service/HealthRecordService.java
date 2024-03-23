package org.api.service;

import java.time.LocalDate;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.api.entity.HealthEntity;
import org.api.entity.LoginEntity;
import org.api.repository.HealthRepository;
import org.core.dto.HealthRequest;
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
    public void saveHealthData(HealthRequest healthRequest, String authorizationHeader) {
        LoginEntity loginEntity = getUserFromAuthorization(authorizationHeader);
        HealthEntity healthEntity = HealthEntity.of(loginEntity.getUser(), healthRequest);
        HealthEntity savedHealth = healthRepository.save(healthEntity);
        supplementRecordService.saveSupplementData(savedHealth, healthRequest.pills());
    }

    @Transactional(readOnly = true)
    public Optional<HealthResponse> getHealthDataByDate(LocalDate date, String authorizationHeader) {
        return healthRepository.getHealthDataByHealthDateAndUserUserId(date,
                        getUserFromAuthorization(authorizationHeader).getUser().getUserId())
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
    public void putHealthData(HealthRequest healthRequest, String authorizationHeader) {
        LoginEntity loginEntity = getUserFromAuthorization(authorizationHeader);
        healthRepository.getHealthDataByHealthDateAndUserUserId(healthRequest.date(), loginEntity.getUser().getUserId())
                .ifPresent(orgHealthEntity -> {
                    orgHealthEntity.healthEntityUpdate(healthRequest);
                    supplementRecordService.putSupplementData(orgHealthEntity, healthRequest.pills());
                });
    }

    private LoginEntity getUserFromAuthorization(String authorizationHeader) {
        return loginService.validateLoginId(authorizationHeader);
    }
}
