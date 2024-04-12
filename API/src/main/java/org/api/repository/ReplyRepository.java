package org.api.repository;


import java.util.List;
import org.api.entity.CommentEntity;
import org.api.entity.ReplyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReplyRepository extends JpaRepository<ReplyEntity, Long> {
    List<ReplyEntity> findByCommentEntityCommentId(Long commentId);
}