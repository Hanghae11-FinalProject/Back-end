package com.team11.backend.repository;


import com.team11.backend.model.BookMark;
import com.team11.backend.model.Post;
import com.team11.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookMarkRepository extends JpaRepository<BookMark,Long> {
    boolean existsByUserAndPost(User user, Post post);
    Optional<BookMark> findByUserAndPost(User user, Post post);
    List<BookMark> findByUserUsername(String username);

    Optional<Integer> countByPost(Post post);
}
