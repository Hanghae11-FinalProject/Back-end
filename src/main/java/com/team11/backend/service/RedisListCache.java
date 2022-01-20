package com.team11.backend.service;

import com.team11.backend.dto.querydto.CategoryQueryDto;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RedisListCache {
    private final ListOperations<String,Object> listOperations;

    public RedisListCache(final RedisTemplate<String, Object> redisTemplate) {
        listOperations = redisTemplate.opsForList();
    }

    public void cacheCategory(final String key, final List<CategoryQueryDto> categoryQueryDtos) {
        for (CategoryQueryDto categoryQueryDto : categoryQueryDtos) {
            listOperations.leftPush(key, categoryQueryDto);
        }
    }

    public List<CategoryQueryDto> getCategoryDto(final String key) {
        final List<Object> objects = listOperations.range(key, 0, 5);
        if (CollectionUtils.isEmpty(objects)) {
            return Collections.emptyList();
        }
        return objects.stream()
                .map(x -> (CategoryQueryDto) x)
                .collect(Collectors.toList());
    }

    public CategoryQueryDto getLastElement(final String key) {
        final Object o = listOperations.rightPop(key);
        if (o == null) {
            return null;
        }
        return (CategoryQueryDto) o;
    }

    public boolean isExist(String key) {
        return listOperations.getOperations().hasKey(key);
    }
}
