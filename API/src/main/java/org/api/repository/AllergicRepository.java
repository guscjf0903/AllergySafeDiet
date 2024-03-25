package org.api.repository;

import org.api.entity.AllergyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AllergicRepository extends JpaRepository<AllergyEntity, Long> {
    void deleteByUserUserId(Long userId);
}
