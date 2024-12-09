package org.codiz.onshop.dtos.requests;

import lombok.Data;

@Data
public class LoginRequests {
    private String email;

    private String password;
}
