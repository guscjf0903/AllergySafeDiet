package org.api.repository;

import java.util.List;
import org.api.entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<ImageEntity, Long> {
    public List<ImageEntity> findByPostPostId(Long postId);
}

