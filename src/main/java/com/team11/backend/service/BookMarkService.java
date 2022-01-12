package com.team11.backend.service;

import com.team11.backend.dto.BookMarkDto;
import com.team11.backend.dto.CategoryDto;
import com.team11.backend.model.BookMark;
import com.team11.backend.model.Post;
import com.team11.backend.model.User;
import com.team11.backend.repository.BookMarkRepository;
import com.team11.backend.repository.CommentRepository;
import com.team11.backend.repository.PostRepository;
import com.team11.backend.timeConversion.TimeConversion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookMarkService {

    private final BookMarkRepository bookMarkRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public CategoryDto.ResponseDto addBookMark(User user, Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NullPointerException("해당 게시글이 존재하지 않습니다."));
        impossibleMyPostBookMark(user,post);
        if (!isNotAlreadyBookMark(user,post)) {
            BookMark bookMark = BookMark.builder()
                    .user(user)
                    .post(post)
                    .build();
            bookMarkRepository.save(bookMark);

            CategoryDto.ResponseDto responseDto =  CategoryDto.ResponseDto.builder()
                    .bookMarks(post.getBookMarks().stream()
                            .map(this::toBookmarkResponseDto)
                            .collect(Collectors.toList()))
                    .categoryName(post.getCategory())
                    .postId(post.getId())
                    .profileImg(post.getUser().getProfileImg())
                    .username(post.getUser().getUsername())
                    .nickname(post.getUser().getNickname())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .address(post.getUser().getAddress())
                    .images(post.getImages())
                    .currentState(post.getCurrentState())
                    .myItem(post.getMyItem())
                    .exchangeItem(post.getExchangeItem())
                    .createdAt(TimeConversion.timeConversion(post.getCreateAt()))
                    .bookmarkCnt(bookMarkRepository.countByPost(post).orElse(0))
                    .commentCnt(commentRepository.countByPost(post).orElse(0))
                    .build();


            return responseDto;
        }

        return null;
    }

    private BookMarkDto.DetailResponseDto toBookmarkResponseDto(BookMark bookMark) {

        return BookMarkDto.DetailResponseDto.builder()
                .userId(bookMark.getUser().getId())
                .build();
    }
    @Transactional
    public CategoryDto.ResponseDto cancelBookMark(User user, Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NullPointerException("해당 게시글이 존재하지 않습니다."));
        BookMark bookMark = bookMarkRepository.findByUserAndPost(user, post).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 대상입니다."));
        List<BookMark> bookMarks = bookMarkRepository.findByPost(post);
        for (int i = 0; i < bookMarks.size(); i++) {
            if(bookMarks.get(i).equals(bookMark)){
                bookMarks.remove(bookMarks.get(i));
            }
        }
        post.updatebookMark(bookMarks);
        bookMarkRepository.delete(bookMark);

        return CategoryDto.ResponseDto.builder()
                .bookMarks(post.getBookMarks().stream()
                        .map(this::toBookmarkResponseDto)
                        .collect(Collectors.toList()))
                .categoryName(post.getCategory())
                .postId(post.getId())
                .profileImg(post.getUser().getProfileImg())
                .username(post.getUser().getUsername())
                .nickname(post.getUser().getNickname())
                .title(post.getTitle())
                .content(post.getContent())
                .address(post.getUser().getAddress())
                .images(post.getImages())
                .currentState(post.getCurrentState())
                .myItem(post.getMyItem())
                .exchangeItem(post.getExchangeItem())
                .createdAt(TimeConversion.timeConversion(post.getCreateAt()))
                .bookmarkCnt(bookMarkRepository.countByPost(post).orElse(0))
                .commentCnt(commentRepository.countByPost(post).orElse(0))
                .build();
    }

    @Transactional
    public List<BookMarkDto.ResponseDto> findMyBookMark(User user) {
        if (user == null) throw new NullPointerException("로그인이 필요합니다");
        List<BookMark> bookMarkList =
                bookMarkRepository.findByUserUsername(user.getUsername());
        return bookMarkList.stream().map(s ->
                new BookMarkDto.ResponseDto(
                        s.getPost().getId(),
                        s.getPost().getUser().getId(),
                        s.getPost().getTitle(),
                        s.getPost().getImages().get(0).getImageUrl(),
                        s.getUser().getAddress(),
                        TimeConversion.timeConversion(s.getPost().getCreateAt()),
                        s.getPost().getCurrentState().name())
        ).collect(Collectors.toList());
    }

    private void impossibleMyPostBookMark(User user, Post post) {
        if (user.getUsername().equals(post.getUser().getUsername()))
            throw new IllegalArgumentException("자기 게시물을 즐겨찾기 할 수 없습니다.");
    }

    private boolean isNotAlreadyBookMark(User user, Post post) {
        return bookMarkRepository.existsByUserAndPost(user,post);
    }
}
