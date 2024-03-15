package org.api.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.api.entity.HealthEntity;
import org.api.entity.SupplementEntity;
import org.api.repository.SupplementRepository;
import org.core.dto.PillsDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SupplementRecordService {
    private final SupplementRepository supplementRepository;

    @Transactional
    public void saveSupplementData(HealthEntity health, List<PillsDto> pillsDto) {
        if(pillsDto == null || pillsDto.isEmpty()) return;

        pillsDto.forEach(pill -> {
            SupplementEntity supplementEntity = SupplementEntity.of(health, pill.name(), pill.count());
            supplementRepository.save(supplementEntity);
        });
    }

    @Transactional
    public void putSupplementData(HealthEntity health, List<PillsDto> pillsDto) {
        if(pillsDto == null || pillsDto.isEmpty()) return;

        supplementRepository.deleteByHealthHealthRecordId(health.getHealthRecordId());

        pillsDto.forEach(pill -> {
            SupplementEntity supplementEntity = SupplementEntity.of(health, pill.name(), pill.count());
            supplementRepository.save(supplementEntity);
        });
    }
}
