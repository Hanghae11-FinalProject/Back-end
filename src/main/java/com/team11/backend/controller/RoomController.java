package com.team11.backend.controller;


import com.team11.backend.dto.ChatRoomDto;
import com.team11.backend.dto.RoomDto;
import com.team11.backend.dto.ShowMessageDto;
import com.team11.backend.security.UserDetailsImpl;
import com.team11.backend.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;
    //chat room create
    @PostMapping("/api/room")
    public void roomCreate(@RequestBody RoomDto roomDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        roomService.createRoomService(roomDto,userDetails);
    }

    @GetMapping("/api/room")
    public List<ChatRoomDto> showRoomList(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return roomService.showRoomListService(userDetails);
    }

//    @PostMapping("/api/message")
//    public List<ShowMessageDto.ResponseDto> showMessageList(@RequestBody ShowMessageDto.RequestDto requestDto){
//        return
//    }
}
