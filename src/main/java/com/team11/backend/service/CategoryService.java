package com.team11.backend.service;

import com.team11.backend.dto.CategoryDto;
import com.team11.backend.model.Post;
import com.team11.backend.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryService {

    private final PostRepository postRepository;

    @Transactional
    public List<CategoryDto.ResponseDto> getCategorys(CategoryDto.RequestDto categoryRequestDto) {

        List<Post> findCategory = postRepository.findAllByCategory(categoryRequestDto.getCategoryName());
            if(findCategory == null) {
                throw new NullPointerException("해당 카테고리가 존재하지 않습니다");
            }

        List<Post> findAddress = postRepository.findAllPostBySameAddress(categoryRequestDto.getRegion());
            if(findAddress == null) {
                throw new NullPointerException("해당 지역이 존재하지 않습니다");
            }

//        for(Post post : findAddress) {
//            System.out.println(post.getTitle());
//        }


        Sort.Direction direction = Sort.Direction.DESC;

        // region
        boolean isSeongbuk_gu = false;
        boolean isJung_gu = false;

        String strRegion = categoryRequestDto.getRegion();

        //지역별 별 검색 필터 조건
        if (strRegion.contains("성북구")){
            isSeongbuk_gu = true;
        }
        if (strRegion.contains("중구")){
            isJung_gu = true;
        }

        Post category1 = postRepository.findByCategory("식사재");
        Post category2 = postRepository.findByCategory("생활용품");
        Post category3 = postRepository.findByCategory("재능");
        Post category4 = postRepository.findByCategory("가구");
        Post category5 = postRepository.findByCategory("가전");
        Post category6 = postRepository.findByCategory("의류");
        Post category7 = postRepository.findByCategory("기타");

        Page<Post> categoryList = postRepository.findAllByCategoryAndAddress(category1, category2, category3, category4, category5, category6, category7,
                isSeongbuk_gu,isJung_gu);

        return (categoryList);
}
