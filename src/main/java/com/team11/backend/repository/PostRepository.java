package com.team11.backend.repository;

import com.team11.backend.model.Post;
import com.team11.backend.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    PageImpl<Post> findAllByOrderByCreateAtDesc(Pageable pageable);
    List<Post> findAllByUserOrderByCreateAtDesc(User user);
    @Query(value = "select p from Post p where " +
            "((:isFood = true) and (p.category like 'food')) or " +
            "((:isCloth = true) and (p.category like 'cloth')) or " +
            "((:isHomeappliances = true) and (p.category like 'homeappliances')) or " +
            "(p.user.address = :regionGu)"

    )
    Page<Post> findAllByCategoryFilterLookup(@Param("isFood") boolean isFood,
                                             @Param("isCloth") boolean idCloth,
                                             @Param("isHomeappliances") boolean isHomeappliances,
                                             //@Param("regionSi") String regionSi,
                                             @Param("regionGu") String regionGu,
                                             Pageable pageable);

}

