package com.team11.backend.repository;


import com.team11.backend.model.Post;
import com.team11.backend.model.User;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    PageImpl<Post> findAllByOrderByCreateAtDesc(Pageable pageable);
    List<Post> findAllByUser(User user);
}

