package com.team11.backend.repository.querydsl;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team11.backend.dto.CategoryDto;
import com.team11.backend.dto.querydto.*;
import com.team11.backend.model.Post;
import com.team11.backend.repository.BookMarkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;
import static com.team11.backend.model.QBookMark.bookMark;
import static com.team11.backend.model.QImage.image;
import static com.team11.backend.model.QPost.post;
import static com.team11.backend.model.QUser.user;

@Repository
@Transactional
public class CategoryRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    @Autowired
    public CategoryRepository(EntityManager em, BookMarkRepository bookMarkRepository) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);

    }

    public Page<CategoryQueryDto> categoryFilter(CategoryDto.RequestDto categoryRequestDto, Pageable pageable) {

        List<CategoryQueryDto> result = queryFactory
                .select(new QCategoryQueryDto(
                        post.id, user.username, user.nickname, user.address, post.title,
                        user.profileImg, post.content, post.myItem, post.exchangeItem,
                        post.currentState, post.category, post.createdAt,post.bookMarkCnt
                        ,post.commentCnt
                ))
                .from(post)
                .leftJoin(post.user, user)
                .where(
                        CategoryEq(categoryRequestDto.getCategoryName()),
                        (CategoryCityFilter(categoryRequestDto.getAddress()))

                )
                .orderBy(post.createdAt.desc()) // 최신순 정렬
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .setHint("org.hibernate.readOnly", true)// dirty check를 위해 snapshot 인스턴스를 보관하므로 더 많은 메모리를 사용하는 단점이 있다. 대량의 데이터를 조회만 할거기 때문에 읽기 전용으로 메모리 사용량을 최적화할 수 있다
                .fetch();
        //COUNT 쿼리가 여러번 나가는 것을 방지하기위해 따로 작성
        JPAQuery<Post> count = queryFactory
                .select(post)
                .from(post)
                .leftJoin(post.user, user)
                .where(
                        CategoryEq(categoryRequestDto.getCategoryName()),
                        (CategoryCityFilter(categoryRequestDto.getAddress()))

                ).orderBy(post.createdAt.desc());
        return PageableExecutionUtils.getPage(result, pageable, count::fetchCount);
    }

    public List<ImageQueryDto> imageFilter(List<Long> postIdCollect) {
        return queryFactory
                .select(new QImageQueryDto(
                        post.id, image.id, image.imageName, image.imageUrl
                ))
                .from(image)
                .leftJoin(image.post, post)
                .where(post.id.in(postIdCollect))
                .fetch();
    }

    public List<BookMarkQueryDto> bookMarkFilter(List<Long> postIdCollect) {
        return queryFactory
                .select(new QBookMarkQueryDto(
                        post.id, bookMark.user.id
                ))
                .from(bookMark)
                .leftJoin(bookMark.post, post)
                .where(post.id.in(postIdCollect))
                .fetch();
    }

    private BooleanExpression CategoryEq(List<String> categoryName) {
        return categoryName != null ? Expressions.anyOf(categoryName.stream().map(this::isFilterCategory).toArray(BooleanExpression[]::new)) : null; //anyOf = ~~중 하나
    }

    private BooleanExpression isFilterCategory(String kinds) {
        return post.category.contains(kinds);
    }

    private BooleanExpression CategoryCityFilter(List<String> address){
        return address != null ? Expressions.allOf(address.stream().map(this::isFilterAddress).toArray(BooleanExpression[]::new)) : null; //allOf = 모든조건
    }

    private BooleanExpression isFilterAddress(String address){
        return post.user.address.contains(address);
    }


}
