package com.team11.backend.service;

import com.team11.backend.dto.CategoryDto;
import com.team11.backend.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CategoryService {

    private final PostRepository postRepository;

    @Transactional
    public List<CategoryDto.ResponseDto> getCategorys(CategoryDto.RequestDto categoryRequestDto) {




    }

}
