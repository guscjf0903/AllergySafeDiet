package org.api.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.api.entity.HealthEntity;
import org.api.entity.SupplementEntity;
import org.api.repository.SupplementRepository;
import org.core.request.PillsRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SupplementRecordService {
    private final SupplementRepository supplementRepository;

    @Transactional
    public void saveSupplementData(HealthEntity health, List<PillsRequest> pillsRequest) {
        persistSupplementEntities(health, pillsRequest);
    }

    @Transactional
    public void putSupplementData(HealthEntity health, List<PillsRequest> pillsRequest) {
        if (pillsRequest == null || pillsRequest.isEmpty()) {
            return;
        }

        supplementRepository.deleteByHealthHealthRecordId(health.getHealthRecordId());
        persistSupplementEntities(health, pillsRequest);
    }

    private void persistSupplementEntities(HealthEntity health, List<PillsRequest> pillsRequest) {
        if (pillsRequest == null || pillsRequest.isEmpty()) {
            return;
        }
        pillsRequest.forEach(pill -> {
            SupplementEntity supplementEntity = SupplementEntity.of(health, pill.name(), pill.count());
            supplementRepository.save(supplementEntity);
        });
    }


}
