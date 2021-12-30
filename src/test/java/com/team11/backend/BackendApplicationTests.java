package com.team11.backend;

import com.team11.backend.config.S3MockConfig;
import io.findify.s3mock.S3Mock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(S3MockConfig.class)
class BackendApplicationTests {

    @Autowired
    S3Mock s3Mock;

    @Test
    void contextLoads() {
    }

}
