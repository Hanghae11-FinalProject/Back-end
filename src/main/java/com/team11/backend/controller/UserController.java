package com.team11.backend.controller;
import com.team11.backend.dto.MyPageDto;
import com.team11.backend.dto.SignupDto;
import com.team11.backend.security.oauth2.service.CustomUserDetails;
import com.team11.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;

    @PostMapping("/user/signup")
    public Long Signup(@RequestBody @Valid SignupDto.RequestDto requestDto){
        return userService.signUp(requestDto);
    }

    @GetMapping("/api/userInfos")
    public MyPageDto.ResponseDto MyPage(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        return userService.findMyPage(customUserDetails.getUser());
    }

    @PutMapping("/api/userInfos")
    public Long MyPageModify(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                             @RequestBody MyPageDto.RequestDto requestDto)
    {
        return userService.MyPageModify(customUserDetails.getUser(),requestDto);
    }


}
