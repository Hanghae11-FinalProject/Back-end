package com.team11.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;


public class SignupDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class RequestDto{

        @Email(message = "이메일 주소 형식이 아닙니다.")
        @NotBlank
        private String username;

        @NotBlank(message = "비밀번호는 필수 입력 값 입니다.")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*#?&]{8,16}$"
                ,message = "비밀번호는 대소문자,숫자,특수문자 하나이상 필수, 8자이상 16자이하")
        private String password;

        @NotBlank(message = "비밀번호 확인 필수 입력 값 입니다.")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*#?&]{8,16}$"
                ,message = "비밀번호 확인은 대소문자,숫자,특수문자 하나이상 필수, 8자이상 16자이하")
        private String passwordCheck;

        @NotBlank
        @Pattern(regexp = "^[ㄱ-ㅎ가-힣0-9a-zA-Z]{2,10}$"
                ,message = "닉네임은 한글,영숫자, 2자이상 10자이하")
        private String nickname;

        @NotBlank
        private String address;

        private String profileImg;
    }


    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ResponseDto{
        private Long userId;
        private String profileImg;
        private String nickName;
    }
}