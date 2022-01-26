package com.team11.backend.service;


import com.team11.backend.dto.SearchDto;
import com.team11.backend.dto.SearchRankResponseDto;
import com.team11.backend.dto.querydto.BookMarkQueryDto;
import com.team11.backend.dto.querydto.ImageQueryDto;
import com.team11.backend.dto.querydto.SearchQueryDto;
import com.team11.backend.repository.querydsl.CategoryRepository;
import com.team11.backend.repository.querydsl.SearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SearchService {

    private final SearchRepository searchRepository;
    private final CategoryRepository categoryRepository;
    private final RedisTemplate<String, String> redisTemplate;


    @Transactional
    public Page<SearchQueryDto> keywordSearch(SearchDto.RequestDto searchRequestDto, Pageable pageable) {

        Page<SearchQueryDto> postList = searchRepository.keywordFilter(searchRequestDto, pageable);
        List<Long> collectPostId = postList.stream().map(SearchQueryDto::getPostId).collect(Collectors.toList());

        List<ImageQueryDto> imageList = categoryRepository.imageFilter(collectPostId);
        Map<Long, List<ImageQueryDto>> imageIdMap = imageList.stream().collect(Collectors.groupingBy(ImageQueryDto::getPostId));

        List<BookMarkQueryDto> bookMarkInUserIdList = categoryRepository.bookMarkFilter(collectPostId);
        Map<Long, List<BookMarkQueryDto>> bookMarkInUserIdMap = bookMarkInUserIdList.stream().collect(Collectors.groupingBy(BookMarkQueryDto::getPostId));

        postList.forEach(key -> key.setImages(Optional.ofNullable(imageIdMap.get(key.getPostId())).orElse(new ArrayList<>())));
        postList.forEach(key -> key.setBookMarks(Optional.ofNullable(bookMarkInUserIdMap.get(key.getPostId())).orElse(new ArrayList<>())));


        Double score = 0.0;
        try {
            redisTemplate.opsForZSet().incrementScore("ranking", searchRequestDto.getKeyword().get(0),1);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        redisTemplate.opsForZSet().incrementScore("ranking", searchRequestDto.getKeyword().get(0), score);


        return postList;

    }

    public List<SearchRankResponseDto> SearchRankList() {
        String key = "ranking";
        ZSetOperations<String, String> ZSetOperations = redisTemplate.opsForZSet();
        Set<ZSetOperations.TypedTuple<String>> typedTuples = ZSetOperations.reverseRangeWithScores(key, 0, 9);  //score순으로 10개 보여줌
        return typedTuples.stream().map(SearchRankResponseDto::convertToResponseRankingDto).collect(Collectors.toList());
    }
}
