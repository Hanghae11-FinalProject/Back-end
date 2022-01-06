package com.team11.backend.service;
import com.team11.backend.dto.*;
import com.team11.backend.dto.chat.MessageDto;
import com.team11.backend.model.Message;
import com.team11.backend.model.Post;
import com.team11.backend.model.Room;
import com.team11.backend.model.UserRoom;
import com.team11.backend.redis.RedisMessagePublisher;
import com.team11.backend.repository.MessageRepository;
import com.team11.backend.repository.PostRepository;
import com.team11.backend.repository.RoomRepository;
import com.team11.backend.repository.UserRoomRepository;
import com.team11.backend.security.UserDetailsImpl;
import com.team11.backend.timeConversion.MessageTimeConversion;
import com.team11.backend.timeConversion.TimeConversion;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Transactional
@Service
public class MessageService {

    private final RedisMessagePublisher messagePublisher;
    private final MessageRepository messageRepository;
    private final RoomRepository roomRepository;
    private final UserRoomRepository userRoomRepository;


    public void sendMessage(Message message,Long receiverId) {
        LocalDateTime now = LocalDateTime.now();
        if (Message.MessageType.Start.equals(message.getMessageType())) {
            MessageDto messages = MessageDto.builder()
                    .message(message.getUser().getUsername() + "님이 입장")
                    .senderId(message.getUser().getId())
                    .roomName(message.getRoom().getRoomName())
                    .receiverId(receiverId)
                    .createdAt(MessageTimeConversion.timeConversion(now))
                    .type(message.getMessageType())
                    .build();
            messagePublisher.publish(messages);


            messagePublisher.publish(messages);
        } else if (Message.MessageType.Exit.equals(message.getMessageType())) {
            MessageDto exitMessage = MessageDto.builder()
                    .message(message.getUser().getUsername() + "님이 퇴장")
                    .senderId(message.getUser().getId())
                    .roomName(message.getRoom().getRoomName())
                    .createdAt(MessageTimeConversion.timeConversion(now))
                    .type(message.getMessageType())
                    .receiverId(receiverId)
                    .build();
            messagePublisher.publish(exitMessage);

        } else if (Message.MessageType.Talk.equals(message.getMessageType())) {
            MessageDto talkMessage = MessageDto.builder()
                    .message(message.getContent())
                    .senderId(message.getUser().getId())
                    .roomName(message.getRoom().getRoomName())
                    .createdAt(MessageTimeConversion.timeConversion(now))
                    .type(message.getMessageType())
                    .receiverId(receiverId)
                    .build();

            Room room = roomRepository.findByRoomName(talkMessage.getRoomName()).orElseThrow(
                    ()-> new IllegalArgumentException("해당되는 룸 없습니다.")
            );

            List<UserRoom> userRoomList = userRoomRepository.findByRoom(room);

            messageRepository.save(message);

            for (UserRoom userRoom : userRoomList){
                if(userRoom.getUser().getId() == message.getUser().getId()){
                    userRoom.countChange();
                }
                userRoom.lastMessageIdChange(message.getId());
            }
            messagePublisher.publish(talkMessage);
        }
    }

    public MessageListDto showMessageList(RoomDto.findRoomDto roomDto, Pageable pageable,UserDetailsImpl userDetails) {
        Room room = roomRepository.findByRoomNameAndPost_Id(roomDto.getRoomName(), roomDto.getPostId()).orElseThrow(
                ()-> new IllegalArgumentException("no roomName"));

        PageImpl<Message> messages = messageRepository.findByRoom(room, pageable);
        List<MessageDto> messageDtos = new ArrayList<>();

        PostDto.ShowPostRoomDto showPostRoomDto = PostDto.ShowPostRoomDto.builder()
                .myItem(room.getPost().getMyItem())
                .exchangeItem(room.getPost().getExchangeItem())
                .build();

        for (Message message : messages) {
            if(roomDto.getToUserId() == message.getUser().getId()){
                MessageDto messageDto = MessageDto.builder()
                        .message(message.getContent())
                        .roomName(room.getRoomName())
                        .senderId(message.getUser().getId())
                        .receiverId(userDetails.getUser().getId())
                        .type(message.getMessageType())
                        .createdAt(MessageTimeConversion.timeConversion(message.getCreateAt()))
                        .build();
                messageDtos.add(messageDto);
            }else{
                MessageDto messageDto = MessageDto.builder()
                        .message(message.getContent())
                        .roomName(room.getRoomName())
                        .senderId(message.getUser().getId())
                        .receiverId(roomDto.getToUserId())
                        .type(message.getMessageType())
                        .createdAt(MessageTimeConversion.timeConversion(message.getCreateAt()))
                        .build();
                messageDtos.add(messageDto);
            }

//            ChatUserDto chatUserDto = ChatUserDto.builder()
//                    .userId(message.getUser().getId())
//                    .profileImg(message.getUser().getProfileImg())
//                    .nickname(message.getUser().getNickname())
//                    .build();

//            MessageContentDto messageContentDto = MessageContentDto.builder()
//                    .content(message.getContent())
//                    .createdAt(TimeConversion.timeConversion(message.getCreateAt()))
//                    .build();

//            MessageListDto messageListDto = MessageListDto.builder()
//                    .user(chatUserDto)
//                    .message(messageContentDto)
//                    .build();


        }


        MessageListDto messageListDto = MessageListDto.builder()
                .message(messageDtos)
                .post(showPostRoomDto)
                .build();

        return messageListDto;
    }
}