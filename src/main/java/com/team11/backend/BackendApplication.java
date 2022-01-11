package com.team11.backend;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class BackendApplication {
    public static final String APPLICATION_LOCATIONS = "Dspring.profiles.active="
            + "/home/ubuntu/yarn/application.yml,"
            + "/home/ec2-user/app/config/springboot-webservice/real-application.yml";
    public static void main(String[] args) {
        //SpringApplication.run(BackendApplication.class, args);
        new SpringApplicationBuilder(BackendApplication.class)
                .properties(APPLICATION_LOCATIONS)
                .run(args);
    }
    }

