package com.team11.backend.redis;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.team11.backend.dto.chat.MessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisMessageSubscriber implements MessageListener {
    private final RedisTemplate<String,Object> redisTemplate;
    private final SimpMessageSendingOperations messagingTemplate;
    private final ObjectMapper objectMapper;


    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
        String publishMessage = (String) redisTemplate.getStringSerializer().deserialize(message.getBody());
        System.out.println(publishMessage+"111111");
            com.team11.backend.model.Message message1 = objectMapper.readValue(publishMessage, com.team11.backend.model.Message.class);
            MessageDto mssages = MessageDto.builder()
                    .message(message1.getContent())
                    .sender(message1.getUser().getUsername())
                    .roomName(message1.getRoom().getRoomName())
                    .type(message1.getMessageType())
                    .build();

            messagingTemplate.convertAndSend("/sub/"+mssages.getRoomName(), mssages);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
