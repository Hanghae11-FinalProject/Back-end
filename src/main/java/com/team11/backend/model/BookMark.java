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
public class BookMark {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "postId", nullable = false)
    private Post post;

    public void setPostBookMark(Post post) {
        if (this.post != null)
            this.post.getBookMarks().remove(this);
        this.post = post;
        // infinite loof prevent
        if (!post.getBookMarks().contains(this))
            post.getBookMarks().add(this);
    }

}
