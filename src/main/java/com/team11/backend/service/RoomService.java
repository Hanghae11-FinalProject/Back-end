package com.team11.backend.service;

import com.team11.backend.dto.ChatRoomDto;
import com.team11.backend.dto.ChatUserDto;
import com.team11.backend.dto.LastMessageDto;
import com.team11.backend.dto.RoomDto;
import com.team11.backend.model.*;
import com.team11.backend.repository.*;
import com.team11.backend.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final UserRoomRepository userRoomRepository;
    private final MessageRepository messageRepository;

    @Transactional
    public RoomDto.Response createRoomService(RoomDto.Reqeust roomDto, UserDetailsImpl userDetails){

        Post post = postRepository.findById(roomDto.getPostId()).orElseThrow(
                ()-> new IllegalArgumentException("no post")
        );


        User user = userDetails.getUser();
        User toUser = userRepository.findById(roomDto.getToUserId()).orElseThrow(
                ()-> new IllegalArgumentException("no touser")
        );
        List<Room> checkRoomList = roomRepository.findByPost(post);
        for (Room room : checkRoomList){
            UserRoom checkUserRoom = userRoomRepository.findByRoomAndUser(room,toUser);
            if(checkUserRoom != null){
                throw new IllegalArgumentException("same room");
            }
        }


        String roomName = UUID.randomUUID().toString();

        Room room = Room.builder()
                .roomName(roomName)
                .post(post)
                .build();

        roomRepository.save(room);

        // userRoom two create

        UserRoom userRoom = UserRoom.builder()
                .room(room)
                .user(user)
                .lastMessgeId(null)
                .count(0)
                .build();

        userRoomRepository.save(userRoom);

        UserRoom toUserRoom = UserRoom.builder()
                .room(room)
                .user(toUser)
                .lastMessgeId(null)
                .count(0)
                .build();
        userRoomRepository.save(toUserRoom);

        ChatUserDto chatUserDto = ChatUserDto.builder()
                .userId(toUser.getId())
                .profileImg(toUser.getProfileImg())
                .nickname(toUser.getNickname())
                .build();

        RoomDto.Response response = RoomDto.Response.builder()
                .roomName(room.getRoomName())
                .user(chatUserDto)
                .build();

        return response;
    }

    public List<ChatRoomDto> showRoomListService(UserDetailsImpl userDetails) {
        List<UserRoom> userRooms = userRoomRepository.findByUser(userDetails.getUser());
        List<ChatRoomDto> chatRoomDtos = new ArrayList<>();

        for (UserRoom userRoom : userRooms){
            List<UserRoom> findByRoomUserRooms = userRoomRepository.findByRoom(userRoom.getRoom());
            for(UserRoom findByRoomUserRoom : findByRoomUserRooms){
                if(!Objects.equals(findByRoomUserRoom.getUser().getId(), userDetails.getUser().getId())){
                    ChatUserDto chatUserDto = ChatUserDto.builder()
                            .userId(findByRoomUserRoom.getUser().getId())
                            .profileImg(findByRoomUserRoom.getUser().getProfileImg())
                            .nickname(findByRoomUserRoom.getUser().getNickname())
                            .build();

                    LastMessageDto lastMessageDto;

                    if(findByRoomUserRoom.getLastMessgeId() == null){
                         lastMessageDto = LastMessageDto.builder()
                                .content("room create")
                                .createdAt("asdasd")
                                .build();
                    }else{
                        Message message = messageRepository.getById(findByRoomUserRoom.getLastMessgeId());
                         lastMessageDto = LastMessageDto.builder()
                                .content(message.getContent())
                                .createdAt("asdasd")
                                .build();
                    }

                    ChatRoomDto chatRoomDto = ChatRoomDto.builder()
                            .roomName(findByRoomUserRoom.getRoom().getRoomName())
                            .postId(findByRoomUserRoom.getRoom().getPost().getId())
                            .user(chatUserDto)
                            .lastMessage(lastMessageDto)
                            .notReadingMessageCount(findByRoomUserRoom.getCount())
                            .build();
                    chatRoomDtos.add(chatRoomDto);

                }
            }
        }

        return chatRoomDtos;

    }
}
