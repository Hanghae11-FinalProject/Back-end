package com.team11.backend.service;


import com.team11.backend.dto.SignupDto;
import com.team11.backend.model.AuthProvider;
import com.team11.backend.model.User;
import com.team11.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final BCryptPasswordEncoder encodePassword;
    private final UserRepository userRepository;

    @Transactional
    public Long signUp(SignupDto.RequestDto requestDto) {
        DuplicateUsernameAndNickname(requestDto);
        User user = userRepository.save(
                User.builder()
                .username(requestDto.getUsername())
                .nickname(requestDto.getNickname())
                .password(encodePassword.encode(requestDto.getPassword()))
                .address(requestDto.getAddress())
                .provider(AuthProvider.pingpong)
                .build());
        return user.getId();
    }

    private void DuplicateUsernameAndNickname(SignupDto.RequestDto requestDto) {
        if (userRepository.existsByUsername(requestDto.getUsername()))
            throw new DuplicateKeyException("이미 존재하는 이메일 입니다");

        if (userRepository.existsByNickname(requestDto.getNickname()))
            throw new DuplicateKeyException("이미 존재하는 닉네임 입니다.");
    }
}
