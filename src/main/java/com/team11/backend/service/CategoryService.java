package com.team11.backend.service;


import com.team11.backend.dto.BookMarkDto;
import com.team11.backend.dto.CategoryDto;
import com.team11.backend.model.BookMark;
import com.team11.backend.model.Post;
import com.team11.backend.repository.BookMarkRepository;
import com.team11.backend.repository.CommentRepository;
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
    private final CommentRepository commentRepository;
    private final BookMarkRepository bookMarkRepository;

    @Transactional
    public List<CategoryDto.ResponseDto> categoryFilter(CategoryDto.RequestDto categoryRequestDto, Pageable pageable) {

//상세페이지 댓글 개수 불일치.
        PageImpl<Post> posts = categoryRepository.categoryFilter(categoryRequestDto, pageable);
        return posts.stream()
                .map(s -> new CategoryDto.ResponseDto(
                        s.getCategory(),
                        s.getId(),
                        s.getUser().getProfileImg(),
                        s.getUser().getUsername(),
                        s.getUser().getNickname(),
                        s.getTitle(), s.getContent(),
                        s.getUser().getAddress(),
                        s.getImages(),
                        s.getBookMarks().stream()
                                .map(this::toBookmarkResponseDto)
                                .collect(Collectors.toList()),
                        s.getCurrentState(),
                        s.getMyItem(),
                        s.getExchangeItem(),
                        TimeConversion.timeConversion(s.getCreateAt()),
                        bookMarkRepository.countByPost(s).orElse(0),
                        commentRepository.countByPost(s).orElse(0)))
                .collect(Collectors.toList());

    }

    private BookMarkDto.DetailResponseDto toBookmarkResponseDto(BookMark bookMark) {

        return BookMarkDto.DetailResponseDto.builder()
                .userId(bookMark.getUser().getId())
                .build();
    }
}
