package com.team11.backend.dto;

import lombok.*;

import java.util.List;

public class SearchDto {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RequestDto {
        private List<String> keyword;
    }

}
