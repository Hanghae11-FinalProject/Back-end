package com.team11.backend.service;
/*

import com.team11.backend.config.S3MockConfig;
import com.team11.backend.dto.KakaoUserUpdateAddressDto;
import com.team11.backend.dto.MyPageDto;
import com.team11.backend.dto.SignupDto;
import com.team11.backend.model.User;
import com.team11.backend.repository.UserRepository;
import io.findify.s3mock.S3Mock;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.annotation.Rollback;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@EnableAutoConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Import(S3MockConfig.class)
@Rollback
class UserServiceTest {

    @Autowired
    private S3Mock s3Mock;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Validator validator;

    //given
    private final SignupDto.RequestDto requestDto = SignupDto.RequestDto.builder()
            .nickname("woojin")
            .username("woojin126@naver.com")
            .password("47429468bB")
            .address("서울특별시 강남구")
            .build();

    private User user;

    @Test
    @DisplayName("카카오 로그인 성공 후 주소정보 입력받기 검증")
    void addressTest(){

        SignupDto.RequestDto requestDto = SignupDto.RequestDto.builder()
                .nickname("카카오")
                .username("kakao@naver.com")
                .password("wefwef1bC")
                .build();
        Long user = userService.signUp(requestDto);

        KakaoUserUpdateAddressDto.RequestDto addressDto = KakaoUserUpdateAddressDto.RequestDto.builder()
                .address("경기도 수원시")
                .build();

        User user1 = userRepository.findById(user).get();
        KakaoUserUpdateAddressDto.ResponseDto responseDto = userService.updateKakaoInfo(addressDto, user1);

        assertEquals(user1.getUsername(), responseDto.getUsername());
        assertEquals(user1.getNickname(), responseDto.getNickName());
        assertEquals("경기도 수원시", responseDto.getAddress());


    }

    @DisplayName("회원")
    @Nested
    class register {
        @DisplayName("회원 가입 성공케이스")
        @Nested
        class Success {


            @DisplayName("1.회원가입")
            @Order(1)
            @Test
            void test_1() throws Exception {
                //when
                Long userId = userService.signUp(requestDto);
                user = userRepository.findById(userId).get();
                //then
                assertEquals(user.getId(), userId);
            }


            @DisplayName("마이 페이지 조회")
            @Order(2)
            @Test
            void test_2(){

                MyPageDto.ResponseDto myPage = userService.findMyPage(user);

                assertEquals(user.getNickname(), myPage.getNickname());
                assertEquals(user.getProfileImg(), myPage.getProfileImg());
            }


            @DisplayName("마이 페이지에서 회원정보 수정")
            @Order(3)
            @Test
            void test_3(){
                MyPageDto.RequestDto modifiedUserInfo = MyPageDto.RequestDto.builder()
                        .nickname("변경된닉네임")
                        .profileImg("변경된이미지")
                        .build();
                MyPageDto.ResponseDto responseDto = userService.MyPageModify(user, modifiedUserInfo);

                assertEquals(responseDto.getNickname(), modifiedUserInfo.getNickname());
                assertEquals(responseDto.getProfileImg(), modifiedUserInfo.getProfileImg());

            }

        }

        @DisplayName("회원가입 실패 케이스")
        @Nested
        class Fail{
            @DisplayName("@Valid 닉네임,유저네임,password 테스트")
            @Test
            void test_4(){
                //given
                SignupDto.RequestDto requestDto = SignupDto.RequestDto.builder()
                        .nickname("@@")
                        .username("woojin126naver.com")
                        .password("1234")
                        .build();
                //when
                Set<ConstraintViolation<SignupDto.RequestDto>> validate =  validator.validate(requestDto);

                //then

                Iterator<ConstraintViolation<SignupDto.RequestDto>> iterator = validate.iterator();
                List<String> messages = new ArrayList<>();
                while (iterator.hasNext()){
                    ConstraintViolation<SignupDto.RequestDto> next = iterator.next();
                    messages.add(next.getMessage());
                }

                Assertions.assertThat(messages).contains("이메일 주소 형식이 아닙니다.");
                Assertions.assertThat(messages).contains("닉네임은 한글,영숫자, 2자이상 10자이하");
                Assertions.assertThat(messages).contains("비밀번호는 대소문자,숫자,특수문자 하나이상 필수, 8자이상 16자이하");
            }

            @DisplayName("이메일 중복확인")
            @Test
            void test_5(){

                SignupDto.RequestDto requestDto2 = SignupDto.RequestDto.builder()
                        .nickname("green")
                        .username("woojin1267@naver.com")
                        .password("47429468bB")
                        .build();
                userService.signUp(requestDto2);

                SignupDto.RequestDto requestDto1 = SignupDto.RequestDto.builder()
                        .nickname("greens")
                        .username("woojin1267@naver.com")
                        .password("47429468bB")
                        .build();

                DuplicateKeyException duplicateKeyException = assertThrows(DuplicateKeyException.class, () -> userService.usernameCheck(requestDto1.getUsername()));
                assertEquals("이미 존재하는 이메일 입니다", duplicateKeyException.getMessage());
            }

            @DisplayName("닉네임 중복확인")
            @Test
            void test_6(){
                SignupDto.RequestDto requestDto2 = SignupDto.RequestDto.builder()
                        .nickname("green")
                        .username("wooji@google.com")
                        .password("47429468bB")
                        .build();
                userService.signUp(requestDto2);
                SignupDto.RequestDto requestDto1 = SignupDto.RequestDto.builder()
                        .nickname("green")
                        .username("woojinav@google.com")
                        .password("47429468bB")
                        .build();
                DuplicateKeyException duplicateKeyException = assertThrows(DuplicateKeyException.class, () -> userService.nicknameCheck(requestDto1.getNickname()));
                assertEquals("이미 존재하는 닉네임 입니다.", duplicateKeyException.getMessage());
            }
        }
    }


    @AfterEach
    public void shutdownMockS3(){
        s3Mock.stop();
    }
}



*/
