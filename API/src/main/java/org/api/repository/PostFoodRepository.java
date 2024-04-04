package org.api.repository;

import org.api.entity.PostFoodEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostFoodRepository extends JpaRepository<PostFoodEntity, Long> {
}
