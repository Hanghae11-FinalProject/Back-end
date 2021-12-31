package com.team11.backend.service;

import com.team11.backend.dto.PostDto;
import com.team11.backend.dto.SearchDto;
import com.team11.backend.dto.SearchRankResponseDto;
import com.team11.backend.model.Post;

import com.team11.backend.model.Search;
import com.team11.backend.repository.BookMarkRepository;
import com.team11.backend.repository.CommentRepository;
import com.team11.backend.repository.PostRepository;
import com.team11.backend.repository.SearchRepositoryInterface;
import com.team11.backend.repository.querydsl.SearchRepository;
import com.team11.backend.timeConversion.TimeConversion;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SearchService {

    private final SearchRepository searchRepository;
    private final SearchRepositoryInterface searchRepositoryInterface;
    private final CommentRepository commentRepository;
    private final BookMarkRepository bookMarkRepository;

    @Transactional
    public SearchDto.TotalResponseDto keywordSearch(SearchDto.RequestDto searchRequestDto, Pageable pageable) {

        PageImpl<Post> posts = searchRepository.keywordFilter(searchRequestDto, pageable);

        searchRepositoryInterface.save(Search.builder()
                .keyword(searchRequestDto.getKeyword().get(0))
                .build());


        List<SearchDto.ResponseDto> responseDtoList = posts.stream()
                .map(s -> new SearchDto.ResponseDto(s.getId(), s.getUser().getNickname(), s.getTitle(), s.getContent(), s.getUser().getAddress(), s.getMyItem(), s.getExchangeItem(), s.getImages(), s.getCurrentState(), TimeConversion.timeConversion(s.getCreateAt()), bookMarkRepository.countByPost(s).orElse(0), commentRepository.countByPost(s).orElse(0)))
                .collect(Collectors.toList());
        Long postCnt = (long) responseDtoList.size();

        return SearchDto.TotalResponseDto.builder()
                .postCnt(postCnt)
                .posts(responseDtoList)
                .build();
    }

    public SearchRankResponseDto SearchRankList() {
        return new SearchRankResponseDto(searchRepositoryInterface.findKeywordRank().subList(0, 10));
    }
}
