package com.sky.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author sharkCode
 * @date 2025/5/7 14:55
 */
@Configuration
@Slf4j
public class RedisConfig {
    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        log.info("开始创建 Redis 模版对象");
        RedisTemplate redisTemplate = new RedisTemplate();
        // 设置 Redis 的连接工厂对象
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // 设置 RedisKey 的序列化器
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        return redisTemplate;
    }
}
