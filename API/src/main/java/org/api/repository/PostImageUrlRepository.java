package org.api.repository;

import java.util.List;
import org.api.entity.PostHealthEntity;
import org.api.entity.PostImageUrlEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostImageUrlRepository extends JpaRepository<PostImageUrlEntity, Long> {
    List<PostImageUrlEntity> findByPostPostId(Long postId);
}
