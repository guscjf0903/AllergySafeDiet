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
@Table(name = "supplements", schema = "allergysafediet_schema")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SupplementEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "supplement_id")
    private Long supplementId;

    @ManyToOne
    @JoinColumn(name = "health_record_id", nullable = false)
    private HealthEntity health;

    @Column(name = "supplement_name", nullable = false)
    private String supplementName;

    @Column(name = "supplement_count", nullable = false)
    private int supplementCount;

    public SupplementEntity(HealthEntity health, String supplementName, int supplementCount) {
        this.health = health;
        this.supplementName = supplementName;
        this.supplementCount = supplementCount;
    }

    public static SupplementEntity of(HealthEntity health, String supplementName, int supplementCount) {
        return new SupplementEntity(health, supplementName, supplementCount);
    }
}
