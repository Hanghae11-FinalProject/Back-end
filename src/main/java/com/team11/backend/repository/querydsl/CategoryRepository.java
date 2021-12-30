package com.team11.backend.repository.querydsl;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team11.backend.dto.CategoryDto;
import com.team11.backend.model.Post;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static com.team11.backend.model.QPost.post;
import static com.team11.backend.model.QUser.user;

@Repository
@Transactional
public class CategoryRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public CategoryRepository(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    public PageImpl<Post> categoryFilter(CategoryDto.RequestDto categoryRequestDto, Pageable pageable) {

        QueryResults<Post> postQueryResults = queryFactory
                .select(post)
                .from(post)
                .leftJoin(post.user, user)
                .where(
                        CategoryEq(categoryRequestDto.getCategoryName()).and(CategoryCityFilter(categoryRequestDto.getAddress()))

                ).offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .setHint("org.hibernate.readOnly", true)// dirty check를 위해 snapshot 인스턴스를 보관하므로 더 많은 메모리를 사용하는 단점이 있다. 대량의 데이터를 조회만 할거기 때문에 읽기 전용으로 메모리 사용량을 최적화할 수 있다
                .fetchResults();


        List<Post> content = postQueryResults.getResults();
        long total = postQueryResults.getTotal();

        return new PageImpl<>(content, pageable, total);

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
