package org.api.repository;

import java.util.List;
import org.api.entity.PostHealthEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostHealthRepository extends JpaRepository<PostHealthEntity, Long> {
    List<PostHealthEntity> findByPostPostId(Long postId);
}
