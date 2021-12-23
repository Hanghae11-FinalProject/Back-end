package com.team11.backend.service;


import com.team11.backend.dto.CategoryDto;
import com.team11.backend.model.Post;
import com.team11.backend.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CategoryService {

    private final PostRepository postRepository;

    @Transactional
    public List<CategoryDto.ResponseDto> getCategorys(CategoryDto.RequestDto categoryRequestDto , Pageable pageable) {

        boolean isFood = false;
        boolean idCloth = false;
        boolean isHomeappliances = false;

        String categoryFilter = categoryRequestDto.getCategoryName();
        //String regionSi = categoryRequestDto.getRegionSi();
        String regionGu = categoryRequestDto.getRegionGu();

        if (categoryFilter.contains("food"))
            isFood = true;
        if (categoryFilter.contains("cloth"))
            idCloth = true;

        if (categoryFilter.contains("homeappliances"))
            isHomeappliances = true;

        Page<Post> allByCategoryFilterLookup = postRepository.findAllByCategoryFilterLookup(isFood, idCloth, isHomeappliances/*,regionSi*/,regionGu,pageable);


        return allByCategoryFilterLookup.stream().map(s -> {
            return CategoryDto.ResponseDto.builder()
                    .postId(s.getId())
                    .categoryName(s.getCategory())
                    .username(s.getUser().getUsername())
                    .title(s.getTitle())
                    .content(s.getContent())
                    .address(s.getUser().getAddress())
                    .images(s.getImages())
                    .currentState(s.getCurrentState())
                    .createdAt(s.getCreateAt().toString())
                    .build();
        }).collect(Collectors.toList());


    }
}
