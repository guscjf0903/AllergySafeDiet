package org.api.entity.redis_entity;

import org.springframework.data.annotation.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "verificationMail", timeToLive = 300)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class VerificationMailRedisEntity {
    @Id
    private String email;
    private String verificationCode;
}
