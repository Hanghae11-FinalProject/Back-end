package com.team11.backend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MessageContentDto {
    private String content;
    private String createdAt;
}
