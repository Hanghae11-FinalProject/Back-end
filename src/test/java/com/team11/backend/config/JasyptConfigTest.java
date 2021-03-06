package com.team11.backend.config;

import org.assertj.core.api.Assertions;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

/*

@SpringBootTest
@Transactional
@Rollback
class JasyptConfigTest {

    public String jasyptEncoding(String value) {
        String key = "my_jasypt_key";
        StandardPBEStringEncryptor pbeEnc = new StandardPBEStringEncryptor();
        pbeEnc.setAlgorithm("PBEWithMD5AndDES");
        pbeEnc.setPassword(key);

        return pbeEnc.encrypt(value);
    }

    public String jasyptDecryt(String value) {
        String key  = "my_jasypt_key";
        StandardPBEStringEncryptor pbeEnc = new StandardPBEStringEncryptor();
        pbeEnc.setAlgorithm("PBEWithMD5AndDES");
        pbeEnc.setPassword(key);

        return pbeEnc.decrypt(value);
    }

    @Test
    void jasypt(){

        String url = "jdbc:mysql://gongudatabase.cy2jtmlet03t.ap-northeast-2.rds.amazonaws.com:3306/gongu?serverTimezone=Asia/Seoul";
        String username = "woojins";
        String password = "47429468bb";

        String encryptUrl = jasyptEncoding(url);
        String encryptUsername = jasyptEncoding(username);
        String encryptPassword = jasyptEncoding(password);

        Assertions.assertThat(url).isEqualTo(jasyptDecryt(encryptUrl));
        Assertions.assertThat(username).isEqualTo(jasyptDecryt(encryptUsername));
        Assertions.assertThat(password).isEqualTo(jasyptDecryt(encryptPassword));
    }


}
*/
