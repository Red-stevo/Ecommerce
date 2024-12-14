package org.codiz.onshop.dtos.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.SneakyThrows;

@Data
public class MpesaAccessTokenResponse {
    @JsonProperty
    private String accessToken;
    @JsonProperty
    private String expiresIn;

    @SneakyThrows
    @Override
    public String toString(){
        return new ObjectMapper().writeValueAsString(this.accessToken);
    }

}
