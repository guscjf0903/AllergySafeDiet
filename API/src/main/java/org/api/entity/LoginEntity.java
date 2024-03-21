package org.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.Instant;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "login", schema = "allergysafediet_schema")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LoginEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "login_id")
    private Long loginId;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "login_token" , nullable = false, unique = true)
    private String loginToken;

    @Column(name = "token_expiration_time", nullable = false)
    private LocalDateTime tokenExpirationTime;

    @Column(name = "login_time", nullable = false, updatable = false)
    private Long loginTime;

    @PrePersist
    protected void onCreate() {
        this.loginTime = Instant.now().toEpochMilli();
    }

    public LoginEntity(UserEntity user, String loginToken, LocalDateTime tokenExpirationTime) {
        this.user = user;
        this.loginToken = loginToken;
        this.tokenExpirationTime = tokenExpirationTime;
    }

    public static LoginEntity of(UserEntity user, String loginToken, LocalDateTime tokenExpirationTime) {
        return new LoginEntity(user, loginToken, tokenExpirationTime);
    }

}