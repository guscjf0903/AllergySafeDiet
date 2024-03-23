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
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.core.dto.HealthRequest;
import org.core.dto.PillsRequest;

@Entity
@Table(name = "health_record", schema = "allergysafediet_schema")
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

    @OneToMany(mappedBy = "health", cascade = CascadeType.ALL)
    private List<SupplementEntity> supplements;


    @Column(name = "created_at", nullable = false, updatable = false)
    private Long createdAt;

    @Column(name = "updated_at")
    private Long updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now().toEpochMilli();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now().toEpochMilli();
    }


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

    public List<PillsRequest> getPillsDtoList() {
        if (supplements == null) {
            return List.of(); // supplements가 null인 경우, 빈 리스트 반환
        }
        return supplements.stream()
                .map(supplement -> new PillsRequest(supplement.getSupplementName(), supplement.getSupplementCount()))
                .collect(Collectors.toList());
    }

    public static HealthEntity of(UserEntity user, HealthRequest healthRequest) {
        return new HealthEntity(user, healthRequest.date(), healthRequest.allergiesStatus(), healthRequest.conditionStatus(),
                healthRequest.weight(), healthRequest.sleepTime(), healthRequest.healthNotes());
    }

    public void healthEntityUpdate(HealthRequest healthRequest) {
        this.allergiesStatus = healthRequest.allergiesStatus();
        this.conditionStatus = healthRequest.conditionStatus();
        this.weight = healthRequest.weight();
        this.sleepTime = healthRequest.sleepTime();
        this.healthNotes = healthRequest.healthNotes();
    }

}
