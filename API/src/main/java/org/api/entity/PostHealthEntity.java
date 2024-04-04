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
@Table(name = "post_health", schema = "allergysafediet_schema")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PostHealthEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_health_id")
    private Long postHealthId;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private PostEntity post;

    @ManyToOne
    @JoinColumn(name = "health_id", nullable = false)
    private HealthEntity health;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Long createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now().toEpochMilli();
    }

    public PostHealthEntity(PostEntity post, HealthEntity health) {
        this.post = post;
        this.health = health;
    }
}

