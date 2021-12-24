package com.team11.backend.repository.querydsl;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team11.backend.dto.SearchDto;
import com.team11.backend.model.Post;
import com.team11.backend.model.QTimestamped;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;

import static com.team11.backend.model.QPost.post;
import static com.team11.backend.model.QTag.tag;

@Repository
@Transactional
public class SearchRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public SearchRepository(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    public PageImpl<Post> keywordFilter(SearchDto.RequestDto searchRequestDto, Pageable pageable) {

        QueryResults<Post> postQueryResults = queryFactory
                .select(post)
                .from(post)
                .leftJoin(post.tags, tag)

                .where(
                        SearchKeywordFilter(searchRequestDto.getKeyword())
//                        .and(tag.in(post.tags))
                )
//                .orderBy(post.currentState.asc(), QTimestamped.timestamped.createAt.desc()) // 최신순 정렬
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<Post> content = postQueryResults.getResults();
        long total = postQueryResults.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression SearchKeywordFilter(List<String> Keyword) {
        return Keyword != null ? Expressions.allOf(Keyword.stream().map(this::isFilterSearch).toArray(com.querydsl.core.types.dsl.BooleanExpression[]::new)) : null;
    }

    private BooleanExpression isFilterSearch(String keyword) {
        return post.title.containsIgnoreCase(keyword).or(post.content.containsIgnoreCase(keyword)).or(tag.tagName.contains(keyword));
    }
}
