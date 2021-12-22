package com.team11.backend.service;


import com.team11.backend.dto.Signup;
import com.team11.backend.dto.SignupDto;
import com.team11.backend.model.User;
import com.team11.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final BCryptPasswordEncoder encodePassword;
    private final UserRepository userRepository;

    @Transactional
    public Long signup(Signup signup) {

        String encode = encodePassword.encode(signup.getPassword());
        User build = User.builder()
                .username(signup.getUsername())
                .nickname(signup.getNickname())
                .password(encode)
                .build();


        User save = userRepository.save(build);

        return save.getId();
    }
}
