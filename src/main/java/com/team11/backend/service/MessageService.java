package com.team11.backend.service;
import com.team11.backend.dto.chat.MessageDto;
import com.team11.backend.model.Message;
import com.team11.backend.redis.RedisMessagePublisher;
import com.team11.backend.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class MessageService {

    private final RedisMessagePublisher messagePublisher;
    private final MessageRepository messageRepository;

    public void sendMessage(Message message) {
        if (Message.MessageType.Start.equals(message.getMessageType())) {
            MessageDto messages = MessageDto.builder()
                    .message(message.getUser().getUsername() + "님이 입장하셨습니다.")
                    .sender(message.getUser().getNickname())
                    .roomName(message.getRoom().getRoomName())
                    .type(message.getMessageType())
                    .build();
            messagePublisher.publish(messages);


            messagePublisher.publish(messages);
        } else if (Message.MessageType.Exit.equals(message.getMessageType())) {
            MessageDto exitMessage = MessageDto.builder()
                    .message(message.getUser().getUsername() + "님이 퇴장하셨습니다.")
                    .sender(message.getUser().getNickname())
                    .roomName(message.getRoom().getRoomName())
                    .type(message.getMessageType())
                    .build();
            messagePublisher.publish(exitMessage);

        } else if (Message.MessageType.Talk.equals(message.getMessageType())) {
            MessageDto talkMessage = MessageDto.builder()
                    .message(message.getContent())
                    .sender(message.getUser().getNickname())
                    .roomName(message.getRoom().getRoomName())
                    .type(message.getMessageType())
                    .build();
            messageRepository.save(message);
            messagePublisher.publish(talkMessage);
        }

    }
}