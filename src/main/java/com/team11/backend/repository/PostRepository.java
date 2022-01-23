package com.team11.backend.repository;

import com.team11.backend.dto.MyPostResponseDto;
import com.team11.backend.model.Post;
import com.team11.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query(value = "select new com.team11.backend.dto.MyPostResponseDto" +
            " (p.id, p.title, p.content, p.createdAt, u.address, p.currentState, p.bookMarkCnt, p.commentCnt )" +
            " from Post p " +
            " left join p.user u " +
            " where p.user = :user")
    List<MyPostResponseDto> findAllByUserOrderByCreatedAtDesc(@Param("user") User user);



}

