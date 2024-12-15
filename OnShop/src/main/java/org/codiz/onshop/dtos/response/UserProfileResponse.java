package org.codiz.onshop.dtos.response;

import lombok.Data;

@Data
public class UserProfileResponse {
    private String username;
    private String email;
    private String fullName;
    private String profileImageUrl;
    private String phoneNumber;
    private String gender;
    private String address;
}
