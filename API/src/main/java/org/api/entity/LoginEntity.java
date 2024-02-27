package org.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "login", schema = "allergysafediet_schema")
@EntityListeners(AuditingEntityListener.class)
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

    @Column(name = "token_expirationtime", nullable = false)
    private LocalDateTime tokenExpirationTime;

    @Column(name = "created_at")
    @CreatedDate
    private LocalDateTime loginTime;

    public LoginEntity(UserEntity user, String loginToken, LocalDateTime tokenExpirationTime) {
        this.user = user;
        this.loginToken = loginToken;
        this.tokenExpirationTime = tokenExpirationTime;
    }

    public static LoginEntity of(UserEntity user, String loginToken, LocalDateTime tokenExpirationTime) {
        return new LoginEntity(user, loginToken, tokenExpirationTime);
    }

}