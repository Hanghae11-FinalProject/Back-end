package com.team11.backend.controller;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.team11.backend.dto.HeaderDto;
import com.team11.backend.dto.MyPageDto;
import com.team11.backend.dto.SignupDto;
import com.team11.backend.security.UserDetailsImpl;
import com.team11.backend.service.KakaoUserService;
import com.team11.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;
    private final KakaoUserService kakaoUserService;

    @PostMapping("/user/signup")
    public Long Signup(@RequestBody @Valid SignupDto.RequestDto requestDto){
        return userService.signUp(requestDto);
    }

    //아이디 중복검사
    @PostMapping("/user/idCheck")
    public void idCheck(@RequestBody SignupDto.RequestDto signupRequestDto) {
        userService.usernameCheck(signupRequestDto.getUsername());
    }

    // 닉네임 중복검사
    @PostMapping("/user/nicknameCheck")
    public void nicknameCheck(@RequestBody SignupDto.RequestDto signupRequestDto) {
        userService.nicknameCheck(signupRequestDto.getNickname());
    }

    @GetMapping("/api/userInfos")
    public MyPageDto.ResponseDto MyPage(@AuthenticationPrincipal UserDetailsImpl customUserDetails){
        return userService.findMyPage(customUserDetails.getUser());
    }
 //프로필이미지, 닉네임, 유저네임
    @PutMapping("/api/userInfos")
    public MyPageDto.ResponseDto MyPageModify(@AuthenticationPrincipal UserDetailsImpl customUserDetails,
                             @RequestBody MyPageDto.RequestDto requestDto)
    {
        return userService.MyPageModify(customUserDetails.getUser(),requestDto);
    }


    @GetMapping("/oauth/callback/kakao")
    public HeaderDto kakaoLogin(
            @RequestParam String code
    ) throws JsonProcessingException {
        return kakaoUserService.kakaoLogin(code);
    }


}
