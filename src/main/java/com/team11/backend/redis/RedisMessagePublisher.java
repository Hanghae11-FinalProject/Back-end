package com.team11.backend.redis;


import com.team11.backend.model.Message;
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

    public void publish(Message message) {
        redisTemplate.convertAndSend(String.valueOf(message.getRoom().getRoomName()), message);
    }
}
