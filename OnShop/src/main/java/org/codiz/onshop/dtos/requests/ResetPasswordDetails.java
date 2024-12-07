package org.codiz.onshop.dtos.requests;

import lombok.Data;

@Data
public class ResetPasswordDetails {

    private String email;
    private String oldPassword;
    private String newPassword;
}
