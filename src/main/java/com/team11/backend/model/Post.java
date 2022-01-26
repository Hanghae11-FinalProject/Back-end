package com.team11.backend.model;

import com.team11.backend.dto.PostDto;
import lombok.*;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
public class Post extends Timestamped{

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String myItem;

    @Column(nullable = false)
    private String exchangeItem;

    @Enumerated(EnumType.STRING)
    private CurrentState currentState;

    @Column(nullable = false)
    private String category;

    private Integer bookMarkCnt = 0;

    private Integer commentCnt = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId",nullable = false)
    private User user;

    @OneToMany(mappedBy = "post" , fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<Image> images;

    @OneToMany(mappedBy = "post" , fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<Tag> tags;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<BookMark> bookMarks = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private final List<Comment> comments = new ArrayList<>();

    public void updatebookMark(List<BookMark> bookMarks){
        this.bookMarks = bookMarks;
    }

    @Builder
    public Post(Long id,String title, String content, String myItem, String exchangeItem, CurrentState currentState, User user, List<Image> images, List<Tag> tags, String category) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.myItem = myItem;
        this.exchangeItem = exchangeItem;
        this.currentState = currentState;
        this.user = user;
        this.images = images;
        this.tags = tags;
        this.category = category;
        for (Image image : images) {
            image.setPost(this);
        }
        for (Tag tag : tags){
            tag.setPost(this);
        }
    }

    public void updatePost(PostDto.PutRequestDto requestDto, List<Image> images, List<Tag> tags){
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.currentState = requestDto.getCurrentState();
        this.images = images;
        this.tags = tags;
        this.myItem = requestDto.getMyItem();
        this.exchangeItem = requestDto.getExchangeItem();
        this.category = requestDto.getCategory();
    }

    public void updateCurrentState(String currentState){
        this.currentState = CurrentState.valueOf(currentState);
    }

    public void addBookMarkCount(){
        bookMarkCnt = bookMarkCnt + 1;
    }

    public void minusBookMarkCount(){
        bookMarkCnt = bookMarkCnt - 1;
    }

    public void addCommentCount(){
        commentCnt = commentCnt + 1;
    }

    public void minusCommentCount(){
        commentCnt = commentCnt - 1;
    }
}
