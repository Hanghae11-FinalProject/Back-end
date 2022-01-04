package com.team11.backend.controller;

import com.team11.backend.redis.RedisMessageSubscriber;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class RoomController {

    private final RedisMessageSubscriber redisMessageSubscriber;
    private final RedisMessageListenerContainer redisMessageListenerContainer;
    private final Map<String, ChannelTopic> topics;

    @MessageMapping("/join")
    public void join(String roomId){
        String roomId2 = roomId.replaceAll("\"","");
        ChannelTopic topic = topics.get(roomId2);
        if(topic == null){
            topic = new ChannelTopic(roomId2);
            redisMessageListenerContainer.addMessageListener(redisMessageSubscriber,topic);
        }
    }
}
