package org.codiz.onshop.dtos.requests;

import lombok.Data;
import org.codiz.onshop.entities.users.Gender;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UserProfileUpdateRequest {
    private String userId;
    private String fullName;
    private String phoneNumber;
    private Gender gender;
    private String address;
}
