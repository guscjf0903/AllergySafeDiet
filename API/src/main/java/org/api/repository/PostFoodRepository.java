package org.api.repository;

import java.util.List;
import org.api.entity.PostFoodEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostFoodRepository extends JpaRepository<PostFoodEntity, Long> {
    List<PostFoodEntity> findByPostPostId(Long postId);
}
