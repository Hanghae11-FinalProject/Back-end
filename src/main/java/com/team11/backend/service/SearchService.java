package com.team11.backend.service;

import com.team11.backend.dto.SearchDto;
import com.team11.backend.model.Post;

import com.team11.backend.repository.querydsl.SearchRepository;
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

    @Transactional
    public List<SearchDto.ResponseDto> keywordSearch(SearchDto.RequestDto searchRequestDto, Pageable pageable) {

        PageImpl<Post> posts = searchRepository.keywordFilter(searchRequestDto, pageable);

        return posts.stream().map(s -> new SearchDto.ResponseDto(s.getId(), s.getUser().getNickname(), s.getTitle(), s.getContent(), s.getUser().getAddress(), s.getImages(), s.getCurrentState(), s.getCreateAt().toString()))
                .collect(Collectors.toList());
    }
}