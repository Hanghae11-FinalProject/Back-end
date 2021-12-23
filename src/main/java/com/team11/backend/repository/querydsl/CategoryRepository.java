package com.team11.backend.repository.querydsl;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sun.org.apache.xpath.internal.operations.Bool;
import com.team11.backend.dto.CategoryDto;
import com.team11.backend.model.Post;
import com.team11.backend.model.QPost;
import com.team11.backend.model.QUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;


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
                        AllCategoryEq(categoryRequestDto.getCategoryName()),
                        AllCategoryCityGuEq(categoryRequestDto.getAddress())

                ).offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();


        List<Post> content = postQueryResults.getResults();
        long total = postQueryResults.getTotal();


        return new PageImpl<>(content, pageable, total);

    }

    private BooleanExpression AllCategoryEq(List<String> categoryName) {
        return categoryName != null ? Expressions.allOf(categoryName.stream().map(this::isFilterCategory).toArray(BooleanExpression[]::new)) : null;
    }

    private BooleanExpression isFilterCategory(String kinds) {
        return post.category.contains(kinds);
    }

    private BooleanExpression AllCategoryCityGuEq(String cityGu){
        return Optional.ofNullable(cityGu).map(user.address::contains).orElse(null);
    }
}
