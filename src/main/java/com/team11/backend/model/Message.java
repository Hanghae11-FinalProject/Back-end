package com.team11.backend.model;

import com.team11.backend.dto.chat.MessageDto;
import com.team11.backend.repository.RoomRepository;
import com.team11.backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class Message extends Timestamped{

    public enum MessageType{
        Talk,Exit,Start
    }

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userId",nullable = false)
    private User user;

    @Column(nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    private MessageType messageType;

    @ManyToOne
    @JoinColumn(name = "roomId")
    private Room room;

    @Builder
    public Message(Long id, User user, String content, MessageType messageType) {
        this.id = id;
        this.user = user;
        this.content = content;
        this.messageType = messageType;
    }

    @Builder
    public Message(MessageDto messageDto , UserRepository userRepository , RoomRepository roomRepository) {
        this.messageType = messageDto.getType();
        this.user = userRepository.findByNickname(messageDto.getSender()).orElseThrow(() -> new IllegalArgumentException("유저정보가 존재하지 않습니다."));
        this.content = messageDto.getMessage();
        this.room = roomRepository.findByRoomName(messageDto.getRoomName()).orElseThrow(() -> new IllegalArgumentException("방 정보가 존재하지 않습니다."));
    }
}
