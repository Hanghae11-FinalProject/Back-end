package com.team11.backend.service;

import com.team11.backend.dto.BookMarkDto;
import com.team11.backend.model.BookMark;
import com.team11.backend.model.Post;
import com.team11.backend.model.User;
import com.team11.backend.repository.BookMarkRepository;
import com.team11.backend.repository.PostRepository;
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

    @Transactional
    public boolean addBookMark(User user, Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NullPointerException("해당 게시글이 존재하지 않습니다."));
        impossibleMyPostBookMark(user,post);
        if (!isNotAlreadyBookMark(user,post)) {
            bookMarkRepository.save(BookMark.builder()
                    .user(user)
                    .post(post)
                    .build());
            return true;
        }
        return false;

    }

    @Transactional
    public Long cancelBookMark(User user, Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NullPointerException("해당 게시글이 존재하지 않습니다."));
        BookMark bookMark = bookMarkRepository.findByUserAndPost(user, post).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 대상입니다."));
        bookMarkRepository.delete(bookMark);
        return bookMark.getId();
    }

    public List<BookMarkDto> findMyBookMark(User user) {
        if (user == null) throw new NullPointerException("로그인이 필요합니다");
        List<BookMark> bookMarkList =
                bookMarkRepository.findByUserUsername(user.getUsername());
        return bookMarkList.stream().map(s -> new BookMarkDto(s.getPost().getId(), s.getPost().getTitle(), s.getPost().getCurrentState().name())).collect(Collectors.toList());
    }

    private void impossibleMyPostBookMark(User user, Post post) {
        if (user.getUsername().equals(post.getUser().getUsername()))
            throw new IllegalArgumentException("자기 게시물을 즐겨찾기 할 수 없습니다.");
    }

    private boolean isNotAlreadyBookMark(User user, Post post) {
        return bookMarkRepository.existsByUserAndPost(user,post);
    }
}
