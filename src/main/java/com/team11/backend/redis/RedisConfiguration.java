package com.team11.backend.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@RequiredArgsConstructor
public class RedisConfiguration {
    private final RedisConnectionFactory redisConnectionFactory;
    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory redisConnectionFactory){
        RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
        redisMessageListenerContainer.setConnectionFactory(redisConnectionFactory);
//        redisMessageListenerContainer.addMessageListener(messageListenerAdapter,topic());
        return redisMessageListenerContainer;
    }

//    @Bean
//    MessageListenerAdapter messageListenerAdapter(){
//        return new MessageListenerAdapter(redisMessageSubscriber,"onMessage");
//    }
//
//    @Bean
//    ChannelTopic topic(){
//        return new ChannelTopic("stackfortech");
//    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(String.class));
        return redisTemplate;
    }

//    @Bean
//    RedisMessagePublisher messagePublisher(){
//        return new RedisMessagePublisher(redisTemplate(redisConnectionFactory),topic());
//    }
}
