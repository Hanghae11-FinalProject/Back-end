package com.team11.backend.controller;

import com.team11.backend.dto.SearchDto;
import com.team11.backend.dto.SearchRankResponseDto;
import com.team11.backend.service.SearchService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class SearchController {

    private final SearchService searchService;

    @PostMapping("/api/search")
    public SearchController.Result<?> searchList(@RequestBody SearchDto.RequestDto searchRequestDto,
                                                 @PageableDefault(size = 6, sort = "createdAt",
                                                         direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return new SearchController.Result<>(searchService.keywordSearch(searchRequestDto, pageable));
    }

    @GetMapping("/api/search/rank")
    public List<SearchRankResponseDto> searchRankList(){
        return searchService.SearchRankList();
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    static class Result<T> {
        T data;
    }
}
