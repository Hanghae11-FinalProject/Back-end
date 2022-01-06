package com.team11.backend.controller;

import com.team11.backend.dto.RoomDto;
import com.team11.backend.dto.ShowMessageDto;
import com.team11.backend.dto.chat.MessageDto;
import com.team11.backend.model.Message;
import com.team11.backend.repository.RoomRepository;
import com.team11.backend.repository.UserRepository;
import com.team11.backend.security.UserDetailsImpl;
import com.team11.backend.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
public class MessageController {
    private final MessageService messageService;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;

    @MessageMapping("/message")
    public void message(MessageDto messageDto){
        Message message = new Message(messageDto,userRepository,roomRepository);
        messageService.sendMessage(message);
    }

    //pub/api/message 클라이언트 요청으로 메세지 발행
    @PostMapping("/api/message")
    public ShowMessageDto.ResponseDto showMessageList(@RequestBody RoomDto roomDto,
                                                      @AuthenticationPrincipal UserDetailsImpl userDetails,
                                                      @PageableDefault(size = 20, sort = "createAt", direction = Sort.Direction.DESC) Pageable pageable
    ){
        return messageService.showMessageList(roomDto, userDetails, pageable);
    }
}
