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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.core.dto.HealthDto;
import org.core.dto.MenuDto;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "health_record", schema = "allergysafediet_schema")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class HealthEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "health_record_id")
    private Long healthRecordId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "health_date", nullable = false)
    private LocalDate date;

    @Column(name = "allergies")
    private int allergiesStatus;

    @Column(name = "condition")
    private int conditionStatus;

    @Column(name = "weight")
    private int weight;

    @Column(name = "sleeptime")
    private int sleepTime;

    @Column(name = "health_notes")
    private String notes;

    @Column(name = "created_at")
    @CreatedDate
    private LocalDateTime createdAt;

    public HealthEntity(UserEntity user, LocalDate date, int allergiesStatus,
                        int conditionStatus, int weight, int sleepTime, String notes) {
        this.user = user;
        this.date = date;
        this.allergiesStatus = allergiesStatus;
        this.conditionStatus = conditionStatus;
        this.weight = weight;
        this.sleepTime = sleepTime;
        this.notes = notes;
    }

    public static HealthEntity of(UserEntity user, HealthDto healthDto) {
        return new HealthEntity(user, healthDto.getDate(), healthDto.getAllergiesStatus(),
                healthDto.getConditionStatus(), healthDto.getWeight(), healthDto.getSleepTime(), healthDto.getHealthNotes());
    }

    public static HealthDto toDto(HealthEntity healthEntity) {
        HealthDto dto = new HealthDto();
        dto.setDate(healthEntity.date);
        dto.setAllergiesStatus(healthEntity.allergiesStatus);
        dto.setConditionStatus(healthEntity.conditionStatus);
        dto.setWeight(healthEntity.weight);
        dto.setSleepTime(healthEntity.sleepTime);
        dto.setHealthNotes(healthEntity.notes);

        return dto;
    }

}
