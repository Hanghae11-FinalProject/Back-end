package com.team11.backend.service;

import com.team11.backend.dto.CategoryDto;
import com.team11.backend.dto.querydto.BookMarkQueryDto;
import com.team11.backend.dto.querydto.CategoryQueryDto;
import com.team11.backend.dto.querydto.ImageQueryDto;
import com.team11.backend.repository.querydsl.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
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

    
    //메인페이지 조회 (카테고리 선택, 주소정보 선택으로) 필터링
    @Transactional
    public List<CategoryQueryDto> categoryFilter(CategoryDto.RequestDto categoryRequestDto, Pageable pageable) {
        /* 엔티티 연관관계에서 post가 One 이미지, 북마크는 Many 라고 했을 때 
        * post 중심으로 조인을 하게되면 N+1, 데이터 중복이 발생하게됨 해결방법으로 fetch join을 사용하면 되지만 
        * ToMany 관계를 조인하게되면 데이터 중복이생겨 페이징이 불가능하기 때문에 먼저
        * ManyToOne 관계를 다 조인 한 후 LazyLaoding을 이용하여 뒤늦게 toMany 관계들을 동작시키도록함
        * */
        List<CategoryQueryDto> postList = categoryRepository.categoryFilter(categoryRequestDto, pageable).toList();
        //queryDsl 메소드 재활용
        List<Long> postIdCollect = postList.stream().map(CategoryQueryDto::getPostId).collect(Collectors.toList());

        List<ImageQueryDto> imageList = categoryRepository.imageFilter(postIdCollect);
        Map<Long, List<ImageQueryDto>> imageIdMap = imageList.stream().collect(Collectors.groupingBy(ImageQueryDto::getPostId));

        List<BookMarkQueryDto> bookMarkInUserIdList = categoryRepository.bookMarkFilter(postIdCollect);
        Map<Long, List<BookMarkQueryDto>> bookMarkInUserIdMap = bookMarkInUserIdList.stream().collect(Collectors.groupingBy(BookMarkQueryDto::getPostId));
        postList.forEach(key -> key.setImages(Optional.ofNullable(imageIdMap.get(key.getPostId())).orElse(new ArrayList<>())));
        postList.forEach(key -> key.setBookMarks(Optional.ofNullable(bookMarkInUserIdMap.get(key.getPostId())).orElse(new ArrayList<>())));


        return postList;
    }
}
