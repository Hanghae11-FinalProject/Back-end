package com.team11.backend.model;

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
}
