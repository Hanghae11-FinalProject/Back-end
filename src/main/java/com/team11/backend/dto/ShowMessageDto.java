package com.team11.backend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


public class ShowMessageDto {

    @Getter
    @Setter
    @Builder
    public static class RequestDto{
        private String roomName;
    }

    @Getter
    @Setter
    @Builder
    public static class ResponseDto{

    }
}
