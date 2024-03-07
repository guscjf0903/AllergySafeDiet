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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.core.dto.HealthDto;
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
    private LocalDate healthDate;

    @Column(name = "allergies_status")
    private int allergiesStatus;

    @Column(name = "condition_status")
    private int conditionStatus;

    @Column(name = "weight")
    private int weight;

    @Column(name = "sleep_time")
    private int sleepTime;

    @Column(name = "health_notes")
    private String healthNotes;

    @Column(name = "created_at")
    @CreatedDate
    private LocalDateTime createdAt;

    public HealthEntity(UserEntity user, LocalDate healthDate, int allergiesStatus,
                        int conditionStatus, int weight, int sleepTime, String healthNotes) {
        this.user = user;
        this.healthDate = healthDate;
        this.allergiesStatus = allergiesStatus;
        this.conditionStatus = conditionStatus;
        this.weight = weight;
        this.sleepTime = sleepTime;
        this.healthNotes = healthNotes;
    }

    public static HealthEntity of(UserEntity user, HealthDto healthDto) {
        return new HealthEntity(user, healthDto.date(), healthDto.allergiesStatus(), healthDto.conditionStatus(),
                healthDto.weight(), healthDto.sleepTime(), healthDto.healthNotes());
    }

    public static HealthDto toDto(HealthEntity healthEntity) {
        return new HealthDto(healthEntity.healthDate, healthEntity.allergiesStatus, healthEntity.conditionStatus,
                healthEntity.weight, healthEntity.sleepTime, healthEntity.healthNotes);
    }

}
