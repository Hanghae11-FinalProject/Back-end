package com.team11.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

//nginx로 현재 구동중인 서버의 포트번호를 구분하기위해
@RequiredArgsConstructor
@RestController
public class ProfileController {

    private final Environment env;

    @GetMapping("/profile")
    public String getProfile() {
        String[] str = env.getActiveProfiles();
        int idx = 0;
        for(int i = 0; i < str.length; i++){
            if(str[i].contains("set")){
                idx = i;
                break;
            }
        }
//.
        return str[idx];
    }
}