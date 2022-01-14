package com.team11.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchRankResponseDto {
//    List<String> SearchRankList;
    String rank_Keyword;
    int score;

    public static SearchRankResponseDto convertToResponseRankingDto(ZSetOperations.TypedTuple typedTuple) {
        SearchRankResponseDto searchRankResponseDtoDto = new SearchRankResponseDto();
        searchRankResponseDtoDto.rank_Keyword = typedTuple.getValue().toString();
        searchRankResponseDtoDto.score = typedTuple.getScore().intValue();
        return searchRankResponseDtoDto;
    }
}
