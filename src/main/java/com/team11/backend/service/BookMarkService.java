package com.team11.backend.service;

import com.team11.backend.dto.BookMarkDto;
import com.team11.backend.dto.CategoryDto;
import com.team11.backend.model.BookMark;
import com.team11.backend.model.Post;
import com.team11.backend.model.User;
import com.team11.backend.repository.BookMarkRepository;
import com.team11.backend.repository.CommentRepository;
import com.team11.backend.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import static com.team11.backend.dto.BookMarkDto.convertToBookMarkDto;
import static com.team11.backend.dto.CategoryDto.convertToCategoryDto;

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
            post.addBookMarkCount();

            return convertToCategoryDto(post,bookMarkRepository,commentRepository);
        }

        return null;
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
        post.minusBookMarkCount();
        bookMarkRepository.delete(bookMark);

        return convertToCategoryDto(post,bookMarkRepository,commentRepository);
    }

    @Transactional
    public List<BookMarkDto.ResponseDto> findMyBookMark(User user) {
        if (user == null) throw new NullPointerException("로그인이 필요합니다");
        List<BookMark> bookMarkList = bookMarkRepository.findByUserUsername(user.getUsername());

        return convertToBookMarkDto(bookMarkList);
    }

    private void impossibleMyPostBookMark(User user, Post post) {
        if (user.getUsername().equals(post.getUser().getUsername()))
            throw new IllegalArgumentException("본인 게시물은 즐겨찾기 할 수 없습니다.");
    }

    private boolean isNotAlreadyBookMark(User user, Post post) {
        return bookMarkRepository.existsByUserAndPost(user,post);
    }
}
