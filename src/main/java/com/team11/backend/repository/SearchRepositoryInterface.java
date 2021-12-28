package com.team11.backend.repository;

import com.team11.backend.model.Search;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;


public interface SearchRepositoryInterface extends JpaRepository<Search,Long> {
    @Query(value = "select s.keyword from Search s GROUP BY s.keyword order by count(s.id) desc")
    List<String> findKeywordRank();


}
