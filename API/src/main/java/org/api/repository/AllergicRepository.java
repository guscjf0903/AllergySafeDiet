package org.api.repository;

import org.api.entity.AllergyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AllergicRepository extends JpaRepository<AllergyEntity, Long> {
    void deleteByUserUserId(Long userId);
}
