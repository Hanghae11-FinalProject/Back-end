package com.team11.backend.service;

import com.team11.backend.dto.CategoryDto;
import com.team11.backend.dto.querydto.BookMarkQueryDto;
import com.team11.backend.dto.querydto.CategoryQueryDto;
import com.team11.backend.dto.querydto.ImageQueryDto;
import com.team11.backend.repository.querydsl.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

   // private final RedisTemplate<String,List<CategoryQueryDto>> redisTemplateList;..

    @Transactional
    public Page<CategoryQueryDto> categoryFilter(CategoryDto.RequestDto categoryRequestDto, Pageable pageable) {
//상세페이지 댓글 개수 불일치.
        Page<CategoryQueryDto> postList = categoryRepository.categoryFilter(categoryRequestDto, pageable);
        List<Long> postIdCollect = postList.stream().map(CategoryQueryDto::getPostId).collect(Collectors.toList());

        List<ImageQueryDto> imageList = categoryRepository.imageFilter(postIdCollect);
        Map<Long, List<ImageQueryDto>> imageIdMap = imageList.stream().collect(Collectors.groupingBy(ImageQueryDto::getPostId));

        List<BookMarkQueryDto> bookMarkInUserIdList = categoryRepository.bookMarkFilter(postIdCollect);
        Map<Long, List<BookMarkQueryDto>> bookMarkInUserIdMap = bookMarkInUserIdList.stream().collect(Collectors.groupingBy(BookMarkQueryDto::getPostId));
        postList.forEach(key -> key.setImages(Optional.ofNullable(imageIdMap.get(key.getPostId())).orElse(new ArrayList<>())));
        postList.forEach(key -> key.setBookMarks(Optional.ofNullable(bookMarkInUserIdMap.get(key.getPostId())).orElse(new ArrayList<>())));


       // redisTemplateList.opsForList().leftPush("category"+pageable.getPageNumber(), postList.stream().collect(Collectors.toList()));
        return postList;
    }
}
