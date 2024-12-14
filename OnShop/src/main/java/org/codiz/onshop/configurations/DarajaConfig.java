package org.codiz.onshop.configurations;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@Data
public class DarajaConfig {
    @Value("${consumer-key}")
    private String consumerKey;

    @Value("${consumer-secret}")
    private String consumerSecret;

    @Value("${base-url}")
    private String baseUrl;
    @Value("${short-code}")
    private String shortCode;

    @Value("${passkey}")
    private String passKey;

    @Value("${callback-url}")
    private String callbackUrl;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
