package com.team11.backend.service;

import com.team11.backend.dto.SearchDto;
import com.team11.backend.dto.SearchRankResponseDto;
import com.team11.backend.model.Post;
import com.team11.backend.repository.BookMarkRepository;
import com.team11.backend.repository.CommentRepository;
import com.team11.backend.repository.querydsl.SearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import static com.team11.backend.dto.SearchDto.searchResponseDto;

@RequiredArgsConstructor
@Service
public class SearchService {

    private final SearchRepository searchRepository;
    private final CommentRepository commentRepository;
    private final BookMarkRepository bookMarkRepository;
    private final RedisTemplate<String, String> redisTemplate;


    @Transactional
    public SearchDto.TotalResponseDto keywordSearch(SearchDto.RequestDto searchRequestDto, Pageable pageable) {

        PageImpl<Post> posts = searchRepository.keywordFilter(searchRequestDto, pageable);
        Double score = 0.0;
        try {
            redisTemplate.opsForZSet().incrementScore("ranking", searchRequestDto.getKeyword().get(0),1);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        redisTemplate.opsForZSet().incrementScore("ranking", searchRequestDto.getKeyword().get(0), score);


        List<SearchDto.ResponseDto> responseDtoList = searchResponseDto(posts,bookMarkRepository,commentRepository);
        Long postCnt = posts.getTotalElements();

        return SearchDto.TotalResponseDto.builder()
                .postCnt(postCnt)
                .posts(responseDtoList)
                .build();
    }



//    public SearchRankResponseDto SearchRankList() {
//        List<String> strings = searchRepositoryInterface.findKeywordRank();
//        return new SearchRankResponseDto(strings.subList(0, Math.min(10, strings.size())));
//    }

    public List<SearchRankResponseDto> SearchRankList() {
        String key = "ranking";
        ZSetOperations<String, String> ZSetOperations = redisTemplate.opsForZSet();
        Set<ZSetOperations.TypedTuple<String>> typedTuples = ZSetOperations.reverseRangeWithScores(key, 0, 9);  //score순으로 10개 보여줌
        return typedTuples.stream().map(SearchRankResponseDto::convertToResponseRankingDto).collect(Collectors.toList());
    }
}
