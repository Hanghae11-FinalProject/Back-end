package com.team11.backend.service;


import com.team11.backend.dto.CategoryDto;
import com.team11.backend.model.Post;
import com.team11.backend.repository.querydsl.CategoryRepository;
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
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public List<CategoryDto.ResponseDto> categoryFilter(CategoryDto.RequestDto categoryRequestDto, Pageable pageable) {

        PageImpl<Post> posts = categoryRepository.categoryFilter(categoryRequestDto, pageable);
        return posts.stream().map(s -> new CategoryDto.ResponseDto(s.getCategory(), s.getId(), s.getUser().getUsername(), s.getUser().getNickname(), s.getTitle(), s.getContent(), s.getUser().getAddress(), s.getImages(), s.getCurrentState(),s.getMyItem(),s.getExchangeItem() , TimeConversion.timeConversion(s.getCreateAt())))
                .collect(Collectors.toList());

    }
}
