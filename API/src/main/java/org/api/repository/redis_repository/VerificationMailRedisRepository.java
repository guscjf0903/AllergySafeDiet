package org.api.repository.redis_repository;

import org.api.entity.redis_entity.VerificationMailRedisEntity;
import org.springframework.data.repository.CrudRepository;

public interface VerificationMailRedisRepository extends CrudRepository<VerificationMailRedisEntity, String> {
}
