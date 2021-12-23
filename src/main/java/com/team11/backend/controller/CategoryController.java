package com.team11.backend.controller;

import com.team11.backend.dto.CategoryDto;
import com.team11.backend.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/api/category")
    public List<CategoryDto.ResponseDto> categoryList(@RequestBody CategoryDto.RequestDto categoryRequestDto , Pageable pageable) {
        return categoryService.getCategorys(categoryRequestDto,pageable);
    }
}