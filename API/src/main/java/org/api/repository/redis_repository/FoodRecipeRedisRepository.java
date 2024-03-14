package org.api.repository.redis_repository;

import org.api.entity.redis_entity.FoodRecipeRedisEntity;
import org.springframework.data.repository.CrudRepository;

public interface FoodRecipeRedisRepository extends CrudRepository<FoodRecipeRedisEntity, String> {
}
