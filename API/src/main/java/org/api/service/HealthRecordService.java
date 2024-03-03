package org.api.service;

import java.time.LocalDate;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.api.entity.HealthEntity;
import org.api.entity.LoginEntity;
import org.api.repository.HealthRepository;
import org.core.dto.HealthDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HealthRecordService {
    private final HealthRepository healthRepository;
    private final LoginService loginService;

    @Transactional
    public void saveHealthData(HealthDto healthDto) {
        LoginEntity loginEntity = loginService.validateLoginId(healthDto.getLoginToken());
        HealthEntity healthEntity = HealthEntity.of(loginEntity.getUser() ,healthDto);

        healthRepository.save(healthEntity);
    }


    @Transactional(readOnly = true)
    public Optional<HealthDto> getHealthDataByDate(LocalDate date) {
        HealthEntity healthEntity = healthRepository.getHealthDataByDate(date);
        if(healthEntity == null) {
            return Optional.empty();
        } else {
            HealthDto healthDto = HealthEntity.toDto(healthEntity);
            return Optional.of(healthDto);
        }
    }
}
