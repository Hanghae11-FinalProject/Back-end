package com.team11.backend.model;

import com.sun.istack.NotNull;
import com.team11.backend.dto.KakaoUserUpdateAddressDto;
import com.team11.backend.dto.MyPageDto;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    // 사용자 Email == 사용자 ID
    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = true)
    private String address;

    @Column(nullable = true)
    private String profileImg;

    @Column(nullable = false)
    private String password;

    @Column(name = "PROVIDER", length = 20)
    @Enumerated(EnumType.STRING)
    @NotNull
    private AuthProvider provider;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private final Set<BookMark> bookMarks = new HashSet<>();


    public void update(MyPageDto.RequestDto requestDto) {
        this.nickname = requestDto.getNickname();
        this.profileImg = requestDto.getProfileImg();
    }

    public void kakaoUserUpdateAddress(KakaoUserUpdateAddressDto.RequestDto requestDto) {
        this.profileImg = "https://media.vlpt.us/images/leejh4197/post/020fe273-f2c7-4c84-9cd5-a81ce6ea39f3/KakaoTalk_20220122_002028413.png";
        this.address = requestDto.getAddress();
    }
}