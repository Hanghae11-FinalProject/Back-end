package com.team11.backend.config;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import static org.junit.jupiter.api.Assertions.*;

/*
@SpringBootTest
@Rollback
class JasyptConfigTest {

    @Test
    void contextLoads(){

    }

    @Test
    void jasypt(){
        String url = "jdbc:mysql://gongudatabase.cy2jtmlet03t.ap-northeast-2.rds.amazonaws.com:3306/gongu?serverTimezone=Asia/Seoul";
        String username = "woojins";
        String password = "47429468bb";

        System.out.println(jasyptEncoding(url));
        System.out.println(jasyptEncoding(username));
        System.out.println(jasyptEncoding(password));
    }

    public String jasyptEncoding(String value){
        String key = "안알랴줌";
        StandardPBEStringEncryptor pbeEnc = new StandardPBEStringEncryptor();
        pbeEnc.setAlgorithm("안알랴줌");
        pbeEnc.setPassword(key);

        return pbeEnc.encrypt(value);
    }

}*/
