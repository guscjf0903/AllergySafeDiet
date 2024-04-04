package org.api.repository;

import org.api.entity.PostHealthEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostHealthRepository extends JpaRepository<PostHealthEntity, Long> {
}
