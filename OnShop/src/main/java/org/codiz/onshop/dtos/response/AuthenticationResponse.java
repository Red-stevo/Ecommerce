package org.codiz.onshop.dtos.response;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class AuthenticationResponse {
    private  String token;

    private  String message;

    private String userId;

    private String userRole;
    
}
