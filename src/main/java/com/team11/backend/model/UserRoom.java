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
public class UserRoom extends Timestamped{
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId",nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "toUserId",nullable = false)
    private User toUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roomId",nullable = false)
    private Room room;


    @Column(nullable = true)
    private Long lastMessageId;


    @Column(nullable = false)
    private int count;

    public void lastMessageIdChange(Long Id){
        this.lastMessageId = Id;
    }

    public void countChange(){
        this.count = this.count+1;
    }

    public void countInit(){
        this.count = 0;
    }

}
