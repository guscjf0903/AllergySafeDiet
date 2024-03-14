package org.api.repository;

import org.api.entity.LoginEntity;
import org.api.entity.SupplementEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplementRepository extends JpaRepository<SupplementEntity, Long> {

}
