package com.team11.backend.controller;

import com.team11.backend.dto.chat.MessageDto;
import com.team11.backend.model.Message;
import com.team11.backend.repository.RoomRepository;
import com.team11.backend.repository.UserRepository;
import com.team11.backend.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MessageController {
    private final MessageService messageService;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
 
    //pub/api/message 클라이언트 요청으로 메세지 발행
    @MessageMapping("/message")
    public void message(MessageDto messageDto){
        Message message = new Message(messageDto,userRepository,roomRepository);
        messageService.sendMessage(message);
    }
}
