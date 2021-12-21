package com.team11.backend.dto;

import lombok.Getter;
import lombok.Setter;

public class TagDto {

    @Setter
    @Getter
    public static class RequestDto{
        private String tagName;
    }
}
