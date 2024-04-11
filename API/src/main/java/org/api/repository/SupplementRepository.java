package org.api.repository;

import org.api.entity.LoginEntity;
import org.api.entity.SupplementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplementRepository extends JpaRepository<SupplementEntity, Long> {
    void deleteByHealthHealthRecordId(long healthId);

}
