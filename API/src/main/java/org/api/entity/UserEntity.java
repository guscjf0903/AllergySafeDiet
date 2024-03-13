package org.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.core.dto.SignupDto;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "user", schema = "allergysafediet_schema")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user_name", unique = true)
    private String userName;

    @Column(name = "password")
    private String password;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "birthdate")
    private Date birthDate;

    @Column(name = "gender")
    private String gender;

    @Column(name = "height")
    private int height;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false) // created_at 컬럼 매핑
    private Instant createdAt;

    public UserEntity(String userName, String password, String email, Date birthDate, String gender, int height) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.birthDate = birthDate;
        this.gender = gender;
        this.height = height;
    }

    public static UserEntity of(SignupDto signupDto) {
        return new UserEntity(signupDto.userName(), signupDto.password(), signupDto.email(),
                signupDto.birthDate(), signupDto.gender(), signupDto.height());
    }
}
