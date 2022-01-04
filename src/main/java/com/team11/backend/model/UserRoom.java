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
public class UserRoom {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userId",nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "roomId",nullable = false)
    private Room room;


    @ManyToOne
    @JoinColumn(name = "roomId",nullable = false)
    Message lastMessge;

    @Column(nullable = false)
    private int count;

}
