package com.ecommerce.library.service.impl;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;


public class GoogleUserInfoService {

    private final RestTemplate restTemplate;

    public GoogleUserInfoService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getEmailFromGoogle(OAuth2AccessToken accessToken) {
        String userInfoEndpoint = "https://www.googleapis.com/oauth2/v3/userinfo";
        Map<String, Object> userInfo = restTemplate.getForObject(userInfoEndpoint, Map.class);


        if (userInfo != null && userInfo.containsKey("email")) {
            return (String) userInfo.get("email");
        } else {
            throw new OAuth2AuthenticationException(new OAuth2Error("invalid_token"), "Invalid access token");
        }
    }
}
