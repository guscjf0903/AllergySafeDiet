package org.api.config;


import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@RequiredArgsConstructor
public class CacheConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Bean
    public RedisCacheConfiguration redisCacheConfiguration() {
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);

        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(60))
                .disableCachingNullValues()
                .serializeKeysWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer())
                )
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(serializer)
                );
    }

    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);

        return (builder) -> builder
                .withCacheConfiguration("recipes",
                        RedisCacheConfiguration.defaultCacheConfig()
                                .computePrefixWith(cacheName -> "prefix:" + cacheName + ":")
                                .entryTtl(Duration.ofHours(12))
                                .disableCachingNullValues()
                                .serializeKeysWith(
                                        RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer())
                                )
                                .serializeValuesWith(
                                        RedisSerializationContext.SerializationPair.fromSerializer(serializer)
                                ));
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory(){

        // Single 모드
        RedisStandaloneConfiguration conf = new RedisStandaloneConfiguration();

        // Cluster 모드
        // RedisClusterConfiguration conf = new RedisClusterConfiguration();

        conf.setHostName(host);
        conf.setPort(port);

        return new LettuceConnectionFactory(conf);
    }


}