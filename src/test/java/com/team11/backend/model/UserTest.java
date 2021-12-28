package com.team11.backend.model;

import com.team11.backend.dto.MyPageDto;
import com.team11.backend.dto.SignupDto;
import com.team11.backend.repository.UserRepository;
import com.team11.backend.service.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

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
@Transactional
@Rollback
class UserTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Validator validator;

    //given
    private SignupDto.RequestDto requestDto = SignupDto.RequestDto.builder()
            .nickname("woojin126@naver.com")
            .username("woojin")
            .password("47429468bB")
            .build();

    private User user;

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


            @DisplayName("마이 페이지")
            @Order(2)
            @Test
            void test_3(){

                MyPageDto.ResponseDto myPage = userService.findMyPage(user);

                assertEquals(user.getNickname(), myPage.getNickname());
                assertEquals(user.getProfileImg(), myPage.getProfileImg());
            }


            @DisplayName("마이 페이지에서 회원정보 수정")
            @Order(3)
            @Test
            void test_4(){
                MyPageDto.RequestDto modifiedUserInfo = MyPageDto.RequestDto.builder()
                        .nickname("변경된닉네임")
                        .profileImg("변경된이미지")
                        .build();
                Long users = userService.MyPageModify(user, modifiedUserInfo);

                User user = userRepository.findById(users).get();

                assertEquals("변경된닉네임", user.getNickname());
                assertEquals("변경된이미지", user.getProfileImg());

            }

        }



        
        @DisplayName("회원가입 실패 케이스")
        @Nested
        class Fail{
            @DisplayName("@Valid 닉네임,유저네임,password 테스트")
            @Test
            void test_2(){
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
            
        }
        
    }

}
