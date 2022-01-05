package com.team11.backend.controller;


import com.team11.backend.dto.NewMessage;
import com.team11.backend.redis.RedisMessagePublisher;
import com.team11.backend.redis.RedisMessageSubscriber;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class RedisController {

    private  static Logger logger = LoggerFactory.getLogger(RedisController.class);
    private final RedisMessagePublisher messagePublisher;
    private final RedisMessageSubscriber redisMessageSubscriber;
    private final RedisMessageListenerContainer redisMessageListenerContainer;
    private final Map<String, ChannelTopic> topics;

//    @PostMapping("/publish")
//    public void publish(@RequestBody Message message){
//        logger.info(">> publishing : {}",message);
//        messagePublisher.publish(message.toString());
//    }
//
//    @GetMapping("/subscribe")
//    public List<String> getMessage(){
//        return RedisMessageSubscriber.messageList;
//    }

    @MessageMapping("/join")
    public void join(String roomId){
        System.out.println(roomId);
        String roomId2 = roomId.replaceAll("\"","");
        ChannelTopic topic = topics.get(roomId2);
        System.out.println(topic);
        if(topic == null){
            topic = new ChannelTopic(roomId2);
            redisMessageListenerContainer.addMessageListener(redisMessageSubscriber,topic);
        }
    }

    @MessageMapping("/message")
    public void message(NewMessage message){
        messagePublisher.publish(message);
    }
}
