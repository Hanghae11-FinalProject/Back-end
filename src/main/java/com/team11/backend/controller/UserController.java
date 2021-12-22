package com.team11.backend.controller;
import com.team11.backend.dto.Signup;
import com.team11.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;
    @PostMapping("/user/signup")
    public Long Signup(@RequestBody Signup signup){
        return userService.signup(signup);
    }

}
