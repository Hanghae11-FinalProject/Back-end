package com.team11.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;


@Service
public class RedisValueCache {

    private final RedisTemplate<String,Object> redisTemplate;
    private final ValueOperations<String,Object> valueOperations;

    @Autowired
    public RedisValueCache(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        valueOperations = redisTemplate.opsForValue();

    }

    public void cache(final String key, final Object data) {
        valueOperations.set(key, data);
    }

    public Object getCachedValue(final String key) {
        return valueOperations.get(key);
    }

    public void deleteCachedValue(final String key) {
        valueOperations.getOperations().delete(key);
    }

}
