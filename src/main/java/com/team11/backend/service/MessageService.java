package com.team11.backend.service;
import com.team11.backend.model.Message;
import com.team11.backend.redis.RedisMessagePublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MessageService {

    private final RedisMessagePublisher messagePublisher;

    public void sendMessage(Message message) {
        if (Message.MessageType.Start.equals(message.getMessageType())) {
            Message startMessage = Message.builder()
                    .content(message.getUser().getUsername() + "님이 입장")
                    .user(message.getUser())
                    .build();
            messagePublisher.publish(startMessage);
        }else if (Message.MessageType.Exit.equals(message.getMessageType())) {
            Message exitMessage = Message.builder()
                    .content(message.getUser().getUsername() + "님이 퇴장")
                    .user(message.getUser())
                    .build();
            messagePublisher.publish(exitMessage);
        }else if (Message.MessageType.Talk.equals(message.getMessageType())) {
             messagePublisher.publish(message);
        }
    }
}