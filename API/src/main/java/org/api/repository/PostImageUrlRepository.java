package org.api.repository;

import org.api.entity.PostHealthEntity;
import org.api.entity.PostImageUrlEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostImageUrlRepository extends JpaRepository<PostImageUrlEntity, Long> {
}
