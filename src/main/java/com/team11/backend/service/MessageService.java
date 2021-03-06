package com.team11.backend.service;
import com.team11.backend.dto.*;
import com.team11.backend.dto.chat.MessageDto;
import com.team11.backend.model.*;
import com.team11.backend.redis.RedisMessagePublisher;
import com.team11.backend.repository.*;
import com.team11.backend.security.UserDetailsImpl;
import com.team11.backend.timeConversion.MessageTimeConversion;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class MessageService {

    private final RedisMessagePublisher messagePublisher;
    private final MessageRepository messageRepository;
    private final RoomRepository roomRepository;
    private final UserRoomRepository userRoomRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public void sendMessage(MessageDto messageDto) {
        LocalDateTime now = LocalDateTime.now();
        MessageDto sendMessageDto = new MessageDto();
        boolean check = false;

        User sender = userRepository.findById(messageDto.getSenderId()).orElseThrow(
                () -> new IllegalArgumentException("해당되는 sender없음")
        );

        User receiver = userRepository.findById(messageDto.getReceiverId()).orElseThrow(
                () -> new IllegalArgumentException("해당되는 receiver없음")
        );

        //전달받은 메세지 타입체크
        if (Message.MessageType.Start.equals(messageDto.getType())) {
            sendMessageDto = MessageDto.builder()
                    .message(sender.getNickname() + "님이 입장")
                    .senderId(sender.getId())
                    .roomName(messageDto.getRoomName())
                    .receiverId(messageDto.getReceiverId())
                    .createdAt(MessageTimeConversion.timeConversion(now))
                    .type(messageDto.getType())
                    .build();
        } else if (Message.MessageType.Exit.equals(messageDto.getType())) {
            sendMessageDto = MessageDto.builder()
                    .message(sender.getNickname() + "님이 퇴장")
                    .senderId(sender.getId())
                    .roomName(messageDto.getRoomName())
                    .createdAt(MessageTimeConversion.timeConversion(now))
                    .type(messageDto.getType())
                    .receiverId(messageDto.getReceiverId())
                    .build();

            check = roomOut(sendMessageDto);

        } else if (Message.MessageType.Talk.equals(messageDto.getType())) {
            sendMessageDto = MessageDto.builder()
                    .message(messageDto.getMessage())
                    .senderId(sender.getId())
                    .roomName(messageDto.getRoomName())
                    .createdAt(MessageTimeConversion.timeConversion(now))
                    .type(messageDto.getType())
                    .receiverId(messageDto.getReceiverId())
                    .build();

        }

        //채팅방이 있는지 없는지 확인
        if (!check) {
            Room room = roomRepository.findByRoomName(sendMessageDto.getRoomName()).orElseThrow(
                    () -> new IllegalArgumentException("해당되는 룸 없습니다.")
            );

            List<UserRoom> userRoomList = userRoomRepository.findByRoom(room);

            Message message = new Message(sendMessageDto, userRepository, roomRepository);
            messageRepository.save(message);

            for (UserRoom userRoom : userRoomList) {
                userRoom.lastMessageIdChange(message.getId());
            }
            messagePublisher.publish(sendMessageDto);
        }
    }


    public boolean roomOut(MessageDto messages) {
        System.out.println(messages.getRoomName() + "룸네임");
        Room room = roomRepository.findByRoomName(messages.getRoomName()).orElseThrow(
                () -> new IllegalArgumentException("해당되는 룸없음")
        );

        List<UserRoom> userRoomList = userRoomRepository.findByRoom(room);
        // 자기가 보는 userRoom 삭제
        for (UserRoom userRoom : userRoomList) {
            if (userRoom.getUser().getId() == messages.getSenderId()) {
                userRoomRepository.deleteById(userRoom.getId());
            }
        }

        System.out.println("유저룸 사이즈" + userRoomList.size());
        if (userRoomList.size() == 1) { // 마지막으로 나가는 사람 -> room 삭제
            messageRepository.deleteAllByRoom(room);
            roomRepository.deleteById(room.getId());
            return true;
        }

        return false;
    }


    @Transactional
    public void updateRoomMessageCount(RoomDto.UpdateCountDto updateCountDto) {
        Room room = roomRepository.findByRoomName(updateCountDto.getRoomName()).orElseThrow(
                () -> new IllegalArgumentException("해당방 없음")
        );

        User user = userRepository.findById(updateCountDto.getUserId()).orElseThrow(
                () -> new IllegalArgumentException("해당방 유저 없음")
        );

        UserRoom userRoom = userRoomRepository.findByRoomAndUser(room, user);

        userRoom.countChange();
    }

    @Transactional
    public MessageListDto showMessageList(RoomDto.findRoomDto roomDto, Pageable pageable, UserDetailsImpl userDetails) {

        //채팅 페이지 설정
//        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
//        Sort sort = Sort.by(Sort.Direction.ASC, "createdAt");
//        pageable = PageRequest.of(page, 200, sort);

        Room room = userRoomCount(roomDto);

        //해당 RoomPostId로 게시물이 존재하는지 조회, 없으면 null
        Post post = postRepository.findById(room.getRoomPostId()).orElse(null);

        //특정 방에 해당하는 메세지 정보 가져오기
        PageImpl<Message> messages = messageRepository.findByRoom(room, pageable);

        //게시물 주인 유저 입장인지, 게시물에 접근한 유저 입장에서 보낸 메세지인지 판별
        List<MessageDto> messageDtos = DiscriminationWhoSentMessage(roomDto, userDetails, room, messages);

        //위에 post 값이 존재하는지,null 인지를 기준으로 myItem,exchangeItem 있는지 없는지 유무 확인.
        PostDto.ShowPostRoomDto showPostRoomDto = getShowPostRoomDto(post);


        return MessageListDto.builder()
                .message(messageDtos)
                .post(showPostRoomDto)
                .build();
    }

    private List<MessageDto> DiscriminationWhoSentMessage(RoomDto.findRoomDto roomDto, UserDetailsImpl userDetails, Room room, PageImpl<Message> messages) {
        List<MessageDto> messageDtos = new ArrayList<>();
        for (Message message : messages) {
            //게시물 주인이 보낸 메세지
            if (roomDto.getToUserId() == message.getUser().getId()) {
                MessageDto messageDto = MessageDto.builder()
                        .message(message.getContent())
                        .roomName(room.getRoomName())
                        .senderId(message.getUser().getId())
                        .receiverId(userDetails.getUser().getId())
                        .type(message.getMessageType())
                        .createdAt(MessageTimeConversion.timeConversion(message.getCreatedAt()))
                        .build();
                messageDtos.add(messageDto);
            } else {
                //게시물에 접근한 유저가 보낸 메세지
                MessageDto messageDto = MessageDto.builder()
                        .message(message.getContent())
                        .roomName(room.getRoomName())
                        .senderId(message.getUser().getId())
                        .receiverId(roomDto.getToUserId())
                        .type(message.getMessageType())
                        .createdAt(MessageTimeConversion.timeConversion(message.getCreatedAt()))
                        .build();
                messageDtos.add(messageDto);
            }
        }
        return messageDtos;
    }

    private Room userRoomCount(RoomDto.findRoomDto roomDto) {
        //메세지를 담고있을 방 찾기
        Room room = roomRepository.findByRoomNameAndRoomPostId(roomDto.getRoomName(), roomDto.getPostId()).orElseThrow(
                () -> new IllegalArgumentException("no roomName"));
        //게시물 주인 유저 정보 조회
        User user = userRepository.findById(roomDto.getUserId()).orElseThrow(
                () -> new IllegalArgumentException("해당 유저 없음")
        );
        UserRoom userRoom = userRoomRepository.findByRoomAndUser(room, user);

        userRoom.countInit();
        return room;
    }

    private PostDto.ShowPostRoomDto getShowPostRoomDto(Post post) {
        PostDto.ShowPostRoomDto showPostRoomDto;
        if (post == null) {
            showPostRoomDto = PostDto.ShowPostRoomDto.builder()
                    .myItem("")
                    .exchangeItem("")
                    .build();
        } else {
            showPostRoomDto = PostDto.ShowPostRoomDto.builder()
                    .myItem(post.getMyItem())
                    .exchangeItem(post.getExchangeItem())
                    .build();
        }
        return showPostRoomDto;
    }
}