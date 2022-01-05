package com.team11.backend.repository;

import com.team11.backend.redis.RedisMessageSubscriber;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Repository;

import java.util.Map;

@RequiredArgsConstructor
@Repository
public class RoomRepositoryImpl {

    private final RedisMessageListenerContainer redisMessageListenerContainer;
    private final RedisMessageSubscriber redisMessageSubscriber;
    private Map<String, ChannelTopic> topics;

    //채팅방 입장
    public void enterChatRoom(String roomId){
        ChannelTopic topic = topics.get(roomId);
        if (topic == null){
            topic = new ChannelTopic(roomId);
            redisMessageListenerContainer.addMessageListener(redisMessageSubscriber, topic);
            topics.put(roomId, topic);
        }
    }
}
