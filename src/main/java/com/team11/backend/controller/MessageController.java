package com.team11.backend.controller;

import com.team11.backend.dto.MessageListDto;
import com.team11.backend.dto.RoomDto;
import com.team11.backend.dto.chat.MessageDto;
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
    // '/pub/message'
    @MessageMapping("/message") //클라이언트가 SEND할 수 있는 경로 stompConfig에서 설정한 applicationDestinationPrefixes와 @MessageMapping 경로가 병합됨
    public void message(MessageDto messageDto){
        messageService.sendMessage(messageDto);
    }

    @PostMapping("/api/roomcount")
    public void updateCount(@RequestBody RoomDto.UpdateCountDto updateCountDto){
        System.out.println("연결 게속 되고 있어요 ㅠㅠ");
        messageService.updateRoomMessageCount(updateCountDto);
    }
//    pub/api/message 클라이언트 요청으로 메세지 발행
    @PostMapping("/api/message")
    public MessageListDto showMessageList(@RequestBody RoomDto.findRoomDto roomDto,
                                          @PageableDefault(size = 200, sort = "createAt") Pageable pageable,
                                          @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        return messageService.showMessageList(roomDto, pageable,userDetails);
    }
}
