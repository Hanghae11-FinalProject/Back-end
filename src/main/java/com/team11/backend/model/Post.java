package com.team11.backend.model;

import com.team11.backend.dto.PostDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Entity
@Getter
public class Post extends Timestamped{
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    private CurrentState currentState;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId",nullable = false)
    private User user;
    
    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<Image> images;

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<Tag> tags;

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<BookMark> bookMarks;

    @Column(nullable = false)
    private String category;

    public void updateBookMark(BookMark bookMark) {
        List<BookMark> bookMarkList = this.bookMarks;
        bookMarkList.add(bookMark);
    }

    public void updatePost(PostDto.RequestDto requestDto, List<Image> images, List<Tag> tags){
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.currentState = requestDto.getCurrentState();
        this.images = images;
        this.tags = tags;
        this.category = requestDto.getCategory();
    }

}
