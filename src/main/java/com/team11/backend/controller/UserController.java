package com.team11.backend.controller;
import com.team11.backend.dto.SignupDto;
import com.team11.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

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
}
