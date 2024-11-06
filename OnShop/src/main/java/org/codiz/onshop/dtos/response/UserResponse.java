package org.codiz.onshop.dtos.response;

import lombok.Data;
import org.codiz.onshop.entities.users.Role;
@Data
public class UserResponse {
    private String username;
    private String phoneNumber;
    private String userEmail;
    private String password;
    private Role role;
}
