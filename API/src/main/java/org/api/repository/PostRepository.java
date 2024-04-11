package org.api.repository;

import org.api.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<PostEntity, Long> {
    @Modifying
    @Query("update PostEntity p set p.views = p.views + 1 where p.postId = :postId")
    void updateViews(Long postId);

    PostEntity findByPostId(Long postId);
}
