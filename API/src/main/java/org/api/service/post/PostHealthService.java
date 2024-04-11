package org.api.service.post;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.api.entity.HealthEntity;
import org.api.entity.PostEntity;
import org.api.entity.PostHealthEntity;
import org.api.repository.PostHealthRepository;
import org.api.service.HealthRecordService;
import org.core.request.PostRequest;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PostHealthService {
    private final HealthRecordService healthRecordService;
    private final PostHealthRepository postHealthRepository;
    public void savePostHealth(PostEntity postEntity, PostRequest postRequest) {
        List<HealthEntity> healthEntities = healthRecordService.getHealthDataByIds(postRequest.healthIds());
        if (healthEntities.isEmpty()) {
            return;
        }
        for (HealthEntity healthEntity : healthEntities) {
            PostHealthEntity postHealthEntity = new PostHealthEntity(postEntity, healthEntity);
            postHealthRepository.save(postHealthEntity);
        }
    }
}
