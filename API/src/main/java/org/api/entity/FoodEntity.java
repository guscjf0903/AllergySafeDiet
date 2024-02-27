package org.api.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.core.dto.MenuDto;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "food_record", schema = "allergysafediet_schema")
@EntityListeners(AuditingEntityListener.class)
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
    private LocalDate date;

    @Column(name = "meal_type", nullable = false)
    private String mealType;

    @Column(name = "meal_time", nullable = false)
    private LocalTime mealTime;

    @Column(name = "food_name", nullable = false)
    private String foodName;

    @Column(name = "notes")
    private String notes;

    @Column(name = "created_at")
    @CreatedDate
    private LocalDateTime createdAt;

    public FoodEntity(UserEntity user, LocalDate date, String mealType,
                      LocalTime mealTime, String foodName, String notes) {
        this.user = user;
        this.date = date;
        this.mealType = mealType;
        this.mealTime = mealTime;
        this.foodName = foodName;
        this.notes = notes;
    }

    public static FoodEntity of(UserEntity user, MenuDto menuDto) {
        return new FoodEntity(user, menuDto.getDate(), menuDto.getMealType(),
                menuDto.getMealTime(), menuDto.getFoodName(), menuDto.getNotes());
    }

}
