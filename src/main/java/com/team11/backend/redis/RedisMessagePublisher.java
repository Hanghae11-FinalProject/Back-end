package com.team11.backend.redis;

import com.team11.backend.dto.chat.MessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisMessagePublisher{

    private final RedisTemplate<String,Object> redisTemplate;
//    private final ChannelTopic topic;

    public void publish(MessageDto message) {
        redisTemplate.convertAndSend(String.valueOf(message.getRoomName()), message);
    }

    public void publishToUser(Long userId, MessageDto message) {
        redisTemplate.convertAndSend(String.valueOf(userId), message);
    }
}
