package com.team11.backend.repository;

import com.team11.backend.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByCreateAtDesc();

    List<Post> findAllByCategory(String category);


    @Query(value = "select p from Post p where p.user.address IN (:address) ")
    List<Post> findAllPostBySameAddress(@Param("address") String address);


    Post<post> findByCategory(String category);

    Page<Post> findAllByCategoryAndAddress(@Param("category1") Post category1, @Param("category2")Post category2,
                                           @Param("category3")Post category3, @Param("category4")Post category4,
                                           @Param("category5")Post category5, @Param("category6")Post category6,
                                           @Param("category7")Post category7,
                                           boolean isSeongbuk_gu, boolean isJung_gu);
}
