package com.team11.backend.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Entity
@Getter
public class Image {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private String imageName;

    @Column(nullable = false)
    private  String imageUrl;

    @ManyToOne
    @JoinColumn(name = "postId",nullable = false)
    private Post post;
}
