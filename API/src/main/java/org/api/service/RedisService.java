package org.api.service;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final StringRedisTemplate stringRedisTemplate;

    public void saveVerificationCode(String email, String code) {
        String key = "verificationCode:" + email;
        ValueOperations<String, String> valueOps = stringRedisTemplate.opsForValue();
        valueOps.set(key, code, 5, TimeUnit.MINUTES); // 코드는 5분 후 만료.
    }

    public String getVerificationCode(String email) {
        String key = "verificationCode:" + email;
        return stringRedisTemplate.opsForValue().get(key);
    }

    public void deleteVerificationCode(String email) {
        String key = "verificationCode:" + email;
        stringRedisTemplate.delete(key);
    }


}
