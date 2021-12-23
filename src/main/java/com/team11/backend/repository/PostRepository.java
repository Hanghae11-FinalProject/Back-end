package com.team11.backend.repository;

import com.team11.backend.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByCreateAtDesc();
    List<Post> findAllByUser
}
