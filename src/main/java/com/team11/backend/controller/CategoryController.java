package com.team11.backend.controller;

import com.team11.backend.dto.CategoryDto;
import com.team11.backend.service.CategoryService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
public class CategoryController {

    private final CategoryService categoryService;
    //주소 필터링, 카테고리 필터링 기능을 포함한 메인페이지 조회
    @PostMapping("/api/category")
    public Result<?> categoryList(@RequestBody CategoryDto.RequestDto categoryRequestDto, @PageableDefault(size = 6) Pageable pageable) {
        return new Result<>(categoryService.categoryFilter(categoryRequestDto, pageable));
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    static class Result<T> {
        T data;
    }
}