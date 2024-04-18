package org.api.service.post;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.api.entity.PostEntity;
import org.api.entity.UserEntity;
import org.api.repository.PostRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VisitRedisService {
    private static final String VISITED_POSTS_KEY_PREFIX = "userVisitedPosts:";
    private final RedisTemplate<String, Object> redisTemplate;
    private final PostRepository postRepository;

    public void addVisitedRedis(UserEntity visitor, PostEntity postEntity) {
        String visitedKey = VISITED_POSTS_KEY_PREFIX + visitor.getUserId();
        Boolean visited = redisTemplate.opsForSet().isMember(visitedKey, postEntity.getPostId());

        if (visited == null || !visited) {
            postRepository.updateViews(postEntity.getPostId());
            redisTemplate.opsForSet().add(visitedKey, postEntity.getPostId());
            redisTemplate.expire(visitedKey, 24, TimeUnit.HOURS);
        }
    }
}
