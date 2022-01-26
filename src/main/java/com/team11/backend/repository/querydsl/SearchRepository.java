package com.team11.backend.repository.querydsl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team11.backend.dto.SearchDto;
import com.team11.backend.dto.querydto.QSearchQueryDto;
import com.team11.backend.dto.querydto.SearchQueryDto;
import com.team11.backend.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import javax.persistence.EntityManager;

import java.util.List;

import static com.team11.backend.model.QPost.post;
import static com.team11.backend.model.QUser.user;

@Repository
@Transactional
public class SearchRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public SearchRepository(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }


    public Page<SearchQueryDto> keywordFilter(SearchDto.RequestDto searchRequestDto, Pageable pageable) {

        List<SearchQueryDto> result = queryFactory
                .select(new QSearchQueryDto(
                        post.id, user.nickname, post.title, post.content, user.address
                        , post.myItem, post.exchangeItem, user.profileImg, post.currentState,
                        post.createdAt, post.bookMarkCnt, post.commentCnt
                ))
                .from(post)
                .leftJoin(post.user, user)
                .where(
                        SearchKeywordFilter(searchRequestDto.getKeyword())
                )
                .orderBy(post.currentState.asc(), post.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Post> count = queryFactory
                .select(post)
                .from(post)
                .leftJoin(post.user, user)
                .where(
                        SearchKeywordFilter(searchRequestDto.getKeyword())
                ).orderBy(post.currentState.asc(), post.createdAt.desc());

        return PageableExecutionUtils.getPage(result, pageable, count::fetchCount);

    }

    private BooleanExpression SearchKeywordFilter(List<String> Keyword) {
        return Keyword != null ? Expressions.allOf(Keyword.stream().map(this::isFilterSearch).toArray(com.querydsl.core.types.dsl.BooleanExpression[]::new)) : null;
    }

    private BooleanExpression tagContain(String keyword) {
        return keyword != null ? Expressions.allOf(post.tags.any().tagName.contains(keyword)) : null;
    }

    private BooleanExpression isFilterSearch(String keyword) {
        return post.title.containsIgnoreCase(keyword)
                .or(post.category.containsIgnoreCase(keyword))
                .or(post.content.containsIgnoreCase(keyword))
                .or(tagContain(keyword))
                .or(post.myItem.containsIgnoreCase(keyword))
                .or(post.exchangeItem.containsIgnoreCase(keyword));
    }
}
