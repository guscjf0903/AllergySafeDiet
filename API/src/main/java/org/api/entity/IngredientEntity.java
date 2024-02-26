package org.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "ingredients", schema = "allergysafediet_schema")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class IngredientEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ingredient_id")
    private Long ingredientId;

    @ManyToOne
    @JoinColumn(name = "food_record_id", nullable = false)
    private FoodEntity food;

    @Column(name = "ingredient_name", nullable = false)
    private String ingredientName;

    public IngredientEntity(FoodEntity food, String ingredientName) {
        this.food = food;
        this.ingredientName = ingredientName;
    }

    public static IngredientEntity of(FoodEntity foodEntity, String ingredientName) {
        return new IngredientEntity(foodEntity, ingredientName);
    }
}
