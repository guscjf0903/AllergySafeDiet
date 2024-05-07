package org.api.repository;

import java.util.Optional;
import org.api.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {
    @Modifying
    @Query("update PostEntity p set p.views = p.views + 1 where p.postId = :postId")
    void updateViews(Long postId);

    @Query("SELECT DISTINCT p FROM PostEntity p " +
            "JOIN FETCH p.postFoodEntities pf " +
            "JOIN FETCH pf.food f " +
            "JOIN FETCH f.ingredientEntities WHERE p.postId = :postId ")
    Optional<PostEntity> findByPostId(Long postId);
}
