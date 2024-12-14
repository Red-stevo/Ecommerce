package org.codiz.onshop.service.impl;

import lombok.AllArgsConstructor;
import org.codiz.onshop.configurations.DarajaConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.Map;
@AllArgsConstructor
@Service
public class TokenService {

    private final DarajaConfig config;
    private final RestTemplate restTemplate;

    public String getAccessToken() {
        String auth = config.getConsumerKey() + ":" + config.getConsumerSecret();
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + encodedAuth);

        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                config.getBaseUrl() + "/oauth/v1/generate?grant_type=client_credentials",
                HttpMethod.GET,
                request,
                Map.class
        );

        return response.getBody().get("access_token").toString();
    }
}



