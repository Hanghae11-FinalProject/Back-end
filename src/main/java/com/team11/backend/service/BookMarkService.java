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
    
    //북마크 등록
    @Transactional
    public CategoryDto.ResponseDto addBookMark(User user, Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NullPointerException("해당 게시글이 존재하지 않습니다."));
        impossibleMyPostBookMark(user,post);//자기자신의 게시물 북마크 불가능
        if (!isNotAlreadyBookMark(user,post)) { //이미 북마크를 눌렀는지 안눌렀는지 확인
            BookMark bookMark = BookMark.builder()
                    .user(user)
                    .post(post)
                    .build();
            bookMarkRepository.save(bookMark);
            post.addBookMarkCount();//북마크 등록 할때마다 +1 (해당 게시물의 즐겨찾기 개수를 나타내기위해)

            return convertToCategoryDto(post,bookMarkRepository,commentRepository);
        }

        return null;
    }

    //북마크 취소
    @Transactional
    public CategoryDto.ResponseDto cancelBookMark(User user, Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NullPointerException("해당 게시글이 존재하지 않습니다."));
        BookMark bookMark = bookMarkRepository.findByUserAndPost(user, post).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 대상입니다."));
        bookMarkRepository.deleteBookMarkByPostIdAndUser(postId, user);
        if (post.getBookMarkCnt() > 0)
            post.minusBookMarkCount();//북마크를 취소시 개수 -1
        bookMarkRepository.delete(bookMark);

        return convertToCategoryDto(post,bookMarkRepository,commentRepository);
    }

    //내 북마크 목록 전체 조회
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
