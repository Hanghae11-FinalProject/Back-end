package com.team11.backend.service;


import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;

@Service
public class RedisSetCache {
    private final SetOperations<String,Object> setOperations;

    public RedisSetCache(final RedisTemplate<String, Object> redisTemplate) {
        setOperations = redisTemplate.opsForSet();
    }

    public void cacheAddBookMark(final String postId, String userId) {
        setOperations.add(postId, userId);
    }

    public Long cacheRemoveBookMark(final String postId, String userId) {
        return setOperations.remove(postId, userId);
    }

    public Boolean cacheExistBookMark(String postId, String userId) {
        return setOperations.isMember(postId, userId);
    }
}
