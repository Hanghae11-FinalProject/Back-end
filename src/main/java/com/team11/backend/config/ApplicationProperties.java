package com.team11.backend.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
@ConfigurationProperties(value = "application")
public class ApplicationProperties {

    private final Security security = new Security();

    public Security getSecurity() {
        return security;
    }

    public static class Security {

        private final Jwt jwt = new Jwt();

        public Jwt getJwt() {
            return jwt;
        }

        @Getter
        @Setter
        public static class Jwt {

            private String secret;

            private long tokenValidityInSeconds = 18000;

            private long tokenValidityInSecondsForRememberMe = 2592000;

            private long refreshValidityInMilliseconds = 60000;


        }
    }

    private final OAuth2 oAuth2 = new OAuth2();

    public static final class OAuth2 {
        private List<String> authorizedRedirectUris = new ArrayList<>();

        public List<String> getAuthorizedRedirectUris() {
            return authorizedRedirectUris;
        }

        public OAuth2 authorizedRedirectUris(List<String> authorizedRedirectUris) {
            this.authorizedRedirectUris = authorizedRedirectUris;
            return this;
        }
    }

    public OAuth2 getoAuth2() {
        return oAuth2;
    }
}
