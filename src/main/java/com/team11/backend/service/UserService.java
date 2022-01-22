package com.team11.backend.service;


import com.team11.backend.dto.KakaoUserUpdateAddressDto;
import com.team11.backend.dto.MyPageDto;
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
                        .profileImg("https://media.vlpt.us/images/leejh4197/post/020fe273-f2c7-4c84-9cd5-a81ce6ea39f3/KakaoTalk_20220122_002028413.png")
                        .provider(AuthProvider.pingpong)
                        .build());
        return user.getId();
    }

    public MyPageDto.ResponseDto findMyPage(User user) {
        return MyPageDto.ResponseDto.builder()
                .username(user.getUsername())
                .nickname(user.getNickname())
                .profileImg(user.getProfileImg())
                .build();
    }

    @Transactional
    public MyPageDto.ResponseDto MyPageModify(User user, MyPageDto.RequestDto requestDto) {
        User userInfo = userRepository.findById(user.getId()).orElseThrow(() -> new NullPointerException("유저 정보가 존재하지 않습니다."));
        userInfo.update(requestDto);
        return MyPageDto.ResponseDto.builder()
                .username(userInfo.getUsername())
                .nickname(userInfo.getNickname())
                .profileImg(userInfo.getProfileImg())
                .build();

    }

    private void DuplicateUsernameAndNickname(SignupDto.RequestDto requestDto) {
        if (userRepository.existsByUsername(requestDto.getUsername()))
            throw new DuplicateKeyException("이미 존재하는 이메일 입니다");

        if (userRepository.existsByNickname(requestDto.getNickname()))
            throw new DuplicateKeyException("이미 존재하는 닉네임 입니다.");
    }

    // 아이디 중복검사, 반환값 없음
    public void usernameCheck(String username) {
        if (userRepository.existsByUsername(username))
            throw new DuplicateKeyException("이미 존재하는 이메일 입니다");
    }

    // 닉네임 중복검사, 반환값 없음
    public void nicknameCheck(String nickname) {
        if (userRepository.existsByNickname(nickname))
            throw new DuplicateKeyException("이미 존재하는 닉네임 입니다.");
    }

    @Transactional
    public KakaoUserUpdateAddressDto.ResponseDto updateKakaoInfo(KakaoUserUpdateAddressDto.RequestDto requestDto, User user) {
        User userInfo = userRepository.findById(user.getId()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
        userInfo.kakaoUserUpdateAddress(requestDto);
        return KakaoUserUpdateAddressDto.convertDto(userInfo);
    }
}
