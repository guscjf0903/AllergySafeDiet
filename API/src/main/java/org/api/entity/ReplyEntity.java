package org.api.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "replies", schema = "allergysafediet_schema")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReplyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_id")
    private Long replyId;

    @Column(name = "reply_text", nullable = false)
    private String replyText;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "comment_id", nullable = false)
    private CommentEntity commentEntity;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Long createdAt;


    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now().getEpochSecond();
    }
    public ReplyEntity(CommentEntity commentEntity,UserEntity user, String replyText) {
        this.commentEntity = commentEntity;
        this.user = user;
        this.replyText = replyText;
    }

    public static ReplyEntity of(CommentEntity commentEntity,UserEntity user, String replyText) {
        return new ReplyEntity(commentEntity, user, replyText);
    }

}
