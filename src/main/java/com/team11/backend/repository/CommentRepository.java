package com.team11.backend.repository;

import com.team11.backend.model.Comment;
import com.team11.backend.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;


public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findCommentByPost(Post post);

    Optional<Integer> countByPost(Post post);
}
