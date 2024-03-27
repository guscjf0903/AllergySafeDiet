package org.api.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.core.response.IngredientsResponse;
import org.core.request.FoodRequest;

@Entity
@Table(name = "food_record", schema = "allergysafediet_schema")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FoodEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "food_record_id")
    private Long foodRecordId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "food_date", nullable = false)
    private LocalDate foodDate;

    @Column(name = "meal_type", nullable = false)
    private String mealType;

    @Column(name = "meal_time", nullable = false)
    private LocalTime mealTime;

    @Column(name = "food_name", nullable = false)
    private String foodName;

    @Column(name = "food_notes")
    private String foodNotes;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Long createdAt;

    @OneToMany(mappedBy = "food", cascade = CascadeType.ALL)
    private List<IngredientEntity> ingredientEntities;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now().getEpochSecond();
    }

    public List<IngredientsResponse> getIngredientsDtoList() {
        if (ingredientEntities == null) {
            return List.of(); // supplements가 null인 경우, 빈 리스트 반환
        }
        return ingredientEntities.stream()
                .map(ingredient -> new IngredientsResponse(ingredient.getIngredientName()))
                .collect(Collectors.toList());
    }

    public FoodEntity(UserEntity user, LocalDate foodDate, String mealType,
                      LocalTime mealTime, String foodName, String foodNotes) {
        this.user = user;
        this.foodDate = foodDate;
        this.mealType = mealType;
        this.mealTime = mealTime;
        this.foodName = foodName;
        this.foodNotes = foodNotes;
    }

    public static FoodEntity of(UserEntity user, FoodRequest foodRequest) {
        return new FoodEntity(user, foodRequest.date(), foodRequest.mealType(),
                foodRequest.mealTime(), foodRequest.foodName(), foodRequest.notes());
    }

    public void foodEntityUpdate(FoodRequest foodRequest) {
        this.foodDate = foodRequest.date();
        this.mealType = foodRequest.mealType();
        this.mealTime = foodRequest.mealTime();
        this.foodName = foodRequest.foodName();
        this.foodNotes = foodRequest.notes();

    }

}
