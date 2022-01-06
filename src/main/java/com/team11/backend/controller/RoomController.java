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
import com.team11.backend.redis.RedisMessageSubscriber;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@RequiredArgsConstructor
public class RoomController {
    private final RedisMessageSubscriber redisMessageSubscriber;
    private final RedisMessageListenerContainer redisMessageListenerContainer;
    private final Map<String, ChannelTopic> topics;
    private final RoomService roomService;

    //chat room create
    @PostMapping("/api/room")
    public RoomDto.Response roomCreate(@RequestBody RoomDto.Reqeust roomDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return roomService.createRoomService(roomDto,userDetails);
    }

    @GetMapping("/api/room")
    public List<ChatRoomDto> showRoomList(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return roomService.showRoomListService(userDetails);
    }


    @MessageMapping("/join")
    public void join(String roomId){
        String roomId2 = roomId.replaceAll("\"","");
        ChannelTopic topic = topics.get(roomId2);
        if(topic == null){
            topic = new ChannelTopic(roomId2);
            redisMessageListenerContainer.addMessageListener(redisMessageSubscriber,topic);
        }
    }

}
