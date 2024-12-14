package org.codiz.onshop.configurations;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@ToString
public class MpesaConfigurationClass {
    @JsonProperty
    private String consumerKey;
    @JsonProperty
    private String consumerSecret;
    @JsonProperty
    private String grantType;
    @JsonProperty
    private String oauthEndpoint;
    @JsonProperty
    private String shortCode;
}
