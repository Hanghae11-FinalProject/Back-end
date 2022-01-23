package com.team11.backend.repository.querydsl;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team11.backend.dto.querydto.*;
import com.team11.backend.model.Comment;
import com.team11.backend.repository.BookMarkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;

import static com.team11.backend.model.QBookMark.bookMark;
import static com.team11.backend.model.QComment.comment;
import static com.team11.backend.model.QImage.image;
import static com.team11.backend.model.QPost.post;
import static com.team11.backend.model.QTag.tag;
import static com.team11.backend.model.QUser.user;

@Repository
@Transactional
public class PostQueryRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    @Autowired
    public PostQueryRepository(EntityManager em , BookMarkRepository bookMarkRepository) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);

    }

    public PostDetailQueryDto postDetail(Long postId) {
        return queryFactory
                .select(new QPostDetailQueryDto(
                        post.id,user.id, post.user.username, post.user.nickname, post.user.address, post.title,
                        post.user.profileImg, post.content, post.myItem, post.exchangeItem,
                        post.currentState, post.category, post.createdAt,post.bookMarkCnt,post.commentCnt
                       /* ExpressionUtils.as(
                                JPAExpressions.select(bookMark.count())
                                        .from(bookMark)
                                        .where(bookMark.post.eq(post))
                                , "bookmarkCnt"
                        ),
                        ExpressionUtils.as(
                                JPAExpressions.select(comment.count())
                                        .from(comment)
                                        .where(comment.post.eq(post))
                                , "commentCnt"
                        )*/
                ))
                .from(post)
                .leftJoin(post.user, user)
                .where(
                        post.id.eq(postId)
                )
                .orderBy(post.createdAt.desc()) // 최신순 정렬
                .setHint("org.hibernate.readOnly", true)// dirty check를 위해 snapshot 인스턴스를 보관하므로 더 많은 메모리를 사용하는 단점이 있다. 대량의 데이터를 조회만 할거기 때문에 읽기 전용으로 메모리 사용량을 최적화할 수 있다
                .fetchOne();
    }

    public List<ImageQueryDto> imageData(Long postId) {
        return queryFactory
                .select(new QImageQueryDto(
                        post.id,image.id,image.imageName,image.imageUrl
                ))
                .from(image)
                .leftJoin(image.post, post)
                .where(post.id.eq(postId))
                .fetch();
    }

    public List<BookMarkQueryDto> bookMarkData(Long postId) {
        return queryFactory
                .select(new QBookMarkQueryDto(
                        post.id,bookMark.user.id
                ))
                .from(bookMark)
                .leftJoin(bookMark.post, post)
                .where(post.id.eq(postId))
                .fetch();
    }

    public List<TagQueryDto> tagData(Long postId){
        return queryFactory
                .select(new QTagQueryDto(
                        post.id,tag.id,tag.tagName
                ))
                .from(tag)
                .leftJoin(tag.post,post)
                .where(post.id.eq(postId))
                .fetch();
    }

/*    public List<Comment> commentData(Long postId){
        return queryFactory
                .select(
                        post.id, comment.id, comment.content,
                        comment.user.id,comment.user.nickname,
                        comment.user.profileImg,comment.createdAt.stringValue()
                )
                .from(comment)
                .leftJoin(comment.post,post)
                .where(post.id.eq(postId))
                .fetch();
    }*/
   public List<Comment> commentData(Long postId){
        return queryFactory
                .select(comment)
                .from(comment)
                .leftJoin(comment.post,post)
                .where(post.id.eq(postId))
                .fetch();
    }
}
