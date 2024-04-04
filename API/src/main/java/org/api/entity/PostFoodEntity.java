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
@Table(name = "post_food", schema = "allergysafediet_schema")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PostFoodEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_food_id")
    private Long postFoodId;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private PostEntity post;

    @ManyToOne
    @JoinColumn(name = "food_id", nullable = false)
    private FoodEntity food;


    @Column(name = "created_at", nullable = false, updatable = false)
    private Long createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now().toEpochMilli();
    }

    public PostFoodEntity(PostEntity post, FoodEntity food) {
        this.post = post;
        this.food = food;
    }
}
