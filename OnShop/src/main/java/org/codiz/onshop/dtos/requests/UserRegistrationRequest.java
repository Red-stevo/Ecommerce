package org.codiz.onshop.dtos.requests;

import lombok.Data;

import org.codiz.onshop.entities.users.Role;

@Data
public class UserRegistrationRequest {
    private String username;
    private String phoneNumber;
    private String userEmail;
    private String password;
    private Role role;
}
