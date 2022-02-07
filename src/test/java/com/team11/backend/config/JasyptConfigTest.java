package com.team11.backend.config;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
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
        String key = "my_jasypt_key";
        StandardPBEStringEncryptor pbeEnc = new StandardPBEStringEncryptor();
        pbeEnc.setAlgorithm("PBEWithMD5AndDES");
        pbeEnc.setPassword(key);

        return pbeEnc.encrypt(value);
    }

}